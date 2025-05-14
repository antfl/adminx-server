package com.bytescheduler.platformxadmin.modules.system.service.impl;

import com.bytescheduler.platformxadmin.modules.system.dto.MenuDTO;
import com.bytescheduler.platformxadmin.modules.system.entity.Menu;
import com.bytescheduler.platformxadmin.modules.system.mapper.MenuMapper;
import com.bytescheduler.platformxadmin.modules.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author byte-scheduler
 * @date 2025/5/14 21:22
 * @description
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Transactional
    @Override
    public void saveMenu(MenuDTO dto) {
        // 通用校验
        if (dto.getMenuType() == 3 && StringUtils.isEmpty(dto.getPerms())) {
            throw new IllegalArgumentException("按钮必须设置权限标识");
        }

        Menu menu = convertToEntity(dto);
        if (dto.getMenuId() == null) {
            // 新增逻辑
            if (dto.getParentId() != 0 && menuMapper.selectById(dto.getParentId()) == null) {
                throw new IllegalArgumentException("父菜单不存在");
            }
            menuMapper.insert(menu);
        } else {
            // 更新逻辑
            if (dto.getParentId().equals(dto.getMenuId())) {
                throw new IllegalArgumentException("不能设置自己为父菜单");
            }
            menuMapper.update(menu);
        }
    }

    @Transactional
    @Override
    public void deleteMenu(Long menuId) {
        if (menuMapper.existsByParentId(menuId)) {
            throw new IllegalArgumentException("存在子菜单不可删除");
        }
        menuMapper.deleteById(menuId);
    }

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = menuMapper.selectAll();
        Map<Long, List<Menu>> parentMap = allMenus.stream()
                .collect(Collectors.groupingBy(Menu::getParentId));

        allMenus.forEach(menu ->
                menu.setChildren(parentMap.getOrDefault(menu.getMenuId(), Collections.emptyList())));

        return parentMap.getOrDefault(0L, Collections.emptyList());
    }

    private Menu convertToEntity(MenuDTO dto) {
        // 使用BeanUtils或手动映射
        Menu menu = new Menu();
        BeanUtils.copyProperties(dto, menu);
        return menu;
    }
}
