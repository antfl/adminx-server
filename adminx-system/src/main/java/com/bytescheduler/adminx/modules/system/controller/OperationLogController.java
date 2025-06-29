package com.bytescheduler.adminx.modules.system.controller;
import com.bytescheduler.adminx.repository.service.OperationLogService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import com.bytescheduler.adminx.common.entity.OperationLog;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Api(tags = "系统日志")
@RestController
@RequestMapping("/logs")
public class OperationLogController {
    private final OperationLogService logService;

    public OperationLogController(OperationLogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public void saveLog(@RequestBody OperationLog log) {
        logService.save(log);
    }

    @GetMapping
    public Page<OperationLog> getLogs(
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String module,
            @PageableDefault(sort = "operationTime", direction = Sort.Direction.DESC) Pageable pageable) {

        return logService.getLogs(operator, module, pageable);
    }
}
