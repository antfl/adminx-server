package com.bytescheduler.adminx.modules.article.dto.response;

import com.bytescheduler.adminx.modules.article.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentResponse extends Comment {

    @ApiModelProperty(value = "评论用户昵称")
    private String userName;

    @ApiModelProperty(value = "评论用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "当前评论是否作者")
    private Boolean isOwn;

    @ApiModelProperty(value = "被回复的用户昵称")
    private String replyToUserName;
}
