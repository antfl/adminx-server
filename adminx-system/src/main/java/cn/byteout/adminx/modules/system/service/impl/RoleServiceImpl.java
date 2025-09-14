package cn.byteout.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.common.exception.BusinessException;
import cn.byteout.adminx.common.utils.crypto.SqlEscapeUtil;
import cn.byteout.adminx.modules.system.dto.request.RolePermissionsRequest;
import cn.byteout.adminx.modules.system.dto.request.RoleQueryRequest;
import cn.byteout.adminx.modules.system.dto.request.RoleRequest;
import cn.byteout.adminx.modules.system.entity.SysMenu;
import cn.byteout.adminx.modules.system.entity.SysRole;
import cn.byteout.adminx.modules.system.mapper.MenuMapper;
import cn.byteout.adminx.modules.system.mapper.RoleMapper;
import cn.byteout.adminx.modules.system.mapper.RoleMenuMapper;
import cn.byteout.adminx.modules.system.service.MenuService;
import cn.byteout.adminx.modules.system.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author antfl
 * @since 2025/6/8
 */
@AllArgsConstructor
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final MenuMapper menuMapper;
    private final MenuService menuService;

    @Override
    public Result<PageResult<SysRole>> pageQuery(RoleQueryRequest params) {
        Page<SysRole> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getRoleName()), SysRole::getRoleName, SqlEscapeUtil.escapeLike(params.getRoleName()))
                .like(StringUtils.isNotBlank(params.getRoleKey()), SysRole::getRoleKey, SqlEscapeUtil.escapeLike(params.getRoleKey()))
                .eq(params.getStatus() != null, SysRole::getStatus, params.getStatus())
                .orderByDesc(SysRole::getCreateTime);

        Page<SysRole> result = this.page(page, wrapper);

        return Result.success(PageResult.<SysRole>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(result.getRecords())
                .build());
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

        List<SysMenu> menus = menuMapper.selectBatchIds(menuIds);

        List<SysMenu> menuTree = menuService.getMenuTree();

        List<Long> permissionKeys = menus.stream()
                .map(SysMenu::getId)
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
