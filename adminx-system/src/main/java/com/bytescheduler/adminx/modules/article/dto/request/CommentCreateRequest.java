package com.bytescheduler.adminx.modules.article.dto.request;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/22
 */
@Data
public class CommentCreateRequest {
    private Long articleId;
    private String content;
    private Long parentId;
    private Long replyToUserId;
}
