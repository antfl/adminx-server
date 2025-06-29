package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.dto.request.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.response.UserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserResponse;
import com.bytescheduler.adminx.modules.system.entity.User;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, User> implements UserService {

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

    @Override
    public Result<UserInfoResponse> getUserInfo() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, getCurrentUserId());
        User user = sysUserMapper.selectOne(wrapper);
        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setAvatar(user.getAvatar());
        return Result.success(response);
    }

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}
