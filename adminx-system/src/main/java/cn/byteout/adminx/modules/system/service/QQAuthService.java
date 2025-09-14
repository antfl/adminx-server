package cn.byteout.adminx.modules.system.service;

import cn.byteout.adminx.modules.system.dto.response.QQUserInfoResponse;

/**
 * @author antfl
 * @since 2025/8/11
 */
public interface QQAuthService {

    String getAccessToken(String code);

    String getOpenid(String accessToken);

    QQUserInfoResponse getUserInfo(String accessToken, String openid);
}
