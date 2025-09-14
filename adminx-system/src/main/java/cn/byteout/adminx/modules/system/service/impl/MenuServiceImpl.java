package cn.byteout.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.common.utils.crypto.SqlEscapeUtil;
import cn.byteout.adminx.modules.system.dto.request.MenuPageRequest;
import cn.byteout.adminx.modules.system.entity.SysMenu;
import cn.byteout.adminx.modules.system.mapper.MenuMapper;
import cn.byteout.adminx.modules.system.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, SysMenu> implements MenuService {


    @Override
    public Result<SysMenu> saveUpdate(SysMenu menu) {
        if (menu == null) {
            return Result.failed("菜单数据不能为空");
        }

        boolean isInsert = menu.getId() == null;

        if (isInsert) {
            this.save(menu);
        } else {
            this.updateById(menu);
        }

        SysMenu resultEntity = isInsert ? menu : this.getById(menu.getId());
        return Result.success(isInsert ? "新增成功" : "修改成功", resultEntity);
    }

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> allMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSortOrder));
        return buildMenuTree(allMenus);
    }


    @Override
    public boolean updateMenuStatus(Long menuId, Integer status) {
        SysMenu menu = new SysMenu();
        menu.setId(menuId);
        menu.setStatus(status);
        return this.updateById(menu);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        Map<Long, SysMenu> menuMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getId, menu -> menu));

        List<SysMenu> rootMenus = new ArrayList<>();
        menus.forEach(menu -> {
            Long parentId = menu.getParentId();
            if (parentId == 0) {
                rootMenus.add(menu);
            } else {
                SysMenu parent = menuMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        });

        rootMenus.sort(Comparator.comparingInt(SysMenu::getSortOrder));
        rootMenus.forEach(this::sortChildren);
        return rootMenus;
    }

    @Override
    public Result<PageResult<SysMenu>> pageQuery(MenuPageRequest params) {
        Page<SysMenu> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getName()), SysMenu::getName, SqlEscapeUtil.escapeLike(params.getName()))
                .eq(params.getStatus() != null, SysMenu::getStatus, params.getStatus()
                ).orderByDesc(SysMenu::getSortOrder);

        Page<SysMenu> result = this.page(page, wrapper);

        return Result.success(PageResult.<SysMenu>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(result.getRecords())
                .build());
    }

    private void sortChildren(SysMenu menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort(Comparator.comparingInt(SysMenu::getSortOrder));
            menu.getChildren().forEach(this::sortChildren);
        }
    }
}