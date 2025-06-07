package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.LoginRequest;
import com.bytescheduler.adminx.modules.system.dto.RegisterRequest;
import com.bytescheduler.adminx.modules.system.dto.TokenResponse;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
public interface AuthService {

    void register(RegisterRequest request);

    TokenResponse login(LoginRequest request);
}
