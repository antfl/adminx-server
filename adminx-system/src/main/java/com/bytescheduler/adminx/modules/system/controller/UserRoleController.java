package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.UserRoleRequest;
import com.bytescheduler.adminx.modules.system.service.UserRoleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author byte-scheduler
 * @since 2025/6/16
 */
@Api(tags = "用户角色管理")
@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserRoleService userRoleService;

    @PostMapping("/setRoles")
    public Result<?> setUserRoles(@Valid @RequestBody UserRoleRequest dto) {
        return userRoleService.setUserRoles(dto);
    }
}
