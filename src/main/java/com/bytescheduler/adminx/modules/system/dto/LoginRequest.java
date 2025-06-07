package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
