package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long articleId;
    @TableField(fill = FieldFill.INSERT)
    private Long userId;
    private String content;
    private Long parentId;
    private Long replyToUserId;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 可扩展是否审核评论
    @TableField(exist = false)
    private Integer auditStatus;
    @TableField(exist = false)
    private String auditRemark;
}
