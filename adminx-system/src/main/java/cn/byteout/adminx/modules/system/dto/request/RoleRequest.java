package cn.byteout.adminx.modules.system.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author antfl
 * @since 2025/6/8
 */
@Data
public class RoleRequest {

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    @NotBlank(message = "角色标识不能为空")
    private String roleKey;

    @ApiModelProperty(value = "排序")
    private Integer sort = 0;

    @ApiModelProperty(value = "数据范围")
    private Integer dataScope = 1;

    @ApiModelProperty(value = "状态")
    private Integer status = 0;

    @ApiModelProperty(value = "备注")
    private String remark;
}
