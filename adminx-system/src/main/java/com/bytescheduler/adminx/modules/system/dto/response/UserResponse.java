package com.bytescheduler.adminx.modules.system.dto.response;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/17
 */
@Data
public class UserResponse {
    private Long userId;
    private Long deptId;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
}
