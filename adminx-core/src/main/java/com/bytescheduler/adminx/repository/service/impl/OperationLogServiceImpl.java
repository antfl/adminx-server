package com.bytescheduler.adminx.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.dto.request.LogPageRequest;
import com.bytescheduler.adminx.common.dto.response.DailyStatResponse;
import com.bytescheduler.adminx.common.dto.response.ModuleStatResponse;
import com.bytescheduler.adminx.common.dto.response.OperationLogResponse;
import com.bytescheduler.adminx.common.entity.SysOperationLog;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.crypto.SqlEscapeUtil;
import com.bytescheduler.adminx.repository.mapper.OperationLogMapper;
import com.bytescheduler.adminx.repository.service.OperationLogRepository;
import com.bytescheduler.adminx.repository.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@RequiredArgsConstructor
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, SysOperationLog> implements  OperationLogService {

    private final OperationLogRepository logRepository;
    private final OperationLogMapper logMapper;

    @Override
    public void saveLog(SysOperationLog log) {
        logRepository.save(log);
    }

    @Override
    public Result<PageResult<OperationLogResponse>> pageQuery(LogPageRequest params) {

        Page<SysOperationLog> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getOperator()), SysOperationLog::getOperator, SqlEscapeUtil.escapeLike(params.getOperator()))
                .eq(params.getModule() != null, SysOperationLog::getModule, params.getModule()
                ).orderByDesc(SysOperationLog::getOperationTime);

        Page<SysOperationLog> result = this.page(page, wrapper);

        List<OperationLogResponse> records = new ArrayList<>();
        result.getRecords().forEach(record -> {
            OperationLogResponse operationLogResponse = new OperationLogResponse();
            BeanUtils.copyProperties(record, operationLogResponse);
            records.add(operationLogResponse);
        });

        return Result.success(PageResult.<OperationLogResponse>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(records)
                .build());
    }

    @Override
    public List<DailyStatResponse> getDailyStats(int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return logMapper.selectDailyStats(start, end);
    }

    @Override
    public List<ModuleStatResponse> getModuleStats(int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return logMapper.selectModuleStats(start, end);
    }
}
