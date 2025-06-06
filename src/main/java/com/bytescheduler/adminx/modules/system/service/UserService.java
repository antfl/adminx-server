package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.UserLoginRequest;
import com.bytescheduler.adminx.modules.system.dto.UserRegisterRequest;

public interface UserService {
    void register(UserRegisterRequest dto);
    String login(UserLoginRequest dto);
}
