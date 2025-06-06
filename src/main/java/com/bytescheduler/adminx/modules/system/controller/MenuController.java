package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.MenuRequest;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统菜单资源管理控制
 *
 * @author byte-scheduler
 * @since 2025/5/14
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public Result<?> saveMenu(@Valid @RequestBody MenuRequest dto) {
        menuService.saveMenu(dto);
        return Result.success();
    }

    @DeleteMapping("/{menuId}")
    public Result<?> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree() {
        return Result.success(menuService.getMenuTree());
    }
}
