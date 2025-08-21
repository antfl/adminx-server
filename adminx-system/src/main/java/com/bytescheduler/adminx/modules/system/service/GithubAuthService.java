package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.response.GithubUserInfo;

/**
 * @author byte-scheduler
 * @since 2025/8/16
 */
public interface GithubAuthService {

    String getAccessToken(String code);

    GithubUserInfo getUserInfo(String accessToken);
}
