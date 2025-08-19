package com.bytescheduler.adminx.modules.system.dto.request;

import lombok.Data;

/**
 * 绑定三方账号
 *
 * @author byte-scheduler
 * @since 2025/8/17
 */
@Data
public class BindingRequest {

    /**
     * 三方平台标识
     */
    private String provider;

    /**
     * 授权码
     */
    private String authCode;
}
