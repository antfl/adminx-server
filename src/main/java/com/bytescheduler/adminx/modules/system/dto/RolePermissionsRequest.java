package com.bytescheduler.adminx.modules.system.dto;

import com.bytescheduler.adminx.modules.system.entity.Menu;
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
    private List<Menu> menuTree;
    private List<Long> permissionKeys;
}
