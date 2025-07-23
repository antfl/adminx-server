package com.bytescheduler.adminx.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.dto.request.LogPageRequest;
import com.bytescheduler.adminx.common.dto.response.DailyStatResponse;
import com.bytescheduler.adminx.common.dto.response.ModuleStatResponse;
import com.bytescheduler.adminx.common.dto.response.OperationLogResponse;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.entity.OperationLog;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
public interface OperationLogService extends IService<OperationLog> {

    void saveLog(OperationLog log);

    Result<PageResult<OperationLogResponse>> pageQuery(LogPageRequest params);

    List<DailyStatResponse> getDailyStats(int days);

    List<ModuleStatResponse> getModuleStats(int days);
}
