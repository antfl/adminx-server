package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Email(message = "Email should be valid")
    private String email;
}
