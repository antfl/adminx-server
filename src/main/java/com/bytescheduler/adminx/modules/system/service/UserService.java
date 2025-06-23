package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.UserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.UserResponse;
import com.bytescheduler.adminx.modules.system.entity.User;

public interface UserService extends IService<User> {

    Page<UserResponse> listUser(UserQueryRequest queryRequest);

    Result<UserInfoResponse> getUserInfo();
}
