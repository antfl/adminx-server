package com.bytescheduler.adminx.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.request.RoleMenuRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RolePermissionsRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RoleQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RoleRequest;
import com.bytescheduler.adminx.modules.system.entity.SysRole;
import com.bytescheduler.adminx.modules.system.service.RoleMenuService;
import com.bytescheduler.adminx.modules.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Api(tags = "角色管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMenuService roleMenuService;

    @ApiOperation("角色分页查询")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "角色分页查询")
    @GetMapping
    public Result<Page<SysRole>> listRoles(RoleQueryRequest queryRequest) {
        return Result.success(roleService.listRoles(queryRequest));
    }

    @ApiOperation("角色详情")
    @GetMapping("/{roleId}")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "角色详情")
    public Result<SysRole> getRole(@PathVariable Long roleId) {
        SysRole role = roleService.getById(roleId);
        if (role == null || role.getIsDeleted() == 1) {
            return Result.failed("角色不存在");
        }
        return Result.success(role);
    }

    @ApiOperation("创建角色")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "创建角色")
    @PostMapping
    public Result<String> createRole(@Valid @RequestBody RoleRequest dto) {
        roleService.createRole(dto);
        return Result.success("创建成功");
    }

    @ApiOperation("更新角色")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "更新角色")
    @PutMapping("/{roleId}")
    public Result<String> updateRole(
            @PathVariable Long roleId,
            @Valid @RequestBody RoleRequest dto) {
        roleService.updateRole(roleId, dto);
        return Result.success("更新成功");
    }

    @ApiOperation("删除角色")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "删除角色")
    @DeleteMapping("/{roleId}")
    public Result<String> deleteRole(@PathVariable Long roleId) {
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setIsDeleted(1);
        roleService.updateById(role);
        return Result.success("删除成功");
    }

    @ApiOperation("修改角色状态")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "修改角色状态")
    @PatchMapping("/{roleId}/status")
    public Result<String> updateStatus(
            @PathVariable Long roleId,
            @RequestParam Integer status) {
        if (!Arrays.asList(0, 1).contains(status)) {
            return Result.failed("状态值无效");
        }

        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setStatus(status);
        roleService.updateById(role);
        return Result.success("状态更新成功");
    }

    @ApiOperation("获取角色权限")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "获取角色权限")
    @GetMapping("/permission/{roleId}")
    public Result<RolePermissionsRequest> getRolePermissions(@PathVariable Long roleId) {
        return roleService.getRolePermissions(roleId);
    }

    @ApiOperation("编辑角色菜单权限")
    @Log(module = "角色管理", type = OperationType.SELECT, value = "编辑角色菜单权限")
    @PostMapping("/setRoleMenus")
    public Result<String> setRoleMenus(@Valid @RequestBody RoleMenuRequest dto) {
        return roleMenuService.setRoleMenus(dto);
    }
}
