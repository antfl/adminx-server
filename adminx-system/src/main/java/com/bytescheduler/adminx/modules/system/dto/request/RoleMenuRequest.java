package com.bytescheduler.adminx.modules.system.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/15
 */
@Data
public class RoleMenuRequest {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotEmpty(message = "菜单ID列表不能为空")
    private List<Long> menuIds;
}
