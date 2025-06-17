package com.bytescheduler.adminx.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.UserQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.UserResponse;
import com.bytescheduler.adminx.modules.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation("用户分页查询")
    @Log(module = "用户管理", type = OperationType.SELECT, value = "用户分页查询")
    @GetMapping("/listUser")
    public Result<Page<UserResponse>> listUser(UserQueryRequest queryRequest) {
        return Result.success(userService.listUser(queryRequest));
    }
}
