package com.bytescheduler.adminx.repository.service;

import com.bytescheduler.adminx.common.entity.SysOperationLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Repository
public interface OperationLogRepository extends JpaRepository<SysOperationLog, Long> {
}
