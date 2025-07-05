package com.bytescheduler.adminx.modules.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/22
 */
@Data
public class CommentTreeResponse {

    @ApiModelProperty(value = "评论 ID")
    private Long id;

    @ApiModelProperty(value = "评论文章 ID")
    private Long articleId;

    @ApiModelProperty(value = "当前评论用户 ID")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论父级 ID")
    private Long parentId;

    @ApiModelProperty(value = "评论时间 ID")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "评论用户昵称")
    private String userName;

    @ApiModelProperty(value = "评论用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "当前评论是否作者")
    private Boolean isOwn;

    @ApiModelProperty(value = "被回复的用户昵称")
    private String replyTo;

    private List<CommentTreeResponse> replies = new ArrayList<>();
}
