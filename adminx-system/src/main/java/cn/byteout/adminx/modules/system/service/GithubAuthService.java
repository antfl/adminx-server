package cn.byteout.adminx.modules.system.service;

import cn.byteout.adminx.modules.system.dto.response.GithubUserInfo;

/**
 * @author antfl
 * @since 2025/8/16
 */
public interface GithubAuthService {

    String getAccessToken(String code);

    GithubUserInfo getUserInfo(String accessToken);
}
