package com.bytescheduler.adminx.modules.system.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/17
 */
@Data
public class UserResponse {

    @ApiModelProperty(value = "用户 ID")
    private Long userId;

    @ApiModelProperty(value = "部门 ID")
    private Long deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Integer gender;
}
