package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.BindingInfo;
import com.bytescheduler.adminx.modules.system.dto.request.BindingRequest;
import com.bytescheduler.adminx.modules.system.service.ThirdPartyAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/8/17
 */
@Api(tags = "三方账号")
@RequiredArgsConstructor
@RestController
@RequestMapping("/third-party")
public class ThirdPartyAuthController {

    private final ThirdPartyAuthService authService;

    @ApiOperation("绑定三方账号")
    @PostMapping("/bind")
    public Result<Void> bindAccount(@RequestBody BindingRequest params) {
        authService.bindAccount(params);
        return Result.success();
    }

    @ApiOperation("解绑三方账号")
    @DeleteMapping("/unbind/{provider}")
    public Result<?> unbindAccount(@PathVariable String provider) {
        return Result.success(provider);
    }

    @ApiOperation("绑定信息列表")
    @GetMapping("/bindList")
    public Result<List<BindingInfo>> bindList() {
        return Result.success(authService.bindList());
    }
}
