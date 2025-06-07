package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import com.bytescheduler.adminx.modules.system.mapper.MenuMapper;
import com.bytescheduler.adminx.modules.system.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {



    @Override
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = this.list(new LambdaQueryWrapper<Menu>()
                .orderByAsc(Menu::getSortOrder));
        return allMenus;
    }


    @Override
    public boolean updateMenuStatus(Long menuId, Integer status) {
        Menu menu = new Menu();
        menu.setId(menuId);
        menu.setStatus(status);
        return this.updateById(menu);
    }
}