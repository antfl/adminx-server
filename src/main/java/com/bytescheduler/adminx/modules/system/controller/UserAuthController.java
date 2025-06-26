package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.*;
import com.bytescheduler.adminx.modules.system.service.AuthService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "权限控制")
public class UserAuthController {
    private final AuthService authService;
//    private final CaptchaService captchaService;

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

}
