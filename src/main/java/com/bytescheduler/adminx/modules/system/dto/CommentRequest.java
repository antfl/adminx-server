package com.bytescheduler.adminx.modules.system.dto;

import com.bytescheduler.adminx.modules.system.entity.Comment;
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
