package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.RolePermissionsRequest;
import cn.byteout.adminx.modules.system.dto.request.RoleQueryRequest;
import cn.byteout.adminx.modules.system.dto.request.RoleRequest;
import cn.byteout.adminx.modules.system.entity.SysRole;

/**
 * @author antfl
 * @since 2025/6/8
 */
public interface RoleService extends IService<SysRole> {

    Result<PageResult<SysRole>> pageQuery(RoleQueryRequest params);

    void createRole(RoleRequest dto);

    void updateRole(Long roleId, RoleRequest dto);

    Result<RolePermissionsRequest> getRolePermissions(Long roleId);
}
