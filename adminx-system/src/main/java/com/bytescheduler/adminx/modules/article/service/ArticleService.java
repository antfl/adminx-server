package com.bytescheduler.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleSaveRequest;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
import com.bytescheduler.adminx.modules.article.dto.response.ArticlePageResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface ArticleService extends IService<Article> {

    Result<Article> saveUpdate(ArticleSaveRequest params);

    Result<String> deleteArticle(Long id);

    void incrementLikeCount(Long articleId);

    void decrementLikeCount(Long articleId);

    ArticleDetailResponse getArticleDetailById(Long id);

    Result<PageResult<ArticlePageResponse>> pageQuery(ArticleQueryRequest params);
}
