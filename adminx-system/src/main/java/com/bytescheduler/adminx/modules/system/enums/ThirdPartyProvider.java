package com.bytescheduler.adminx.modules.system.enums;

import lombok.Getter;

/**
 * 用户所有三方账户登录方式
 *
 * @author byte-scheduler
 * @since 2025/8/17
 */
@Getter
public enum ThirdPartyProvider {

    GITHUB("GITHUB", "Github 登录"),
    WECHAT("WECHAT", "微信登录"),
    QQ("QQ", "QQ 登录");

    private final String provider;
    private final String description;

    ThirdPartyProvider(String provider, String description) {
        this.provider = provider;
        this.description = description;
    }
}
