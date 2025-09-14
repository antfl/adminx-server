package cn.byteout.adminx.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.byteout.adminx.common.dto.response.DailyStatResponse;
import cn.byteout.adminx.common.dto.response.ModuleStatResponse;
import cn.byteout.adminx.common.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author antfl
 * @since 2025/7/5
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<SysOperationLog> {

    @Select("SELECT DATE(operation_time) AS date, COUNT(*) AS total, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS success, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS failure " +
            "FROM sys_operation_log " +
            "WHERE operation_time BETWEEN #{start} AND #{end} " +
            "GROUP BY DATE(operation_time) ORDER BY date")
    List<DailyStatResponse> selectDailyStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Select("SELECT module AS name, COUNT(*) AS value " +
            "FROM sys_operation_log " +
            "WHERE operation_time BETWEEN #{start} AND #{end} " +
            "GROUP BY module HAVING COUNT(*) > 0 ORDER BY value DESC LIMIT 10")
    List<ModuleStatResponse> selectModuleStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
