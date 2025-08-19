package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.context.UserContextHolder;
import com.bytescheduler.adminx.modules.system.dto.request.BindingInfo;
import com.bytescheduler.adminx.modules.system.dto.request.BindingRequest;
import com.bytescheduler.adminx.modules.system.dto.request.CreateUser;
import com.bytescheduler.adminx.modules.system.dto.response.GithubUserInfo;
import com.bytescheduler.adminx.modules.system.dto.response.QQUserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.SysThirdPartyAuth;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.enums.ThirdPartyProvider;
import com.bytescheduler.adminx.modules.system.mapper.SysThirdPartyAuthMapper;
import com.bytescheduler.adminx.modules.system.service.GithubAuthService;
import com.bytescheduler.adminx.modules.system.service.QQAuthService;
import com.bytescheduler.adminx.modules.system.service.ThirdPartyAuthService;
import com.bytescheduler.adminx.modules.system.service.UserService;
import com.bytescheduler.adminx.modules.system.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author byte-scheduler
 * @since 2025/8/17
 */
@RequiredArgsConstructor
@Service
public class ThirdPartyAuthServiceImpl extends ServiceImpl<SysThirdPartyAuthMapper, SysThirdPartyAuth> implements ThirdPartyAuthService {

    private final HttpRequestIpResolver ipResolver;
    private final PasswordEncoder passwordEncoder;
    private final SysThirdPartyAuthMapper authMapper;
    private final UserService userService;
    private final AuthUtil authUtil;
    private final GithubAuthService githubAuthService;
    private final QQAuthService qqAuthService;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void bindAccount(BindingRequest params) {
        Long userId = UserContextHolder.get();
        SysThirdPartyAuth thirdPartyAuth = authMapper.selectOne(
                new LambdaUpdateWrapper<SysThirdPartyAuth>()
                        .eq(SysThirdPartyAuth::getProvider, params.getProvider())
                        .eq(SysThirdPartyAuth::getUserId, userId)
        );

        if (thirdPartyAuth != null) {
            throw new BusinessException("该账号已被绑定");
        }

        if (params.getProvider().equals(ThirdPartyProvider.GITHUB.getProvider())) {
            String accessToken = githubAuthService.getAccessToken(params.getAuthCode());
            GithubUserInfo userInfo = githubAuthService.getUserInfo(accessToken);

            SysThirdPartyAuth partyAuth = authMapper.selectOne(
                    new LambdaQueryWrapper<SysThirdPartyAuth>()
                            .eq(SysThirdPartyAuth::getOpenId, userInfo.getId())
            );
            if (partyAuth != null) {
                throw new BusinessException("该账号已被绑定");
            }

            SysThirdPartyAuth sysThirdPartyAuth = new SysThirdPartyAuth();
            sysThirdPartyAuth.setOpenId(userInfo.getId());
            sysThirdPartyAuth.setProvider(ThirdPartyProvider.GITHUB.getProvider());
            sysThirdPartyAuth.setBindTime(LocalDateTime.now());
            sysThirdPartyAuth.setAvatarUrl(userInfo.getAvatarUrl());
            sysThirdPartyAuth.setNickname(userInfo.getName());
            sysThirdPartyAuth.setUserId(userId);
            baseMapper.insert(sysThirdPartyAuth);
            return;
        }

        if (params.getProvider().equals(ThirdPartyProvider.QQ.getProvider())) {
            String accessToken = qqAuthService.getAccessToken(params.getAuthCode());
            String openid = qqAuthService.getOpenid(accessToken);
            QQUserInfoResponse userInfo = qqAuthService.getUserInfo(accessToken, openid);

            SysThirdPartyAuth partyAuth = authMapper.selectOne(
                    new LambdaQueryWrapper<SysThirdPartyAuth>()
                            .eq(SysThirdPartyAuth::getOpenId, userInfo.getOpenId())
            );
            if (partyAuth != null) {
                throw new BusinessException("该账号已被绑定");
            }

            SysThirdPartyAuth sysThirdPartyAuth = new SysThirdPartyAuth();
            sysThirdPartyAuth.setOpenId(userInfo.getOpenId());
            sysThirdPartyAuth.setProvider(ThirdPartyProvider.QQ.getProvider());
            sysThirdPartyAuth.setBindTime(LocalDateTime.now());
            sysThirdPartyAuth.setAvatarUrl(userInfo.getQqAvatar());
            sysThirdPartyAuth.setNickname(userInfo.getNickname());
            sysThirdPartyAuth.setUserId(userId);
            baseMapper.insert(sysThirdPartyAuth);
        }
    }

    @Override
    public List<BindingInfo> bindList() {
        Long userId = UserContextHolder.get();
        if (userId == null) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(
                        new LambdaQueryWrapper<SysThirdPartyAuth>()
                                .select(SysThirdPartyAuth::getId,
                                        SysThirdPartyAuth::getNickname,
                                        SysThirdPartyAuth::getAvatarUrl,
                                        SysThirdPartyAuth::getProvider,
                                        SysThirdPartyAuth::getBindTime)
                                .eq(SysThirdPartyAuth::getUserId, userId)
                ).stream()
                .map(item -> BindingInfo.builder()
                        .id(item.getId())
                        .nickname(item.getNickname())
                        .avatar(item.getAvatarUrl())
                        .provider(item.getProvider())
                        .bindTime(item.getBindTime())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 创建三方账号登录的用户
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public TokenResponse createUser(CreateUser params, HttpServletRequest request) {
        String openId = params.getOpenId();
        SysThirdPartyAuth thirdPartyAuth = authMapper.selectOne(new LambdaQueryWrapper<SysThirdPartyAuth>().eq(SysThirdPartyAuth::getOpenId, openId));
        if (thirdPartyAuth == null) {
            throw new BusinessException("无效 openId");
        }

        if (!Objects.equals(params.getPassword(), params.getConfirmPassword())) {
            throw new BusinessException("确认密码与设置的密码不一致");
        }

        if (userService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, params.getUsername())) > 0) {
            throw new BusinessException("用户已存在");
        }

        SysUser user = new SysUser();
        user.setAvatar(thirdPartyAuth.getAvatarUrl());
        user.setNickname(thirdPartyAuth.getNickname());
        user.setUsername(params.getUsername());

        // 设置加密密码
        String userPassword = passwordEncoder.encode(params.getPassword());
        user.setPassword(userPassword);

        // 最近登录时间
        user.setLastLoginTime(LocalDateTime.now());

        // 最近登录 IP
        String ip = ipResolver.resolve(request);
        user.setLastLoginIp(ip);

        // 保存用户
        userService.save(user);

        // 设置用户三方账号用户 ID
        authMapper.update(null, new LambdaUpdateWrapper<SysThirdPartyAuth>()
                .eq(SysThirdPartyAuth::getOpenId, openId)
                .set(SysThirdPartyAuth::getUserId, user.getUserId())
        );

        return authUtil.generateToken(user);
    }
}
