package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 *
 * @author byte-scheduler
 * @since 2025/6/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
public class SysMenu {

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父级 ID")
    private Long parentId;

    @ApiModelProperty(value = "菜单名（路由名称）")
    private String name;

    @ApiModelProperty(value = "菜单中文名")
    private String titleZh;

    @ApiModelProperty(value = "菜单英文名")
    private String titleEn;

    @ApiModelProperty(value = "地址")
    private String path;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否缓存（0-否，1-是）")
    @TableField("is_cache")
    private Boolean cache;

    @ApiModelProperty(value = "是否显示（0-否，1-是）")
    @TableField("is_visible")
    private Boolean visible;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = "菜单类型（0-目录，1-菜单，2-按钮）")
    private Integer menuType;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

    @ApiModelProperty(value = "状态（0-停用，1-正常）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty(value = "删除标志（0-未删除，1-已删除）")
    private Integer isDeleted;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}