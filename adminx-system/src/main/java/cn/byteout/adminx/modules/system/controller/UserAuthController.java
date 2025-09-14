package cn.byteout.adminx.modules.system.controller;

import cn.byteout.adminx.annotation.Log;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.enums.OperationType;
import cn.byteout.adminx.modules.system.dto.request.*;
import cn.byteout.adminx.modules.system.service.AuthService;
import cn.byteout.adminx.modules.system.service.ThirdPartyAuthService;
import cn.byteout.adminx.modules.system.dto.response.CaptchaResponse;
import cn.byteout.adminx.modules.system.dto.response.MailCodeResponse;
import cn.byteout.adminx.modules.system.dto.response.TokenResponse;
import cn.byteout.adminx.modules.system.strategy.UnifiedThirdPartyLoginService;
import cn.byteout.adminx.modules.system.utils.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Api(tags = "权限控制")
public class UserAuthController {

    private final AuthUtil authUtil;
    private final AuthService authService;
    private final ThirdPartyAuthService thirdPartyAuthService;
    private final UnifiedThirdPartyLoginService unifiedLoginService;

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

    @ApiOperation("用户退出登录")
    @Log(module = "用户退出登录", type = OperationType.USER_LOGIN, value = "用户登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authUtil.logout();
        return Result.success();
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

    @ApiOperation("三方账号登录")
    @PostMapping("/third-party")
    public Result<TokenResponse> thirdPartyLogin(@RequestBody BindingRequest params) {
        TokenResponse response = unifiedLoginService.handleLogin(
                params.getProvider(),
                params.getAuthCode()
        );
        return Result.success(response);
    }

    @ApiOperation("根据三方账号创建用户")
    @PostMapping("/createUser")
    public Result<TokenResponse> createUser(@RequestBody CreateUser params, HttpServletRequest request) {
        return Result.success(thirdPartyAuthService.createUser(params, request));
    }
}
