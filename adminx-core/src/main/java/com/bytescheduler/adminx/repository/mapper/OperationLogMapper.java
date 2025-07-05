package com.bytescheduler.adminx.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.common.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author byte-scheduler
 * @since 2025/7/5
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
