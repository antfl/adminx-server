package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

    @NotBlank(message = "captchaId cannot be blank")
    @Pattern(
            regexp = "^[a-fA-F0-9\\-]{36}$",
            message = "captchaId must be a valid 36-character UUID"
    )
    private String captchaId;

    @NotBlank(message = "code cannot be blank")
    private String code;
}
