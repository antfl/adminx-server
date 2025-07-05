package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.RoleMenuRequest;
import com.bytescheduler.adminx.modules.system.entity.SysRoleMenu;
import com.bytescheduler.adminx.modules.system.mapper.RoleMenuMapper;
import com.bytescheduler.adminx.modules.system.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author byte-scheduler
 * @since 2025/6/15
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, SysRoleMenu> implements RoleMenuService {

    private final RoleMenuMapper roleMenuMapper;

    public RoleMenuServiceImpl(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> setRoleMenus(RoleMenuRequest dto) {
        // 删除角色现有菜单权限
        roleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().lambda().in(SysRoleMenu::getRoleId, dto.getRoleId()));

        // 批量插入新菜单权限
        if (!dto.getMenuIds().isEmpty()) {
            roleMenuMapper.batchInsert(dto.getRoleId(), dto.getMenuIds());
        }

        return Result.success("角色菜单权限设置成功");
    }
}
