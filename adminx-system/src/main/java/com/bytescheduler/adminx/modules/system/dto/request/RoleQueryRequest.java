package com.bytescheduler.adminx.modules.system.dto.request;

import com.bytescheduler.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryRequest extends PageParams {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    private String roleKey;

    @ApiModelProperty(value = "角色状态")
    private Integer status;
}
