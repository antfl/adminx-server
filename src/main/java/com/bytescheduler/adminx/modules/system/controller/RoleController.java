package com.bytescheduler.adminx.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.RoleRequest;
import com.bytescheduler.adminx.modules.system.dto.RoleQueryRequest;
import com.bytescheduler.adminx.modules.system.entity.SysRole;
import com.bytescheduler.adminx.modules.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@RestController
@RequestMapping("/system/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // 角色分页查询
    @GetMapping
    public Result<Page<SysRole>> listRoles(RoleQueryRequest queryRequest) {
        return Result.success(roleService.listRoles(queryRequest));
    }

    // 角色详情
    @GetMapping("/{roleId}")
    public Result<SysRole> getRole(@PathVariable Long roleId) {
        SysRole role = roleService.getById(roleId);
        if (role == null || role.getIsDeleted() == 1) {
            return Result.failed("角色不存在");
        }
        return Result.success(role);
    }

    // 创建角色
    @PostMapping
    public Result<String> createRole(@Valid @RequestBody RoleRequest dto) {
        roleService.createRole(dto);
        return Result.success("创建成功");
    }

    // 更新角色
    @PutMapping("/{roleId}")
    public Result<String> updateRole(
            @PathVariable Long roleId,
            @Valid @RequestBody RoleRequest dto) {
        roleService.updateRole(roleId, dto);
        return Result.success("更新成功");
    }

    // 删除角色（逻辑删除）
    @DeleteMapping("/{roleId}")
    public Result<String> deleteRole(@PathVariable Long roleId) {
        SysRole role = new SysRole();
        role.setRoleId(roleId);
        role.setIsDeleted(1);
        roleService.updateById(role);
        return Result.success("删除成功");
    }

    // 修改角色状态
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
}
