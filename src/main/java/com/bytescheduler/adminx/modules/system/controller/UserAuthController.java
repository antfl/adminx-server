package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.common.utils.EmailVerificationService;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.*;
import com.bytescheduler.adminx.modules.system.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "权限控制")
public class UserAuthController {
    private final AuthService authService;

    public UserAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest dto) {
        authService.register(dto);
        return Result.success("注册成功");
    }

    @Log(module = "用户登录", type = OperationType.OTHER, value = "用户登录")
    @PostMapping("/login")
    public Result<TokenResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return Result.success(authService.login(loginRequest));
    }

    @GetMapping("/captcha")
    public Result<CaptchaResponse> getCaptcha() {
        return Result.success(authService.generateCaptcha());
    }

    @ApiOperation("发送验证码")
    @GetMapping("/sendVerificationCodes/{email}")
    public Result<CaptchaResponse> sendVerificationCodes(@Valid @PathVariable String email) {
        boolean result = EmailVerificationService.sendVerificationEmail(email);
        return result ? Result.success() : Result.failed();
    }
}
