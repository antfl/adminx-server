package cn.byteout.adminx.modules.system.strategy;

import cn.byteout.adminx.modules.system.dto.response.ThirdPartyUserInfo;

/**
 *
 * @author antfl
 * @since 2025/8/21
 */
public interface  ThirdPartyLoginStrategy {

    ThirdPartyUserInfo getUserInfo(String code) throws Exception;

    String getProviderType();
}
