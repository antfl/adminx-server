package com.bytescheduler.adminx.modules.article.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/7/5
 */
@Data
public class CommentUpdateRequest {

    @ApiModelProperty(value = "评论 ID")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    private String content;
}
