package com.bytescheduler.adminx.modules.system.strategy;

import com.bytescheduler.adminx.modules.system.dto.response.ThirdPartyUserInfo;

/**
 *
 * @author byte-scheduler
 * @since 2025/8/21
 */
public interface  ThirdPartyLoginStrategy {

    ThirdPartyUserInfo getUserInfo(String code) throws Exception;

    String getProviderType();
}
