package cn.byteout.adminx.modules.system.service;

import cn.byteout.adminx.modules.system.dto.request.LoginRequest;
import cn.byteout.adminx.modules.system.dto.request.PasswordResetRequest;
import cn.byteout.adminx.modules.system.dto.request.RegisterRequest;
import cn.byteout.adminx.modules.system.dto.response.CaptchaResponse;
import cn.byteout.adminx.modules.system.dto.response.MailCodeResponse;
import cn.byteout.adminx.modules.system.dto.response.TokenResponse;

/**
 * @author antfl
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
