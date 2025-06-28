package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.*;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
public interface AuthService {

    void register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    CaptchaResponse generateCaptcha();

    MailCodeResponse generateMailCode(String email);
}
