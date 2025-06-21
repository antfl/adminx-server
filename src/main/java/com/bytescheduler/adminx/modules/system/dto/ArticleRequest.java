package com.bytescheduler.adminx.modules.system.dto;

import com.bytescheduler.adminx.modules.system.entity.Article;
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
