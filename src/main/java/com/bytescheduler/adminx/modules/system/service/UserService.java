package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.modules.system.dto.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.UserResponse;

public interface UserService {

    Page<UserResponse> listUser(UserQueryRequest queryRequest);
}
