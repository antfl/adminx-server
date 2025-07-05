package com.bytescheduler.adminx.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.dto.request.LogPageRequest;
import com.bytescheduler.adminx.common.entity.OperationLog;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.SqlEscapeUtil;
import com.bytescheduler.adminx.repository.mapper.OperationLogMapper;
import com.bytescheduler.adminx.repository.service.OperationLogRepository;
import com.bytescheduler.adminx.repository.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements  OperationLogService {

    private final OperationLogRepository logRepository;

    public OperationLogServiceImpl(OperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void saveLog(OperationLog log) {
        logRepository.save(log);
    }

    @Override
    public Result<PageResult<OperationLog>> pageQuery(LogPageRequest params) {

        Page<OperationLog> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getOperator()), OperationLog::getOperator, SqlEscapeUtil.escapeLike(params.getOperator()))
                .eq(params.getModule() != null, OperationLog::getModule, params.getModule()
                ).orderByDesc(OperationLog::getOperationTime);

        Page<OperationLog> result = this.page(page, wrapper);

        return Result.success(PageResult.<OperationLog>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(result.getRecords())
                .build());
    }
}
