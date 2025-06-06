package com.bytescheduler.adminx.modules.system.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/5/14
 */
@Accessors(chain = true)
@Data
public class Menu {
    private Long menuId;
    private Long parentId = 0L;
    private String menuName;
    private Integer menuType; // 1-目录 2-菜单 3-按钮
    private String path;
    private String component;
    private String perms;
    private String icon;
    private Integer sort = 0;
    private Integer status = 0;
    private Integer isExternal = 0;
    private Integer isCache = 0;
    private Integer isVisible = 1;
    private Date createTime;
    private Date updateTime;

    private List<Menu> children;
}
