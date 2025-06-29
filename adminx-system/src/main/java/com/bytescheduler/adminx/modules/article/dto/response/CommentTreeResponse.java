package com.bytescheduler.adminx.modules.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/22
 */
@Data
public class CommentTreeResponse {
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long parentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer auditStatus;
    private String auditRemark;

    private String userName;
    private String userAvatar;
    private Boolean isOwn;
    private String replyTo;

    private List<CommentTreeResponse> replies = new ArrayList<>();
}
