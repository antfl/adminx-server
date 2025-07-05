package com.bytescheduler.adminx.modules.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 互动记录表
 *
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
@TableName("interaction")
public class Interaction {
    /**
     * 互动记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Long interactionId;

    /**
     * 互动记录对应的文章 ID
     */
    private Long articleId;

    /**
     * 互动分类（LIKE-点赞，FAVORITE-收藏）
     */
    private String type;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
