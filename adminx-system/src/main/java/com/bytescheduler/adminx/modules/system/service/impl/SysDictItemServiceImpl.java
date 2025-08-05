package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.crypto.SqlEscapeUtil;
import com.bytescheduler.adminx.context.UserContextHolder;
import com.bytescheduler.adminx.modules.system.dto.request.DictItemPageRequest;
import com.bytescheduler.adminx.modules.system.entity.SysDict;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;
import com.bytescheduler.adminx.modules.system.mapper.SysDictItemMapper;
import com.bytescheduler.adminx.modules.system.mapper.SysDictMapper;
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

    private final SysDictMapper sysDictMapper;

    @Override
    public Result<SysDictItem> saveUpdate(SysDictItem dictItem) {
        if (dictItem == null) {
            return Result.failed("字典数据不能为空");
        }

        boolean isInsert = dictItem.getId() == null;

        long count = baseMapper.selectCount(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getCreateUser, UserContextHolder.get())
                .eq(SysDictItem::getDictId, dictItem.getDictId())
        );

        if (isInsert && count >= 10) {
            return Result.failed("最多允许新增 10 个字典数据");
        }

        if (isInsert) {

            SysDict sysDict = sysDictMapper.selectById(dictItem.getDictId());
            if (!Objects.equals(sysDict.getCreateUser(), UserContextHolder.get())) {
                return Result.failed("无该操作权限");
            }
            this.save(dictItem);
        } else {
            SysDictItem sysDictItem = this.getById(dictItem.getId());
            if (!Objects.equals(sysDictItem.getCreateUser(), UserContextHolder.get())) {
                return Result.failed("无该操作权限");
            }
            this.updateById(dictItem);
        }

        SysDictItem resultEntity = isInsert ? dictItem : this.getById(dictItem.getId());
        return Result.success(isInsert ? "新增成功" : "修改成功", resultEntity);
    }

    @Override
    public Result<Void> deleteDictData(Long id) {
        SysDictItem sysDictItem = this.getById(id);
        if (!Objects.equals(sysDictItem.getCreateUser(), UserContextHolder.get())) {
            return Result.failed("无该操作权限");
        }
        return this.removeById(id) ? Result.success() : Result.failed("删除失败");

    }

    @Override
    public Result<PageResult<SysDictItem>> pageQuery(DictItemPageRequest params) {
        Page<SysDictItem> page = Page.of(params.getCurrent(), params.getSize());

        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(params.getDictId() != null, SysDictItem::getDictId, params.getDictId())
                .like(StringUtils.isNotBlank(params.getItemLabel()), SysDictItem::getItemLabel, SqlEscapeUtil.escapeLike(params.getItemLabel()))
                .like(StringUtils.isNotBlank(params.getItemValue()), SysDictItem::getItemLabel, SqlEscapeUtil.escapeLike(params.getItemValue()))
                .orderByAsc(SysDictItem::getSort);

        Page<SysDictItem> result = this.page(page, wrapper);

        return Result.success(PageResult.<SysDictItem>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(result.getRecords())
                .build());
    }
}
