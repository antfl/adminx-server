package com.bytescheduler.adminx.modules.system.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.modules.system.dto.response.ThirdPartyUserInfo;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.SysThirdPartyAuth;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.mapper.SysThirdPartyAuthMapper;
import com.bytescheduler.adminx.modules.system.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/8/21
 */
@Service
@RequiredArgsConstructor
public class UnifiedThirdPartyLoginService {

    private final List<ThirdPartyLoginStrategy> strategies;
    private final SysThirdPartyAuthMapper thirdPartyAuthMapper;
    private final AuthUtil authUtil;

    @Transactional(rollbackFor = RuntimeException.class)
    public TokenResponse handleLogin(String provider, String code) {
        ThirdPartyLoginStrategy strategy = strategies.stream()
                .filter(s -> s.getProviderType().equals(provider))
                .findFirst()
                .orElseThrow(() -> new BusinessException("不支持的登录方式"));

        try {
            ThirdPartyUserInfo userInfo = strategy.getUserInfo(code);
            return processThirdPartyAuth(provider, userInfo);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception e) {
            throw new BusinessException("登录异常");
        }
    }

    private TokenResponse processThirdPartyAuth(String provider, ThirdPartyUserInfo userInfo) {
        String openId = userInfo.getOpenId();
        SysThirdPartyAuth authRecord = thirdPartyAuthMapper.selectOne(
                new LambdaQueryWrapper<SysThirdPartyAuth>()
                        .eq(SysThirdPartyAuth::getOpenId, openId)
        );

        // 已绑定用户直接登录
        if (authRecord != null && authRecord.getUserId() != null) {
            return authUtil.generateToken(new SysUser().setUserId(authRecord.getUserId()));
        }

        // 更新或创建三方记录
        if (authRecord == null) {
            SysThirdPartyAuth record = SysThirdPartyAuth.builder()
                    .openId(openId)
                    .provider(provider)
                    .bindTime(LocalDateTime.now())
                    .avatarUrl(userInfo.getAvatarUrl())
                    .nickname(userInfo.getNickname())
                    .build();

            thirdPartyAuthMapper.insert(record);
        } else {
            thirdPartyAuthMapper.update(null,
                    new LambdaUpdateWrapper<SysThirdPartyAuth>()
                            .eq(SysThirdPartyAuth::getOpenId, openId)
                            .set(SysThirdPartyAuth::getBindTime, LocalDateTime.now())
                            .set(SysThirdPartyAuth::getAvatarUrl, userInfo.getAvatarUrl())
                            .set(SysThirdPartyAuth::getNickname, userInfo.getNickname())
            );
        }

        return new TokenResponse(null, openId);
    }
}
