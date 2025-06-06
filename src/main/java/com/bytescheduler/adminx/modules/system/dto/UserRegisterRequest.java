package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterRequest extends UserLoginRequest {
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    private String email;
    private String phone;
}
