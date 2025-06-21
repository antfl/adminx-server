package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章
 *
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
@TableName("article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long articleId;
    private String title;
    private String content;
    private Long categoryId;
    private Long userId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    private Integer status;
    private Integer likeCount;
    private Integer auditStatus;
    private String auditRemark;
}
