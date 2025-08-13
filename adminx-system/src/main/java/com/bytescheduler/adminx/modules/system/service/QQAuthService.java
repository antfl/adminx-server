package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.response.QQUserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author byte-scheduler
 * @since 2025/8/11
 */
public interface QQAuthService {

    TokenResponse qqLogin(String code, HttpServletRequest request);

    String getAccessToken(String code);

    String getOpenid(String accessToken);

    QQUserInfoResponse getUserInfo(String accessToken, String openid);
}
