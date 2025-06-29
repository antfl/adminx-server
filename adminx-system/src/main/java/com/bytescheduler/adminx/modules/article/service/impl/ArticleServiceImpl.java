package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.SqlEscapeUtil;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleRequest;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.mapper.ArticleMapper;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@AllArgsConstructor
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    @Override
    public Result<Page<ArticleRequest>> getArticlePage(ArticleQueryRequest queryRequest) {
        Page<ArticleRequest> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(queryRequest.getTitle())) {
            queryWrapper.like("title", SqlEscapeUtil.escapeLike(queryRequest.getTitle()));
        }
        if (queryRequest.getCategoryId() != null) {
            queryWrapper.eq("a.category_id", queryRequest.getCategoryId());
        }
        if (queryRequest.getAuditStatus() != null) {
            queryWrapper.eq("a.audit_status", queryRequest.getAuditStatus());
        }
        if (queryRequest.getUserId() != null) {
            queryWrapper.eq("a.user_id", queryRequest.getUserId());
        }

        queryWrapper.orderByDesc("a.create_time");
        return Result.success(articleMapper.selectArticlePage(page, queryWrapper));
    }

    @Override
    @Transactional
    public Result<?> auditArticle(Long articleId, Integer auditStatus, String auditRemark) {
        Article article = new Article();
        article.setArticleId(articleId);
        article.setAuditStatus(auditStatus);
        article.setAuditRemark(auditRemark);
        return updateById(article) ? Result.success() : Result.failed("审核失败");
    }

    @Override
    public void updateCommentCount(Long articleId) {
        lambdaUpdate()
                .setSql("comment_count = comment_count + 1")
                .eq(Article::getArticleId, articleId)
                .update();
    }

    @Override
    public void incrementLikeCount(Long articleId) {
        lambdaUpdate()
                .setSql("like_count = like_count + 1")
                .eq(Article::getArticleId, articleId)
                .update();
    }

    @Override
    public void decrementLikeCount(Long articleId) {
        lambdaUpdate()
                .setSql("like_count = GREATEST(0, like_count - 1)")
                .eq(Article::getArticleId, articleId)
                .update();
    }

    @Override
    public ArticleDetailResponse getArticleDetailById(Long id) {
        Long currentUserId = getCurrentUserId();
        return articleMapper.selectArticleDetailById(id, currentUserId);
    }

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}
