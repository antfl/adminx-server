package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.ArticleRequest;
import com.bytescheduler.adminx.modules.system.entity.Article;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface ArticleService extends IService<Article> {
    Result<Page<ArticleRequest>> getArticlePage(ArticleQueryRequest queryRequest);

    Result<?> auditArticle(Long articleId, Integer auditStatus, String auditRemark);

    void updateCommentCount(Long articleId);

    void incrementLikeCount(Long articleId);

    void decrementLikeCount(Long articleId);
}
