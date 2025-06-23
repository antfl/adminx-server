package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/23
 */
@Data
public class UserInfoResponse {
    private Long userId;
    private String username;
    private String avatar;
}
