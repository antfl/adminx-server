package cn.byteout.adminx.modules.article.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author antfl
 * @since 2025/7/5
 */
@Data
public class CommentUpdateRequest {

    @ApiModelProperty(value = "评论 ID")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    @Size(min = 1, max = 2000, message = "评论内容长度需在1-200位之间")
    private String content;
}
