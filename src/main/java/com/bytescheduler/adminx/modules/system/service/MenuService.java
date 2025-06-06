package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.modules.system.dto.MenuRequest;
import com.bytescheduler.adminx.modules.system.entity.Menu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/5/14
 */
public interface MenuService {

    @Transactional
    void saveMenu(MenuRequest dto);

    @Transactional
    void deleteMenu(Long menuId);

    List<Menu> getMenuTree();
}
