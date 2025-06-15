package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.RoleMenuRequest;
import com.bytescheduler.adminx.modules.system.entity.RoleMenu;

/**
 * @author byte-scheduler
 * @since 2025/6/15
 */
public interface RoleMenuService extends IService<RoleMenu> {

    Result<String> setRoleMenus(RoleMenuRequest dto);
}
