package cn.byteout.adminx.modules.system.service;

import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.UserRoleRequest;

/**
 * @author antfl
 * @since 2025/6/16
 */
public interface UserRoleService {

    Result<String> setUserRoles(UserRoleRequest params);
}
