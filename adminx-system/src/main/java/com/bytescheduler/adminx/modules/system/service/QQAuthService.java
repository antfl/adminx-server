package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.response.QQUserInfoResponse;

/**
 * @author byte-scheduler
 * @since 2025/8/11
 */
public interface QQAuthService {

    String getAccessToken(String code);

    String getOpenid(String accessToken);

    QQUserInfoResponse getUserInfo(String accessToken, String openid);
}
