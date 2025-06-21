package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.entity.Interaction;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface InteractionService extends IService<Interaction> {
    Result<?> toggleInteraction(Interaction interaction);
}
