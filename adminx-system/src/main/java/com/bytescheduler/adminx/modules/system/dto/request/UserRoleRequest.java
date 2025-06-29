package com.bytescheduler.adminx.modules.system.dto.request;

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
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "角色ID列表不能为空")
    private List<Long> roleIds;
}
