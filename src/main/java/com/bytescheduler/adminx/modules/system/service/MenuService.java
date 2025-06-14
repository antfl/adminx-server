package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu>  getMenuTree();

    boolean updateMenuStatus(Long menuId, Integer status);
}