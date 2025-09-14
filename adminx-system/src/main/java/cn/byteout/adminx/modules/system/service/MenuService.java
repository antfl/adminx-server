package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.MenuPageRequest;
import cn.byteout.adminx.modules.system.entity.SysMenu;

import java.util.List;

public interface MenuService extends IService<SysMenu> {

    Result<SysMenu> saveUpdate(SysMenu menu);

    boolean updateMenuStatus(Long menuId, Integer status);

    List<SysMenu> getMenuTree();

    Result<PageResult<SysMenu>> pageQuery(MenuPageRequest params);

    List<SysMenu> buildMenuTree(List<SysMenu> menus);
}