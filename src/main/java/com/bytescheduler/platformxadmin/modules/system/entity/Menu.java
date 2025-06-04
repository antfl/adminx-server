package com.bytescheduler.platformxadmin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author byte-scheduler
 * @date 2025/5/14 21:04
 * @description
 */
@Accessors(chain = true)
@TableName(value = "sys_menu")
@Data
public class Menu {
    @TableId(value = "menu_id",type = IdType.AUTO)
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

    @TableField(exist = false)
    private List<Menu> children;
}
