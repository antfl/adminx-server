package cn.byteout.adminx.modules.article.dto.request;

import cn.byteout.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author antfl
 * @since 2025/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageParams {

    @ApiModelProperty(value = "文章 ID", required = true)
    private Long articleId;
}
