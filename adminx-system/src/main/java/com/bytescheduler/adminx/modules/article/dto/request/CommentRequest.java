package com.bytescheduler.adminx.modules.article.dto.request;

import com.bytescheduler.adminx.modules.article.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentRequest extends Comment {
    private String userName;
    private String userAvatar;
    private Boolean isOwn;
    private String replyToUserName;
}
