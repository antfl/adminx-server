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
    @TableId(type = IdType.AUTO)
    private Long interactionId;
    @TableField(fill = FieldFill.INSERT)
    private Long userId;
    private Long articleId;
    private String type;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
