package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.UpdateUserRequest;
import com.bytescheduler.adminx.modules.system.dto.request.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.response.UserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserPermissionResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserResponse;
import com.bytescheduler.adminx.modules.system.entity.SysUser;

public interface UserService extends IService<SysUser> {

    Result<String> updateUser(UpdateUserRequest params);

    Result<PageResult<UserResponse>> pageQuery(UserQueryRequest params);

    Result<UserInfoResponse> getUserInfo();

    Result<UserPermissionResponse> getPermissions();
}
