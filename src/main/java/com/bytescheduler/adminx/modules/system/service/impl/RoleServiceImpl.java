package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.SqlEscapeUtil;
import com.bytescheduler.adminx.modules.system.dto.RolePermissionsRequest;
import com.bytescheduler.adminx.modules.system.dto.RoleRequest;
import com.bytescheduler.adminx.modules.system.dto.RoleQueryRequest;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import com.bytescheduler.adminx.modules.system.entity.SysRole;
import com.bytescheduler.adminx.modules.system.mapper.MenuMapper;
import com.bytescheduler.adminx.modules.system.mapper.RoleMapper;
import com.bytescheduler.adminx.modules.system.mapper.RoleMenuMapper;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import com.bytescheduler.adminx.modules.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final MenuMapper menuMapper;

    private final MenuService menuService;

    public RoleServiceImpl(RoleMapper roleMapper, RoleMenuMapper roleMenuMapper, MenuMapper menuMapper, MenuService menuService) {
        this.roleMapper = roleMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.menuMapper = menuMapper;
        this.menuService = menuService;
    }

    @Override
    public Page<SysRole> listRoles(RoleQueryRequest queryRequest) {
        Page<SysRole> page = new Page<>(queryRequest.getPage(), queryRequest.getSize());
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysRole::getIsDeleted, 0)
                .like(StringUtils.isNotBlank(queryRequest.getRoleName()),
                        SysRole::getRoleName, SqlEscapeUtil.escapeLike(queryRequest.getRoleName()))
                .like(StringUtils.isNotBlank(queryRequest.getRoleKey()),
                        SysRole::getRoleKey, SqlEscapeUtil.escapeLike(queryRequest.getRoleKey()))
                .eq(queryRequest.getStatus() != null,
                        SysRole::getStatus, queryRequest.getStatus());

        return roleMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional
    public void createRole(RoleRequest dto) {
        // 检查角色标识是否唯一
        if (lambdaQuery().eq(SysRole::getRoleKey, dto.getRoleKey()).count() > 0) {
            throw new BusinessException("角色标识已存在");
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        save(role);
    }

    @Override
    @Transactional
    public void updateRole(Long roleId, RoleRequest dto) {
        SysRole role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查角色标识是否唯一（排除自身）
        if (lambdaQuery()
                .eq(SysRole::getRoleKey, dto.getRoleKey())
                .ne(SysRole::getRoleId, roleId)
                .count() > 0) {
            throw new BusinessException("角色标识已存在");
        }

        BeanUtils.copyProperties(dto, role);
        role.setRoleId(roleId);
        updateById(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<RolePermissionsRequest> getRolePermissions(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            return Result.failed("角色不存在");
        }

        List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleId(roleId);
        if (menuIds.isEmpty()) {
            return Result.success(new RolePermissionsRequest(roleId, role.getRoleName(),
                    role.getRoleKey(), Collections.emptyList(), Collections.emptyList()));
        }

        List<Menu> menus = menuMapper.selectBatchIds(menuIds);

        List<Menu> menuTree = menuService.getMenuTree();

        List<Long> permissionKeys = menus.stream()
                .map(Menu::getId)
                .distinct()
                .collect(Collectors.toList());

        RolePermissionsRequest dto = new RolePermissionsRequest();
        dto.setRoleId(roleId);
        dto.setRoleName(role.getRoleName());
        dto.setRoleKey(role.getRoleKey());
        dto.setMenuTree(menuTree);
        dto.setPermissionKeys(permissionKeys);
        return Result.success(dto);
    }
}
