package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.MenuPageRequest;
import com.bytescheduler.adminx.modules.system.entity.SysMenu;

import java.util.List;

public interface MenuService extends IService<SysMenu> {

    Result<SysMenu> saveUpdate(SysMenu menu);

    boolean updateMenuStatus(Long menuId, Integer status);

    List<SysMenu> getMenuTree();

    Result<PageResult<SysMenu>> pageQuery(MenuPageRequest params);

    List<SysMenu> buildMenuTree(List<SysMenu> menus);
}