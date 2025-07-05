package com.bytescheduler.adminx.modules.article.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/22
 */
@Data
public class CommentCreateRequest {

    @ApiModelProperty(value = "评论的文章 ID")
    private Long articleId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论父 ID")
    private Long parentId;

    @ApiModelProperty(value = "被回复的用户 ID")
    private Long replyToUserId;
}
