package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.context.UserContextHolder;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CategoryResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.entity.ArticleCategory;
import com.bytescheduler.adminx.modules.article.mapper.ArticleCategoryMapper;
import com.bytescheduler.adminx.modules.article.service.ArticleCategoryService;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@RequiredArgsConstructor
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

    private final ArticleService articleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ArticleCategory> saveUpdate(ArticleCategoryCreateRequest params) {
        if (params == null) {
            return Result.failed("分类数据不能为空");
        }

        boolean isInsert = params.getCategoryId() == null;

        long count = baseMapper.selectCount(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getCreateUser, UserContextHolder.get()));
        if (isInsert && count >= 3) {
            return Result.failed("每个用户最多可以新建 3 个分类");
        }

        LambdaQueryWrapper<ArticleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCategory::getCategoryName, params.getCategoryName());

        if (params.getCategoryId() != null) {
            queryWrapper.ne(ArticleCategory::getCategoryId, params.getCategoryId());
        }

        if (count(queryWrapper) > 0) {
            throw new BusinessException("分类名称已存在");
        }

        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setCategoryName(params.getCategoryName());
        articleCategory.setRemark(params.getRemark());
        articleCategory.setCategoryId(params.getCategoryId());

        if (isInsert) {
            this.save(articleCategory);
        } else {
            ArticleCategory category = this.getById(params.getCategoryId());
            if (!Objects.equals(category.getCreateUser(), UserContextHolder.get())) {
                throw new BusinessException("无该操作权限");
            }
            this.updateById(articleCategory);
        }

        ArticleCategory resultEntity = isInsert ? articleCategory : this.getById(params.getCategoryId());
        return Result.success(isInsert ? "新增成功" : "修改成功", resultEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteCategory(Long id) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getCategoryId, id);
        int articleCount = Math.toIntExact(articleService.count(articleQuery));

        if (articleCount > 0) {
            return Result.failed("该分类下有文章，无法删除");
        }

        ArticleCategory category = this.getById(id);
        if (!Objects.equals(category.getCreateUser(), UserContextHolder.get())) {
            return Result.failed("无该操作权限");
        }

        return this.removeById(id) ? Result.success("删除成功") : Result.failed("删除失败");
    }

    @Override
    public Result<PageResult<CategoryResponse>> pageQuery(ArticleCategoryRequest params) {

        IPage<CategoryResponse> page = new Page<>(params.getCurrent(), params.getSize());
        baseMapper.selectCategoryPage(page, params);

        return Result.success(PageResult.<CategoryResponse>builder()
                .total(page.getTotal())
                .current(page.getCurrent())
                .size(page.getSize())
                .pages(page.getPages())
                .records(page.getRecords())
                .build());
    }
}
