package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
public interface SysDictItemService extends IService<SysDictItem> {

    Result<Void> deleteDictData(Long id);
}
