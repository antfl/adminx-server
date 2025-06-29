package com.bytescheduler.adminx.modules.system.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "密码必填")
    private String password;

    @NotBlank(message = "邮箱必填")
    @Email(message = "请输入有效邮箱")
    private String email;

    @Pattern(
            regexp = "^$|^[a-fA-F0-9\\-]{36}$",
            message = "captchaId must be empty or a valid 36-character UUID"
    )
    private String captchaId;

    @NotBlank(message = "验证码必填")
    private String code;
}
