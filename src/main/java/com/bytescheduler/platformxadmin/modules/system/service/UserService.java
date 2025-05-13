package com.bytescheduler.platformxadmin.modules.system.service;

import com.bytescheduler.platformxadmin.modules.system.dto.UserLoginDTO;
import com.bytescheduler.platformxadmin.modules.system.dto.UserRegisterDTO;

public interface UserService {
    void register(UserRegisterDTO dto);
    String login(UserLoginDTO dto);
}
