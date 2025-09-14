package cn.byteout.adminx.modules.system.strategy;

import cn.byteout.adminx.modules.system.dto.response.QQUserInfoResponse;
import cn.byteout.adminx.modules.system.dto.response.ThirdPartyUserInfo;
import cn.byteout.adminx.modules.system.enums.ThirdPartyProvider;
import cn.byteout.adminx.modules.system.service.QQAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author antfl
 * @since 2025/8/21
 */
@RequiredArgsConstructor
@Service
public class QQLoginStrategy implements ThirdPartyLoginStrategy {

    private final QQAuthService qqAuthService;

    @Override
    public ThirdPartyUserInfo getUserInfo(String code) throws Exception {
        String accessToken = qqAuthService.getAccessToken(code);
        String openId = qqAuthService.getOpenid(accessToken);
        QQUserInfoResponse userInfo = qqAuthService.getUserInfo(accessToken, openId);

        return new ThirdPartyUserInfo(
                openId,
                userInfo.getQqAvatar(),
                userInfo.getNickname()
        );
    }

    @Override
    public String getProviderType() {
        return ThirdPartyProvider.QQ.getProvider();
    }
}
