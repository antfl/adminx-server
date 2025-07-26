package com.bytescheduler.adminx.modules.article.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author byte-scheduler
 * @since 2025/7/25
 */
@Data
public class ArticleSaveRequest {

    @ApiModelProperty(value = "文章 ID")
    private Long articleId;

    @ApiModelProperty(value = "文章标题")
    @NotBlank(message = "文章标题不能为空")
    @Size(min = 2, max = 100, message = "文章标题长度需在 2-100 位之间")
    @Pattern(regexp = "^\\S+$", message = "文章标题不能包含空格")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "文章分类 ID")
    private Long categoryId;
}
