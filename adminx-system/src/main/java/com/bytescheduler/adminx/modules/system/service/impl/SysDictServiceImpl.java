package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.SqlEscapeUtil;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.dto.request.DictPageRequest;
import com.bytescheduler.adminx.modules.system.entity.SysDict;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;
import com.bytescheduler.adminx.modules.system.mapper.SysDictItemMapper;
import com.bytescheduler.adminx.modules.system.mapper.SysDictMapper;
import com.bytescheduler.adminx.modules.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
@RequiredArgsConstructor
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
    private final SysDictItemMapper dictItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<SysDict> saveUpdate(SysDict dict) {
        if (dict == null) {
            return Result.failed("字典数据不能为空");
        }

        boolean isInsert = dict.getId() == null;

        long count = baseMapper.selectCount(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getCreateUser, UserContext.getCurrentUserId())
        );

        if (isInsert && count >= 5) {
            return Result.failed("最多允许新增 5 个字典分类");
        }

        if (isInsert) {
            this.save(dict);
        } else {
            SysDict sysDict = this.getById(dict.getId());
            if (!Objects.equals(sysDict.getCreateUser(), UserContext.getCurrentUserId())) {
                return Result.failed("无该操作权限");
            }
            this.updateById(dict);
        }

        SysDict resultEntity = isInsert ? dict : this.getById(dict.getId());
        return Result.success(isInsert ? "新增成功" : "修改成功", resultEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteDict(Long id) {
        SysDict sysDict = this.getById(id);

        if (!Objects.equals(sysDict.getCreateUser(), UserContext.getCurrentUserId())) {
            return Result.failed("无该操作权限");
        }
        return this.removeById(id) ?
                Result.success() : Result.failed("删除失败");
    }

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
    public Result<PageResult<SysDict>> pageQuery(DictPageRequest params) {
        Page<SysDict> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getDictName()), SysDict::getDictName, SqlEscapeUtil.escapeLike(params.getDictName()))
                .like(StringUtils.isNotBlank(params.getDictCode()), SysDict::getDictCode, SqlEscapeUtil.escapeLike(params.getDictCode())
                ).orderByDesc(SysDict::getCreateTime);

        Page<SysDict> result = this.page(page, wrapper);

        return Result.success(PageResult.<SysDict>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(result.getRecords())
                .build());
    }
}
