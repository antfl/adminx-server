package cn.byteout.adminx.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.dto.request.LogPageRequest;
import cn.byteout.adminx.common.dto.response.DailyStatResponse;
import cn.byteout.adminx.common.dto.response.ModuleStatResponse;
import cn.byteout.adminx.common.dto.response.OperationLogResponse;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.common.entity.SysOperationLog;

import java.util.List;

/**
 * @author antfl
 * @since 2025/6/8
 */
public interface OperationLogService extends IService<SysOperationLog> {

    void saveLog(SysOperationLog log);

    Result<PageResult<OperationLogResponse>> pageQuery(LogPageRequest params);

    List<DailyStatResponse> getDailyStats(int days);

    List<ModuleStatResponse> getModuleStats(int days);
}
