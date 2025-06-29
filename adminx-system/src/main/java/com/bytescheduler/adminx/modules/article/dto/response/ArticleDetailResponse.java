package com.bytescheduler.adminx.modules.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author byte-scheduler
 * @since 2025/6/22
 */
@Data
public class ArticleDetailResponse {
    private Long articleId;
    private String title;
    private String content;
    private Integer status;
    private Integer likeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private String categoryName;
    private Boolean isLiked;
    private Boolean isFavorite;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Long userId;
    private String nickname;
    private String avatar;
}
