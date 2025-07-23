package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.common.dto.request.LogPageRequest;
import com.bytescheduler.adminx.common.dto.response.DailyStatResponse;
import com.bytescheduler.adminx.common.dto.response.ModuleStatResponse;
import com.bytescheduler.adminx.common.dto.response.OperationLogResponse;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.repository.service.OperationLogService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Api(tags = "系统日志")
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class OperationLogController {

    private final OperationLogService logService;

    @GetMapping("/page")
    public Result<PageResult<OperationLogResponse>> pageLog(@Valid LogPageRequest params) {
        return logService.pageQuery(params);
    }

    @GetMapping("/daily")
    public Result<List<DailyStatResponse>> getDailyStats(
            @RequestParam(defaultValue = "7") int days) {
        return Result.success(logService.getDailyStats(days));
    }

    @GetMapping("/modules")
    public Result<List<ModuleStatResponse>> getModuleStats(
            @RequestParam(defaultValue = "30") int days) {
        return Result.success(logService.getModuleStats(days));
    }
}
