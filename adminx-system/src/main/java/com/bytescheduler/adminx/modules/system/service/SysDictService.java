package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.DictPageRequest;
import com.bytescheduler.adminx.modules.system.entity.SysDict;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
public interface SysDictService extends IService<SysDict> {

    Result<SysDict> saveUpdate(SysDict sysDict);

    Result<Void> deleteDict(Long id);

    Result<List<SysDictItem>> getDictItemsByCode(String dictCode);

    Result<PageResult<SysDict>> pageQuery(DictPageRequest params);
}
