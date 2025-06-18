package com.bytescheduler.adminx.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.entity.SysDict;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;
import com.bytescheduler.adminx.modules.system.service.SysDictItemService;
import com.bytescheduler.adminx.modules.system.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
@AllArgsConstructor
@RestController
@RequestMapping("/dict")
@Api(tags = "字典管理")
public class DictController {
    private final SysDictService dictService;
    private final SysDictItemService dictItemService;

    @PostMapping("/add")
    @ApiOperation("新增字典")
    public Result<SysDict> createDict(@RequestBody SysDict dict) {
        if (dictService.save(dict)) {
            return Result.success(dict);
        }
        return Result.failed("新增失败");
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询字典")
    public Result<SysDict> getDictById(@PathVariable Long id) {
        SysDict dict = dictService.getById(id);
        return dict != null ? Result.success(dict) : Result.failed("字典不存在");
    }

    @GetMapping("/code/{dictCode}")
    @ApiOperation("根据编码查询字典项")
    public Result<List<SysDictItem>> getItemsByCode(@PathVariable String dictCode) {
        return dictService.getDictItemsByCode(dictCode);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除字典")
    public Result<Void> deleteDict(@PathVariable Long id) {
        return dictService.removeById(id) ?
                Result.success() : Result.failed("删除失败");
    }

    @GetMapping("/page")
    @ApiOperation("分页查询字典")
    public Result<Page<SysDict>> pageDict(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "dictName", required = false) String dictName,
            @RequestParam(value = "dictCode", required = false) String dictCode) {

        Page<SysDict> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dictName), SysDict::getDictName, dictName)
                .like(StringUtils.isNotBlank(dictCode), SysDict::getDictCode, dictCode)
                .orderByDesc(SysDict::getCreateTime);

        return Result.success(dictService.page(page, wrapper));
    }

    @GetMapping("/pageData")
    @ApiOperation("分页查询字典值")
    public Result<Page<SysDictItem>> pageDictItem(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "dictId", required = false) Long dictId,
            @RequestParam(value = "itemLabel", required = false) String itemLabel) {

        Page<SysDictItem> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dictId != null, SysDictItem::getDictId, dictId)
                .like(StringUtils.isNotBlank(itemLabel), SysDictItem::getItemLabel, itemLabel)
                .orderByAsc(SysDictItem::getSort);

        return Result.success(dictItemService.page(page, wrapper));
    }
}
