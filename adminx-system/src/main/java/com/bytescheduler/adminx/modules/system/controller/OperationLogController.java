package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.common.dto.request.LogPageRequest;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.repository.service.OperationLogService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import com.bytescheduler.adminx.common.entity.OperationLog;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Api(tags = "系统日志")
@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class OperationLogController {

    private final OperationLogService logService;

    @GetMapping("/page")
    public Result<PageResult<OperationLog>> pageLog(@Valid LogPageRequest params) {
        return logService.pageQuery(params);
    }
}
