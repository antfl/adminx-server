package com.bytescheduler.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.entity.Interaction;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface InteractionService extends IService<Interaction> {

    Result<String> toggleInteraction(Interaction interaction);
}
