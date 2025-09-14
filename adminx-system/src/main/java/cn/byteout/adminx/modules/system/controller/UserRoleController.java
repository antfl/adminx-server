package cn.byteout.adminx.modules.system.controller;

import cn.byteout.adminx.annotation.Log;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.enums.OperationType;
import cn.byteout.adminx.modules.system.dto.request.UserRoleRequest;
import cn.byteout.adminx.modules.system.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author antfl
 * @since 2025/6/16
 */
@Api(tags = "用户角色管理")
@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @ApiOperation("设置用户角色")
    @Log(module = "设置用户角色", type = OperationType.INSERT, value = "设置用户角色")
    @PostMapping("/setRoles")
    public Result<String> setUserRoles(@Valid @RequestBody UserRoleRequest params) {
        return userRoleService.setUserRoles(params);
    }
}
