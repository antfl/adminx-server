package com.bytescheduler.adminx.modules.system.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/16
 */
@Data
public class UserRoleRequest {

    @ApiModelProperty(value = "用户 ID")
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    @ApiModelProperty(value = "角色 ID 数组")
    @NotEmpty(message = "角色 ID 列表不能为空")
    private List<Long> roleIds;
}
