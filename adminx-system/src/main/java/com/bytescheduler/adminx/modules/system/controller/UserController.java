package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.request.UpdateUserRequest;
import com.bytescheduler.adminx.modules.system.dto.request.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.response.UserInfoResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserPermissionResponse;
import com.bytescheduler.adminx.modules.system.dto.response.UserResponse;
import com.bytescheduler.adminx.modules.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation("用户分页查询")
    @Log(module = "用户管理", type = OperationType.SELECT, value = "用户分页查询")
    @GetMapping("/page")
    public Result<PageResult<UserResponse>> pageUser(@Valid UserQueryRequest params) {
        return userService.pageQuery(params);
    }

    @ApiOperation("修改用户信息")
    @Log(module = "用户管理", type = OperationType.UPDATE, value = "修改用户信息")
    @PostMapping("/updateUser")
    public Result<String> updateUser(@Valid @RequestBody UpdateUserRequest params) {
        return userService.updateUser(params);
    }

    @ApiOperation("获取用户信息")
    @Log(module = "用户管理", type = OperationType.SELECT, value = "获取用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserInfoResponse> getUserInfo() {
        return userService.getUserInfo();
    }

    @ApiOperation("获取用户权限")
    @Log(module = "用户管理", type = OperationType.SELECT, value = "获取用户权限")
    @GetMapping("/permissions")
    public Result<UserPermissionResponse> getPermissions() {
        return userService.getPermissions();
    }
}
