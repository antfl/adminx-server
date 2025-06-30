package com.bytescheduler.adminx.modules.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章分类
 *
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
@TableName("article_category")
public class ArticleCategory {
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    private String categoryName;

    @TableField(fill = FieldFill.INSERT)
    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
