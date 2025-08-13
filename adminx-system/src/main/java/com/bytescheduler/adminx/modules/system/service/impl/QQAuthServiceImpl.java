package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.config.QQConfig;
import com.bytescheduler.adminx.modules.system.dto.response.QQUserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.service.UserService;
import com.bytescheduler.adminx.modules.system.utils.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bytescheduler.adminx.modules.system.enums.UserStatus;
import com.bytescheduler.adminx.modules.system.service.QQAuthService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * @author byte-scheduler
 * @since 2025/8/11
 */
@RequiredArgsConstructor
@Service
public class QQAuthServiceImpl implements QQAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QQAuthServiceImpl.class);
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String CALLBACK_PREFIX = "callback(";
    private static final String CALLBACK_SUFFIX = ");";
    private static final int HTTP_OK = 200;

    private final QQConfig qqConfig;
    private final AuthUtil authUtil;
    private final UserService userService;
    private final HttpRequestIpResolver ipResolver;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TokenResponse qqLogin(String code, HttpServletRequest request) {
        String clientRealIp = ipResolver.resolve(request);

        try {
            String accessToken = getAccessToken(code);
            String openid = getOpenid(accessToken);
            QQUserInfoResponse userQQInfo = getUserInfo(accessToken, openid);

            SysUser sysUser = userService.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getOpenId, userQQInfo.getOpenId()));

            if (sysUser != null) {
                return handleExistingUser(sysUser, clientRealIp);
            } else {
                return handleNewUser(userQQInfo, clientRealIp);
            }
        } catch (BusinessException businessException) {
            // 已知业务异常直接抛出
            throw businessException;
        } catch (Exception e) {
            LOGGER.error("QQ login failed: {}", e.getMessage(), e);
            throw new BusinessException("QQ登录服务异常");
        }
    }

    private TokenResponse handleExistingUser(SysUser user, String clientIp) {
        if (user.getStatus() == UserStatus.DISABLED.getCode()) {
            throw new BusinessException("当前用户已被禁用");
        }

        updateUserLoginInfo(user.getUserId(), clientIp);
        return authUtil.generateToken(user);
    }

    private TokenResponse handleNewUser(QQUserInfoResponse qqInfo, String clientIp) {
        SysUser newUser = new SysUser()
                .setOpenId(qqInfo.getOpenId())
                .setNickname(qqInfo.getNickname())
                .setPassword("")
                .setAvatar(qqInfo.getQqAvatar())
                .setLastLoginIp(clientIp)
                .setLastLoginTime(LocalDateTime.now());

        userService.save(newUser);
        return authUtil.generateToken(newUser);
    }

    private void updateUserLoginInfo(Long userId, String clientIp) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, userId)
                .set(StringUtils.isNotBlank(clientIp), SysUser::getLastLoginIp, clientIp)
                .set(SysUser::getLastLoginTime, LocalDateTime.now());
        userService.update(null, updateWrapper);
    }

    @Override
    public String getAccessToken(String code) {
        String url = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                qqConfig.getAccessTokenUrl(),
                qqConfig.getAppId(),
                qqConfig.getAppKey(),
                code,
                qqConfig.getRedirectUri());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                return parseTokenResponse(EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get QQ access token: {}", e.getMessage(), e);
            throw new BusinessException("获取QQ访问令牌失败");
        }
    }

    private String parseTokenResponse(String response) {
        for (String pair : response.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2 && ACCESS_TOKEN_KEY.equals(kv[0])) {
                return kv[1];
            }
        }
        throw new BusinessException("无效的QQ令牌响应");
    }

    @Override
    public String getOpenid(String accessToken) {
        String url = qqConfig.getOpenidUrl() + "?access_token=" + accessToken;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String jsonp = EntityUtils.toString(response.getEntity());
                return parseOpenid(jsonp);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get QQ openid: {}", e.getMessage(), e);
            throw new BusinessException("获取 QQ OpenID 失败");
        }
    }

    private String parseOpenid(String jsonp) throws IOException {
        String json = jsonp.replace(CALLBACK_PREFIX, "").replace(CALLBACK_SUFFIX, "").trim();
        return objectMapper.readTree(json).get("openid").asText();
    }

    @Override
    public QQUserInfoResponse getUserInfo(String accessToken, String openid) {
        String url = String.format("%s?access_token=%s&oauth_consumer_key=%s&openid=%s",
                qqConfig.getUserInfoUrl(),
                accessToken,
                qqConfig.getAppId(),
                openid);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                validateResponse(response);
                String json = EntityUtils.toString(response.getEntity());
                QQUserInfoResponse userInfo = objectMapper.readValue(json, QQUserInfoResponse.class);
                userInfo.setOpenId(openid);
                return userInfo;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to get QQ user info: {}", e.getMessage(), e);
            throw new BusinessException("获取 QQ 用户信息失败");
        }
    }

    /**
     * 判断获取 QQ 用户信息接口的状态
     */
    private void validateResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HTTP_OK) {
            throw new IOException("HTTP 请求失败，状态码: " + statusCode);
        }
        if (response.getEntity() == null) {
            throw new IOException("响应体为空");
        }
    }
}
