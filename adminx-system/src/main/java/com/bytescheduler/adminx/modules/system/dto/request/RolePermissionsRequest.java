package com.bytescheduler.adminx.modules.system.dto.request;

import com.bytescheduler.adminx.modules.system.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionsRequest {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private List<SysMenu> menuTree;
    private List<Long> permissionKeys;
}
