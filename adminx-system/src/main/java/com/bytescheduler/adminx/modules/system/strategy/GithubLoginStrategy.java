package com.bytescheduler.adminx.modules.system.strategy;

import com.bytescheduler.adminx.modules.system.dto.response.GithubUserInfo;
import com.bytescheduler.adminx.modules.system.dto.response.ThirdPartyUserInfo;
import com.bytescheduler.adminx.modules.system.enums.ThirdPartyProvider;
import com.bytescheduler.adminx.modules.system.service.GithubAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author byte-scheduler
 * @since 2025/8/21
 */
@RequiredArgsConstructor
@Service
public class GithubLoginStrategy implements ThirdPartyLoginStrategy {

    private final GithubAuthService githubAuthService;

    @Override
    public ThirdPartyUserInfo getUserInfo(String code) throws Exception {
        String accessToken = githubAuthService.getAccessToken(code);
        GithubUserInfo userInfo = githubAuthService.getUserInfo(accessToken);

        return new ThirdPartyUserInfo(
                userInfo.getId(),
                userInfo.getAvatarUrl(),
                userInfo.getName()
        );
    }

    @Override
    public String getProviderType() {
        return ThirdPartyProvider.GITHUB.getProvider();
    }
}
