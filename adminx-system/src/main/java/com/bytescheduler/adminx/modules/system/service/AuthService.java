package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.request.LoginRequest;
import com.bytescheduler.adminx.modules.system.dto.request.PasswordResetRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RegisterRequest;
import com.bytescheduler.adminx.modules.system.dto.response.CaptchaResponse;
import com.bytescheduler.adminx.modules.system.dto.response.MailCodeResponse;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
public interface AuthService {

    void register(RegisterRequest params);

    TokenResponse login(LoginRequest params);

    void passwordReset(PasswordResetRequest params);

    CaptchaResponse generateCaptcha();

    MailCodeResponse generateMailCode(String email, String type);

    void disableUserAndClearToken(Long userId);
}
