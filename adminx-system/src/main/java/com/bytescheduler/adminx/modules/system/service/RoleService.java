package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.RolePermissionsRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RoleQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RoleRequest;
import com.bytescheduler.adminx.modules.system.entity.SysRole;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
public interface RoleService extends IService<SysRole> {

    Page<SysRole> listRoles(RoleQueryRequest queryRequest);

    void createRole(RoleRequest dto);

    void updateRole(Long roleId, RoleRequest dto);

    Result<RolePermissionsRequest> getRolePermissions(Long roleId);
}
