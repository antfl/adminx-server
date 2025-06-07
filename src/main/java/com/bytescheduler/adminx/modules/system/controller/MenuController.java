package com.bytescheduler.adminx.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.MenuTreeRequest;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单控制")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    @ApiOperation("创建菜单")
    public Result<Long> create(@RequestBody @Validated Menu menu) {
        boolean success = menuService.save(menu);
        return success ? Result.success(menu.getId()) : Result.failed();
    }

    @PutMapping("/{id}")
    @ApiOperation("更新菜单")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated Menu menu) {
        menu.setId(id);
        boolean success = menuService.updateById(menu);
        return success ? Result.success() : Result.failed();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = menuService.removeById(id);
        return success ? Result.success() : Result.failed();
    }

    @GetMapping("/{id}")
    @ApiOperation("获取菜单详情")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @GetMapping("/tree")
    @ApiOperation("获取菜单树")
    public Result<List<MenuTreeRequest>> getMenuTree() {
        List<MenuTreeRequest> menuTree = menuService.getMenuTree().stream()
                .map(this::convertToTreeRequest)
                .collect(Collectors.toList());
        return Result.success(menuTree);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询菜单")
    public Result<Page<Menu>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Menu> page = new Page<>(pageNum, pageSize);
        Page<Menu> menuPage = menuService.page(page);
        return Result.success(menuPage);
    }

    @PutMapping("/status/{id}")
    @ApiOperation("更新菜单状态")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        boolean success = menuService.updateMenuStatus(id, status);
        return success ? Result.success() : Result.failed();
    }

    private MenuTreeRequest convertToTreeRequest(Menu menu) {
        MenuTreeRequest dto = new MenuTreeRequest();
        dto.setId(menu.getId());
        dto.setParentId(menu.getParentId());
        dto.setName(menu.getName());
        dto.setTitle(menu.getTitle());
        dto.setPath(menu.getPath());
        dto.setComponent(menu.getComponent());
        dto.setIcon(menu.getIcon());
        dto.setCache(menu.getCache());
        dto.setVisible(menu.getVisible());
        dto.setRedirect(menu.getRedirect());
        dto.setMenuType(menu.getMenuType());
        dto.setPermission(menu.getPermission());
        dto.setSortOrder(menu.getSortOrder());
        dto.setStatus(menu.getStatus());
        return dto;
    }
}