package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleSaveRequest;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
import com.bytescheduler.adminx.modules.article.dto.response.ArticlePageResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.mapper.ArticleMapper;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import com.bytescheduler.adminx.modules.system.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    private final FileService fileService;

    @Override
    public Result<Article> saveUpdate(ArticleSaveRequest params) {
        if (params == null) {
            return Result.failed("文章数据不能为空");
        }

        boolean isInsert = params.getArticleId() == null;

        long count = baseMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCreateUser, UserContext.getCurrentUserId()));
        if (isInsert && count >= 50) {
            return Result.failed("每个用户最多可以新建 50 个文章");
        }

        Article article = new Article();
        BeanUtils.copyProperties(params, article);

        if (isInsert) {

            this.save(article);
        } else {
            Article data = this.getById(params.getArticleId());
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
        ArticleDetailResponse res = articleMapper.selectArticleDetailById(id, currentUserId);
        res.setAvatar(fileService.getFileToken(res.getAvatar()));
        return res;
    }

    @Override
    public Result<PageResult<ArticlePageResponse>> pageQuery(ArticleQueryRequest params) {

        Page<ArticlePageResponse> page = new Page<>(params.getCurrent(), params.getSize());
        articleMapper.pageQueryArticle(page, params);

        return Result.success(PageResult.<ArticlePageResponse>builder()
                .total(page.getTotal())
                .current(page.getCurrent())
                .size(page.getSize())
                .pages(page.getPages())
                .records(page.getRecords())
                .build());
    }
}
