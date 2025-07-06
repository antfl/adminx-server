package com.bytescheduler.adminx.modules.system.service;

import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.system.dto.request.UserRoleRequest;

/**
 * @author byte-scheduler
 * @since 2025/6/16
 */
public interface UserRoleService {

    Result<String> setUserRoles(UserRoleRequest params);
}
