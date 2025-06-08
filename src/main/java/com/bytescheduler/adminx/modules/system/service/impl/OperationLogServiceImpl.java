package com.bytescheduler.adminx.modules.system.service.impl;

import com.bytescheduler.adminx.modules.system.entity.OperationLog;
import com.bytescheduler.adminx.modules.system.service.OperationLogService;
import com.bytescheduler.adminx.repository.OperationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository logRepository;

    public OperationLogServiceImpl(OperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void save(OperationLog log) {
        logRepository.save(log);
    }

    @Override
    public Page<OperationLog> getLogs(String operator, String module, Pageable pageable) {
        if (StringUtils.hasText(operator) && StringUtils.hasText(module)) {
            return logRepository.findByOperatorAndModule(operator, module, pageable);
        } else if (StringUtils.hasText(operator)) {
            return logRepository.findByOperator(operator, pageable);
        } else if (StringUtils.hasText(module)) {
            return logRepository.findByModule(module, pageable);
        }
        return logRepository.findAll(pageable);
    }
}
