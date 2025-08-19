package com.bytescheduler.adminx.modules.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户登录返回的权限标识
 *
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Data
@AllArgsConstructor
public class TokenResponse {

    /**
     * 用户 TOKEN
     */
    private String token;

    /**
     * 用户标识
     */
    private String openId;
}
