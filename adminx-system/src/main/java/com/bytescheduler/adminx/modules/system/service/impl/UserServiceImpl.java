package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.dto.request.UpdateUserRequest;
import com.bytescheduler.adminx.modules.system.dto.request.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.request.UserRoleRequest;
import com.bytescheduler.adminx.modules.system.dto.response.UserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserPermissionResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserResponse;
import com.bytescheduler.adminx.modules.system.entity.SysMenu;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.mapper.MenuMapper;
import com.bytescheduler.adminx.modules.system.mapper.RoleMenuMapper;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.mapper.UserRoleMapper;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import com.bytescheduler.adminx.modules.system.service.UserRoleService;
import com.bytescheduler.adminx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    private final SysUserMapper sysUserMapper;
    private final UserRoleService userRoleService;
    private final UserRoleMapper userRoleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final MenuMapper menuMapper;
    private final MenuService menuService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> updateUser(UpdateUserRequest params) {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, params.getUserId()));
        if (!Objects.equals(user.getUserId(), UserContext.getCurrentUserId())) {
            return Result.failed("无该操作权限");
        }

        if (StringUtils.isNotBlank(params.getUsername())) {
            Long usernameCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, params.getUsername())
                    .ne(SysUser::getUserId, params.getUserId()));

            if (usernameCount > 0) {
                return Result.failed("用户名已存在");
            }
        }

        if (StringUtils.isNotBlank(params.getNickname())) {
            Long nicknameCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getNickname, params.getNickname())
                    .ne(SysUser::getUserId, params.getUserId()));

            if (nicknameCount > 0) {
                return Result.failed("昵称已存在");
            }
        }

        if (CollectionUtils.isNotEmpty(params.getRoleIds())) {
            UserRoleRequest userRole = new UserRoleRequest();
            userRole.setUserId(params.getUserId());
            userRole.setRoleIds(params.getRoleIds());
            userRoleService.setUserRoles(userRole);
        }

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, params.getUserId());
        // 更新用户信息
        updateWrapper.set(StringUtils.isNotBlank(params.getUsername()), SysUser::getUsername, params.getUsername());
        updateWrapper.set(StringUtils.isNotBlank(params.getNickname()), SysUser::getNickname, params.getNickname());
        updateWrapper.set(params.getGender() != null, SysUser::getGender, params.getGender());
        updateWrapper.set(StringUtils.isNotBlank(params.getAvatar()), SysUser::getAvatar, params.getAvatar());

        sysUserMapper.update(null, updateWrapper);
        return Result.success();
    }

    @Override
    public Result<PageResult<UserResponse>> pageQuery(UserQueryRequest params) {
        Page<SysUser> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(params.getStatus() != null, SysUser::getStatus, params.getStatus())
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = this.page(page, wrapper);
        List<UserResponse> list = result.getRecords().stream().map(this::convertResponse).collect(Collectors.toList());

        return Result.success(PageResult.<UserResponse>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(list)
                .build());
    }

    @Override
    public Result<UserInfoResponse> getUserInfo() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserId, UserContext.getCurrentUserId());

        SysUser user = sysUserMapper.selectOne(wrapper);

        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(user.getUserId());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setGender(user.getGender());
        response.setUsername(user.getUsername());

        return Result.success(response);
    }

    @Override
    public Result<UserPermissionResponse> getPermissions() {
        Long userId = UserContext.getCurrentUserId();

        // 获取用户角色 ID 列表
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);

        Result<UserPermissionResponse> emptyList = Result.success(
                new UserPermissionResponse()
                        .setMenus(Collections.emptyList())
                        .setPermissions(Collections.emptySet())
        );

        if (CollectionUtils.isEmpty(roleIds)) {
            return emptyList;
        }

        // 获取菜单 ID 列表
        List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return emptyList;
        }

        // 获取菜单列表
        List<SysMenu> menus = menuMapper.selectMenusByIds(menuIds);
        List<SysMenu> menuTree = menuService.buildMenuTree(menus);

        // 获取按钮权限
        List<String> permissionList = menuMapper.selectPermissionsByIds(menuIds);
        Set<String> permissions = new HashSet<>(permissionList);

        return Result.success(
                new UserPermissionResponse()
                        .setPermissions(permissions)
                        .setMenus(menuTree)
        );
    }

    private UserResponse convertResponse(SysUser user) {
        UserResponse response = new UserResponse();

        response.setUserId(user.getUserId());
        response.setNickname(user.getNickname());
        response.setUsername(user.getUsername());
        response.setAvatar(user.getAvatar());
        response.setGender(user.getGender());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());
        response.setUpdateTime(user.getUpdateTime());
        return response;
    }
}
