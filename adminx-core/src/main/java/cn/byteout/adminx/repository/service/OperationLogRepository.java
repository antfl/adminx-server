package cn.byteout.adminx.repository.service;

import cn.byteout.adminx.common.entity.SysOperationLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author antfl
 * @since 2025/6/8
 */
@Repository
public interface OperationLogRepository extends JpaRepository<SysOperationLog, Long> {
}
