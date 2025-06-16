package com.bytescheduler.adminx.modules.system.service.impl;

import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.UserRoleRequest;
import com.bytescheduler.adminx.modules.system.mapper.UserRoleMapper;
import com.bytescheduler.adminx.modules.system.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author byte-scheduler
 * @since 2025/6/16
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setUserRoles(UserRoleRequest dto) {
        userRoleMapper.deleteByUserId(dto.getUserId());

        if (!dto.getRoleIds().isEmpty()) {
            userRoleMapper.batchInsert(dto.getUserId(), dto.getRoleIds());
        }

        return Result.success("用户角色设置成功");
    }
}
