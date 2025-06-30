package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author byte-scheduler
 * @since 2025/6/18
 */
@Data
@ApiModel(value = "字典对象", description = "系统字典表")
@TableName("sys_dict")
public class SysDict {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "字典名称", required = true)
    private String dictName;

    @ApiModelProperty(value = "字典编码", required = true)
    private String dictCode;

    @TableField(fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
