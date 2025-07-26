package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.system.dto.request.MenuPageRequest;
import com.bytescheduler.adminx.modules.system.entity.SysMenu;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Api(tags = "菜单控制")
public class MenuController {

    private final MenuService menuService;

    @Log(module = "菜单控制", type = OperationType.INSERT, value = "保存菜单")
    @PostMapping("/saveUpdate")
    @ApiOperation("保存菜单（新增或修改）")
    public Result<SysMenu> saveUpdate(@RequestBody @Validated SysMenu menu) {
        return menuService.saveUpdate(menu);
    }

    @Log(module = "菜单控制", type = OperationType.DELETE, value = "删除菜单")
    @DeleteMapping("/{id}")
    @ApiOperation("删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = menuService.removeById(id);
        return success ? Result.success() : Result.failed();
    }

    @Log(module = "菜单控制", type = OperationType.UPDATE, value = "更新菜单状态")
    @PutMapping("/status/{id}")
    @ApiOperation("更新菜单状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = menuService.updateMenuStatus(id, status);
        return success ? Result.success() : Result.failed();
    }

    @Log(module = "菜单控制", type = OperationType.SELECT, value = "分页查询菜单")
    @GetMapping("/page")
    @ApiOperation("分页查询菜单")
    public Result<PageResult<SysMenu>> pageMenu(@Valid MenuPageRequest params){
        return menuService.pageQuery(params);
    }

    @Log(module = "菜单控制", type = OperationType.SELECT, value = "获取菜单详情")
    @GetMapping("/{id}")
    @ApiOperation("获取菜单详情")
    public Result<SysMenu> getById(@PathVariable Long id) {
        SysMenu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @Log(module = "菜单控制", type = OperationType.SELECT, value = "获取菜单树")
    @GetMapping("/tree")
    @ApiOperation("获取菜单树")
    public Result<List<SysMenu>> getMenuTree() {
        List<SysMenu> menuTree = menuService.getMenuTree();
        return Result.success(menuTree);
    }
}