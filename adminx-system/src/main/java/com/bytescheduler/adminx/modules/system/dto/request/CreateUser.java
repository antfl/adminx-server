package com.bytescheduler.adminx.modules.system.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 根据三方账号注册时完善用户信息
 *
 * @author byte-scheduler
 * @since 2025/8/17
 */
@Data
public class CreateUser {

    @ApiModelProperty(value = "用户名")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "用户名必须为5-20位字母或数字，不能包含空格或特殊字符")
    private String username;

    @NotBlank(message = "密码必填")
    @Size(min = 6, message = "密码长度至少为6位")
    @Size(max = 32, message = "密码长度大于了32位")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "密码必须包含小写字母、大写字母、数字和特殊符号"
    )
    private String password;

    @NotBlank(message = "确认密码必填")
    private String confirmPassword;

    @NotBlank(message = "用户 openId")
    private String openId;
}
