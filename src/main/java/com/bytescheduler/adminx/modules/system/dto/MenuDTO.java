package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author byte-scheduler
 * @date 2025/5/14 21:43
 * @description
 */
@Data
public class MenuDTO {
    private Long menuId;
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;
    @NotNull(message = "菜单类型不能为空")
    @Range(min = 1, max = 3, message = "无效的菜单类型")
    private Integer menuType;
    private Long parentId;
    private String perms;
}
