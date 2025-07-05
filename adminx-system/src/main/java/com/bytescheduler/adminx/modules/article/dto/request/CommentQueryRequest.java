package com.bytescheduler.adminx.modules.article.dto.request;

import com.bytescheduler.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageParams {

    @ApiModelProperty(value = "文章 ID", required = true)
    private Long articleId;
}
