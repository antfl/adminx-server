package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;
import com.bytescheduler.adminx.modules.system.mapper.SysDictItemMapper;
import com.bytescheduler.adminx.modules.system.service.SysDictItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
@AllArgsConstructor
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    @Override
    public Result<Void> deleteDictData(Long id) {
        SysDictItem sysDictItem = this.getById(id);
        if (!Objects.equals(sysDictItem.getUserId(), this.getCurrentUserId())) {
            return Result.failed("无该操作权限");
        }
        return this.removeById(id) ?
                Result.success() : Result.failed("删除失败");
    }

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}
