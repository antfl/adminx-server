package com.bytescheduler.adminx.modules.system.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/7/6
 */
@Data
public class UpdateUserRequest {

    @ApiModelProperty(value = "用户 ID")
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    @ApiModelProperty(value = "角色 ID 数组")
    private List<Long> roleIds;

    @ApiModelProperty(value = "昵称")
    @Pattern(regexp = "^\\S+$", message = "昵称不能包含空格")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "用户名必须为5-20位字母或数字，不能包含空格或特殊字符")
    private String username;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Integer gender;
}
