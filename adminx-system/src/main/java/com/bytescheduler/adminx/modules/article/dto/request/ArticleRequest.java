package com.bytescheduler.adminx.modules.article.dto.request;

import com.bytescheduler.adminx.modules.article.entity.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleRequest extends Article {
    private String categoryName;
    private String authorName;
}
