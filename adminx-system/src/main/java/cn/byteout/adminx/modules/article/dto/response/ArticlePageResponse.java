package cn.byteout.adminx.modules.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author antfl
 * @since 2025/7/19
 */
@Data
@Accessors(chain = true)
public class ArticlePageResponse {

    @ApiModelProperty(value = "文章 ID")
    private Long articleId;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "分类 ID")
    private Long categoryId;

    @ApiModelProperty(value = "点赞数量")
    private Integer likeCount;

    @ApiModelProperty(value = "收藏数量")
    private Integer favoriteCount;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
