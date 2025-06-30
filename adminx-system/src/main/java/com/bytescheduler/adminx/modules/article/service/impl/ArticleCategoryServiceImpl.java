package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.SqlEscapeUtil;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.entity.ArticleCategory;
import com.bytescheduler.adminx.modules.article.mapper.ArticleCategoryMapper;
import com.bytescheduler.adminx.modules.article.service.ArticleCategoryService;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@AllArgsConstructor
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

    private final ArticleService articleService;

    @Override
    public Result<Page<ArticleCategory>> getCategoryPage(ArticleCategoryRequest categoryRequest) {
        Page<ArticleCategory> page = new Page<>(categoryRequest.getPageNum(), categoryRequest.getPageSize());
        LambdaQueryWrapper<ArticleCategory> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(categoryRequest.getKeyword())) {
            queryWrapper.like(ArticleCategory::getCategoryName, SqlEscapeUtil.escapeLike(categoryRequest.getKeyword()));
        }

        queryWrapper.orderByDesc(ArticleCategory::getCreateTime);
        return Result.success(page(page, queryWrapper));
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(ArticleCategory entity) {
        LambdaQueryWrapper<ArticleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCategory::getCategoryName, entity.getCategoryName());

        if (entity.getCategoryId() != null) {
            queryWrapper.ne(ArticleCategory::getCategoryId, entity.getCategoryId());
        }

        if (count(queryWrapper) > 0) {
            throw new BusinessException("分类名称已存在");
        }

        return super.saveOrUpdate(entity);
    }

    @Override
    public Result<?> deleteCategory(Long id) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getCategoryId, id);
        int articleCount = Math.toIntExact(articleService.count(articleQuery));

        if (articleCount > 0) {
            return Result.failed("该分类下有文章，无法删除");
        }

        ArticleCategory category = this.getById(id);
        if (!Objects.equals(category.getUserId(), this.getCurrentUserId())) {
            return Result.failed("无该操作权限");
        }

        return this.removeById(id) ?
                Result.success("分类删除成功") : Result.failed("分类删除失败");
    }

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}
