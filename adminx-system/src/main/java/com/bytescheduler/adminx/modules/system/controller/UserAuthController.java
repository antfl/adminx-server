package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.request.LoginRequest;
import com.bytescheduler.adminx.modules.system.dto.request.PasswordResetRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RegisterRequest;
import com.bytescheduler.adminx.modules.system.dto.response.CaptchaResponse;
import com.bytescheduler.adminx.modules.system.dto.response.MailCodeResponse;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;
import com.bytescheduler.adminx.modules.system.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Api(tags = "权限控制")
public class UserAuthController {

    private final AuthService authService;

    @ApiOperation("用户注册")
    @Log(module = "用户注册", type = OperationType.USER_REGISTER, value = "用户注册")
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest params) {
        authService.register(params);
        return Result.success("注册成功");
    }

    @ApiOperation("用户登录")
    @Log(module = "用户登录", type = OperationType.USER_LOGIN, value = "用户登录")
    @PostMapping("/login")
    public Result<TokenResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return Result.success(authService.login(loginRequest));
    }

    @ApiOperation("修改密码")
    @Log(module = "修改密码", type = OperationType.UPDATE, value = "修改密码")
    @PostMapping("/passwordReset")
    public Result<String> passwordReset(@Valid @RequestBody PasswordResetRequest params) {
        authService.passwordReset(params);
        return Result.success("操作成功");
    }

    @ApiOperation("获取图片验证码")
    @Log(module = "获取图片验证码", type = OperationType.OTHER, value = "获取图片验证码")
    @GetMapping("/captcha")
    public Result<CaptchaResponse> getCaptcha() {
        return Result.success(authService.generateCaptcha());
    }

    @ApiOperation("获取邮箱验证码")
    @Log(module = "获取邮箱验证码", type = OperationType.OTHER, value = "获取邮箱验证码")
    @GetMapping("/sendMailCode/{mail}/{type}")
    public Result<MailCodeResponse> getMailCaptcha(@Valid @PathVariable String mail, @PathVariable String type) {
        return Result.success(authService.generateMailCode(mail, type));
    }
}
