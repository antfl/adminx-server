package com.bytescheduler.adminx.modules.system.entity;

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
    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long articleId;
    private Long userId;
    private String content;
    private Long parentId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private Integer auditStatus;
    private String auditRemark;
}
