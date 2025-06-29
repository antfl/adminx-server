package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import com.bytescheduler.adminx.modules.system.mapper.MenuMapper;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Override
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = this.list(new LambdaQueryWrapper<Menu>()
                .orderByAsc(Menu::getSortOrder));
        return buildMenuTree(allMenus);
    }


    @Override
    public boolean updateMenuStatus(Long menuId, Integer status) {
        Menu menu = new Menu();
        menu.setId(menuId);
        menu.setStatus(status);
        return this.updateById(menu);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        List<Menu> rootMenus = new ArrayList<>();
        menus.forEach(menu -> {
            Long parentId = menu.getParentId();
            if (parentId == 0) {
                rootMenus.add(menu);
            } else {
                Menu parent = menuMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        });

        rootMenus.sort(Comparator.comparingInt(Menu::getSortOrder));
        rootMenus.forEach(this::sortChildren);
        return rootMenus;
    }

    private void sortChildren(Menu menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort(Comparator.comparingInt(Menu::getSortOrder));
            menu.getChildren().forEach(this::sortChildren);
        }
    }
}