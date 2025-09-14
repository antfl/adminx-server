package cn.byteout.adminx.modules.system.controller;

import cn.byteout.adminx.annotation.Log;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.enums.OperationType;
import cn.byteout.adminx.modules.system.dto.request.UpdateUserRequest;
import cn.byteout.adminx.modules.system.dto.request.UserQueryRequest;
import cn.byteout.adminx.modules.system.dto.response.*;
import cn.byteout.adminx.modules.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/recent")
    @ApiOperation("获取最近 7 天新增用户")
    public Result<List<RecentUserResponse>> getRecentUsers() {
        return Result.success(userService.getRecentUsers());
    }

    @GetMapping("/active")
    @ApiOperation("获取活跃用户")
    public Result<List<ActiveUserResponse>> getActiveUsers() {
        return Result.success(userService.getActiveUsers());
    }
}
