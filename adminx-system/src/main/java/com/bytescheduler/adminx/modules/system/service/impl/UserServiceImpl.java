package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.dto.request.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.response.UserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserResponse;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    private final SysUserMapper sysUserMapper;

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
        response.setUsername(user.getUsername());
        response.setAvatar(user.getAvatar());

        return Result.success(response);
    }

   private UserResponse convertResponse(SysUser user) {
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
   }
}
