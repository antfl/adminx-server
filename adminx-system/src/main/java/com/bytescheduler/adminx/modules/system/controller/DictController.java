package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.request.DictItemPageRequest;
import com.bytescheduler.adminx.modules.system.dto.request.DictPageRequest;
import com.bytescheduler.adminx.modules.system.entity.SysDict;
import com.bytescheduler.adminx.modules.system.entity.SysDictItem;
import com.bytescheduler.adminx.modules.system.service.SysDictItemService;
import com.bytescheduler.adminx.modules.system.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
@Api(tags = "字典管理")
public class DictController {

    private final SysDictService dictService;
    private final SysDictItemService dictItemService;

    @Log(module = "字典管理", type = OperationType.INSERT, value = "保存字典")
    @PostMapping("/saveUpdate")
    @ApiOperation("保存字典（新增或修改）")
    public Result<SysDict> saveUpdate(@RequestBody SysDict dict) {
       return dictService.saveUpdate(dict);
    }

    @Log(module = "字典管理", type = OperationType.INSERT, value = "保存字典数据")
    @PostMapping("/saveUpdateData")
    @ApiOperation("保存字典数据（新增或修改）")
    public Result<SysDictItem> saveUpdateData(@RequestBody SysDictItem dictItem) {
        return dictItemService.saveUpdate(dictItem);
    }

    @Log(module = "字典管理", type = OperationType.DELETE, value = "删除字典类型")
    @DeleteMapping("/deleteDict/{id}")
    @ApiOperation("删除字典类型")
    public Result<Void> deleteDict(@PathVariable Long id) {
        return dictService.deleteDict(id);
    }

    @Log(module = "字典管理", type = OperationType.DELETE, value = "删除字典值")
    @DeleteMapping("/deleteDictData/{id}")
    @ApiOperation("删除字典值")
    public Result<Void> deleteDictData(@PathVariable Long id) {
        return dictItemService.deleteDictData(id);
    }

    @Log(module = "字典管理", type = OperationType.SELECT, value = "根据 ID 查询字典")
    @GetMapping("/{id}")
    @ApiOperation("根据 ID 查询字典")
    public Result<SysDict> getDictById(@PathVariable Long id) {
        SysDict dict = dictService.getById(id);
        return dict != null ? Result.success(dict) : Result.failed("字典不存在");
    }

    @Log(module = "字典管理", type = OperationType.SELECT, value = "根据编码查询字典项")
    @GetMapping("/code/{dictCode}")
    @ApiOperation("根据编码查询字典项")
    public Result<List<SysDictItem>> getItemsByCode(@PathVariable String dictCode) {
        return dictService.getDictItemsByCode(dictCode);
    }

    @Log(module = "字典管理", type = OperationType.SELECT, value = "分页查询字典")
    @GetMapping("/page")
    @ApiOperation("分页查询字典")
    public Result<PageResult<SysDict>> pageDict(@Valid DictPageRequest params) {
        return dictService.pageQuery(params);
    }

    @Log(module = "字典管理", type = OperationType.SELECT, value = "分页查询字典值")
    @GetMapping("/pageData")
    @ApiOperation("分页查询字典值")
    public Result<PageResult<SysDictItem>> pageDictData(@Valid DictItemPageRequest params) {
        return dictItemService.pageQuery(params);
    }
}
