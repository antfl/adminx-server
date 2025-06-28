package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "用户名必填")
    @Size(min = 2, max = 20, message = "用户名 2 到 20 个字符")
    private String nickname;

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

    private String code;
}
