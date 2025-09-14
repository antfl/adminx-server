package cn.byteout.adminx.modules.system.dto.request;

import cn.byteout.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author antfl
 * @since 2025/7/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuPageRequest extends PageParams {

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "状态（0：停用，1：正常）")
    private Integer status;
}
