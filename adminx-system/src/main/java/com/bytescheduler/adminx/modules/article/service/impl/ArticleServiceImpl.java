package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.SqlEscapeUtil;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.mapper.ArticleMapper;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    @Override
    public Result<Article> saveUpdate(Article article) {
        if (article == null) {
            return Result.failed("文章数据不能为空");
        }

        boolean isInsert = article.getArticleId() == null;

        if (isInsert) {
            this.save(article);
        } else {
            Article data = this.getById(article.getArticleId());
            if (!Objects.equals(data.getCreateUser(), UserContext.getCurrentUserId())) {
                throw new BusinessException("无该操作权限");
            }
            this.updateById(article);
        }

        Article resultEntity = isInsert ? article : this.getById(article.getArticleId());
        return Result.success(isInsert ? "新增成功" : "修改成功", resultEntity);
    }

    @Override
    public Result<String> deleteArticle(Long id) {
        Article article = this.getById(id);

        Long currentUserId = UserContext.getCurrentUserId();

        if (!Objects.equals(article.getCreateUser(), currentUserId)) {
            return Result.failed("无该操作权限");
        }

        return this.removeById(id) ? Result.success() : Result.failed("删除失败");
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
        Long currentUserId = UserContext.getCurrentUserId();
        return articleMapper.selectArticleDetailById(id, currentUserId);
    }

    @Override
    public Result<PageResult<Article>> pageQuery(ArticleQueryRequest params) {
        Page<Article> page = Page.of(params.getCurrent(), params.getSize());
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(params.getTitle()), Article::getTitle, SqlEscapeUtil.escapeLike(params.getTitle()))
                .eq(params.getCategoryId() != null, Article::getCategoryId, params.getCategoryId())
                .orderByDesc(Article::getCreateTime);

        Page<Article> result = this.page(page, wrapper);

        return Result.success(PageResult.<Article>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(result.getRecords())
                .build());
    }
}
