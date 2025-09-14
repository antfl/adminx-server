package cn.byteout.adminx.modules.system.dto.response;

import cn.byteout.adminx.modules.system.entity.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * @author antfl
 * @since 2025/7/6
 */
@Data
@Accessors(chain = true)
public class UserPermissionResponse {

    @ApiModelProperty(value = "菜单集合")
    private List<SysMenu> menus;

    @ApiModelProperty(value = "按钮权限集合")
    private Set<String> permissions;
}
