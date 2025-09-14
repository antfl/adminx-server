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
public class ArticleCategoryRequest extends PageParams {

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "创建人")
    private String createUserName;
}
