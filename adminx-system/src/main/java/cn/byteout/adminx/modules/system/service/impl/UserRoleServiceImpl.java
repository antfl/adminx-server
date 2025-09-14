package cn.byteout.adminx.modules.system.service.impl;

import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.UserRoleRequest;
import cn.byteout.adminx.modules.system.mapper.UserRoleMapper;
import cn.byteout.adminx.modules.system.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author antfl
 * @since 2025/6/16
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> setUserRoles(UserRoleRequest dto) {
        userRoleMapper.deleteByUserId(dto.getUserId());

        if (!dto.getRoleIds().isEmpty()) {
            userRoleMapper.batchInsert(dto.getUserId(), dto.getRoleIds());
        }

        return Result.success("用户角色设置成功");
    }
}
