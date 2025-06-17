package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.modules.system.dto.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.UserResponse;
import com.bytescheduler.adminx.modules.system.entity.User;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;

    @Override
    public Page<UserResponse> listUser(UserQueryRequest queryRequest) {
        Page<User> userPage = new Page<>(queryRequest.getPage(), queryRequest.getSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIsDeleted, 0)
                .eq(queryRequest.getStatus() != null,
                        User::getStatus, queryRequest.getStatus());

        sysUserMapper.selectPage(userPage, wrapper);

        return (Page<UserResponse>) userPage.convert(user -> {
            UserResponse response = new UserResponse();
            response.setUserId(user.getUserId());
            response.setDeptId(user.getDeptId());
            response.setUsername(user.getUsername());
            response.setNickname(user.getNickname());
            response.setEmail(user.getEmail());
            response.setAvatar(user.getAvatar());
            response.setPhone(user.getPhone());
            response.setGender(user.getGender());
            return response;
        });
    }
}
