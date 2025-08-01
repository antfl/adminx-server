package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author byte-scheduler
 * @since 2025/7/27
 */
@Data
@TableName("sys_file_record")
public class SysFileRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fileName;

    private String filePath;

    private Long fileSize;

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
}
