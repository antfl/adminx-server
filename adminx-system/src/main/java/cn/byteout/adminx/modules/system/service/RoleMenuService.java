package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.RoleMenuRequest;
import cn.byteout.adminx.modules.system.entity.SysRoleMenu;

/**
 * @author antfl
 * @since 2025/6/15
 */
public interface RoleMenuService extends IService<SysRoleMenu> {

    Result<String> setRoleMenus(RoleMenuRequest dto);
}
