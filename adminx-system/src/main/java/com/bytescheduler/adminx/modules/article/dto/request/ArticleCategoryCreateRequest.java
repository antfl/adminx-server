package com.bytescheduler.adminx.modules.article.dto.request;

import com.bytescheduler.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCategoryCreateRequest extends PageParams {

    @ApiModelProperty(value = "分类名称")
    private Long categoryId;

    @ApiModelProperty(value = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 2, max = 50, message = "分类名称长度需在 2-50 位之间")
    @Pattern(regexp = "^\\S+$", message = "分类名称不能包含空格")
    private String categoryName;

    @ApiModelProperty(value = "备注")
    @Size(max = 500, message = "备注长度不能大于 500 字符")
    private String remark;
}
