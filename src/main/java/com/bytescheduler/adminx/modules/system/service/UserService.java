package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.UserLoginDTO;
import com.bytescheduler.adminx.modules.system.dto.UserRegisterDTO;

public interface UserService {
    void register(UserRegisterDTO dto);
    String login(UserLoginDTO dto);
}
