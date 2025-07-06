package com.bytescheduler.adminx.modules.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论
 *
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
@TableName("comment")
public class Comment {

    /**
     * 评论 ID
     */
    @TableId(type = IdType.AUTO)
    private Long commentId;

    /**
     * 文章 ID
     */
    private Long articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论 ID（0-顶级评论）
     */
    private Long parentId;

    /**
     * 被回复的用户 ID
     */
    private Long replyToUserId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
