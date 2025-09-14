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
public class ArticleQueryRequest extends PageParams {

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章分类")
    private Long categoryId;

    @ApiModelProperty(value = "操作人")
    private String createUserName;
}
