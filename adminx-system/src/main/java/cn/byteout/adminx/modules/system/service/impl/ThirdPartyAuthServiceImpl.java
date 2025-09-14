package cn.byteout.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.byteout.adminx.common.exception.BusinessException;
import cn.byteout.adminx.common.utils.http.HttpRequestIpResolver;
import cn.byteout.adminx.context.UserContextHolder;
import cn.byteout.adminx.modules.system.dto.request.BindingInfo;
import cn.byteout.adminx.modules.system.dto.request.BindingRequest;
import cn.byteout.adminx.modules.system.dto.request.CreateUser;
import cn.byteout.adminx.modules.system.dto.response.GithubUserInfo;
import cn.byteout.adminx.modules.system.dto.response.QQUserInfoResponse;
import cn.byteout.adminx.modules.system.dto.response.TokenResponse;
import cn.byteout.adminx.modules.system.entity.SysThirdPartyAuth;
import cn.byteout.adminx.modules.system.entity.SysUser;
import cn.byteout.adminx.modules.system.enums.ThirdPartyProvider;
import cn.byteout.adminx.modules.system.mapper.SysThirdPartyAuthMapper;
import cn.byteout.adminx.modules.system.service.GithubAuthService;
import cn.byteout.adminx.modules.system.service.QQAuthService;
import cn.byteout.adminx.modules.system.service.ThirdPartyAuthService;
import cn.byteout.adminx.modules.system.service.UserService;
import cn.byteout.adminx.modules.system.utils.AuthUtil;
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
 * @author antfl
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
        final Long userId = UserContextHolder.get();
        final String provider = params.getProvider();

        // 检查当前用户是否已绑定该平台账号
        checkUserBinding(userId, provider);

        // 根据不同的第三方平台处理绑定逻辑
        if (ThirdPartyProvider.GITHUB.is(provider)) {
            bindGithubAccount(userId, params.getAuthCode());
        } else if (ThirdPartyProvider.QQ.is(provider)) {
            bindQQAccount(userId, params.getAuthCode());
        } else {
            throw new BusinessException("不支持的第三方登录类型");
        }
    }

    /**
     * 判断当前三方账号是否被绑定
     */
    private void checkUserBinding(Long userId, String provider) {
        boolean exists = authMapper.exists(
                new LambdaQueryWrapper<SysThirdPartyAuth>()
                        .eq(SysThirdPartyAuth::getUserId, userId)
                        .eq(SysThirdPartyAuth::getProvider, provider)
        );
        if (exists) {
            throw new BusinessException("该平台账号已被绑定");
        }
    }

    /**
     * 绑定 GitHub 账号
     */
    private void bindGithubAccount(Long userId, String authCode) {
        String accessToken = githubAuthService.getAccessToken(authCode);
        GithubUserInfo userInfo = githubAuthService.getUserInfo(accessToken);

        checkOpenIdBinding(userInfo.getId());
        createThirdPartyAuth(userId,
                ThirdPartyProvider.GITHUB.getProvider(),
                userInfo.getId(),
                userInfo.getAvatarUrl(),
                userInfo.getName());
    }

    /**
     * 绑定 QQ 账号
     */
    private void bindQQAccount(Long userId, String authCode) {
        String accessToken = qqAuthService.getAccessToken(authCode);
        String openid = qqAuthService.getOpenid(accessToken);
        QQUserInfoResponse userInfo = qqAuthService.getUserInfo(accessToken, openid);

        checkOpenIdBinding(userInfo.getOpenId());
        createThirdPartyAuth(userId,
                ThirdPartyProvider.QQ.getProvider(),
                userInfo.getOpenId(),
                userInfo.getQqAvatar(),
                userInfo.getNickname());
    }

    private void checkOpenIdBinding(String openId) {
        boolean exists = authMapper.exists(
                new LambdaQueryWrapper<SysThirdPartyAuth>()
                        .eq(SysThirdPartyAuth::getOpenId, openId)
        );
        if (exists) {
            throw new BusinessException("该第三方账号已被其他用户绑定");
        }
    }

    /**
     * 创建用户绑定的三方账号
     */
    private void createThirdPartyAuth(Long userId,
                                      String provider,
                                      String openId,
                                      String avatarUrl,
                                      String nickname) {
        SysThirdPartyAuth auth = SysThirdPartyAuth.builder()
                .userId(userId)
                .provider(provider)
                .openId(openId)
                .avatarUrl(avatarUrl)
                .nickname(nickname)
                .bindTime(LocalDateTime.now())
                .build();

        baseMapper.insert(auth);
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
