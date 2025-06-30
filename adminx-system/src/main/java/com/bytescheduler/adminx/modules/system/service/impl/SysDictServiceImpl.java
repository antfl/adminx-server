package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.entity.SysDict;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;
import com.bytescheduler.adminx.modules.system.mapper.SysDictItemMapper;
import com.bytescheduler.adminx.modules.system.mapper.SysDictMapper;
import com.bytescheduler.adminx.modules.system.service.SysDictService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
@AllArgsConstructor
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
    private final SysDictItemMapper dictItemMapper;

    @Override
    public Result<List<SysDictItem>> getDictItemsByCode(String dictCode) {
        SysDict dict = baseMapper.selectByDictCode(dictCode);
        if (dict == null) {
            return Result.failed("字典不存在");
        }
        List<SysDictItem> items = dictItemMapper.selectByDictId(dict.getId());
        return Result.success(items);
    }

    @Override
    public Result<Void> deleteDict(Long id) {
        SysDict sysDict = this.getById(id);

        if (!Objects.equals(sysDict.getUserId(), this.getCurrentUserId())) {
            return Result.failed("无该操作权限");
        }
        return this.removeById(id) ?
                Result.success() : Result.failed("删除失败");
    }

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}
