package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Data
public class RoleRequest {
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "角色标识不能为空")
    private String roleKey;

    private Integer sort = 0;
    private Integer dataScope = 1;
    private Integer status = 0;
    private String remark;
}
