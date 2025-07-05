package com.bytescheduler.adminx.modules.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author byte-scheduler
 * @since 2025/6/22
 */
@Data
public class ArticleDetailResponse {

    @ApiModelProperty(value = "文章 ID")
    private Long articleId;

    @ApiModelProperty(value = "文章分类名称")
    private String categoryName;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "位置内容")
    private String content;

    @ApiModelProperty(value = "点赞数量")
    private Integer likeCount;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;

    @ApiModelProperty(value = "收藏数量")
    private Integer favoriteCount;

    @ApiModelProperty(value = "当前登录用户是否已经点赞")
    private Boolean isLiked;

    @ApiModelProperty(value = "当前登录用户是否已经收藏")
    private Boolean isFavorite;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人 ID")
    private Long userId;

    @ApiModelProperty(value = "创建人昵称")
    private String nickname;

    @ApiModelProperty(value = "创建人头像")
    private String avatar;
}
