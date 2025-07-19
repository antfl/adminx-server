package com.bytescheduler.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CategoryResponse;
import com.bytescheduler.adminx.modules.article.entity.ArticleCategory;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {

    Result<ArticleCategory> saveUpdate(ArticleCategoryCreateRequest params);

    Result<String> deleteCategory(Long id);

    Result<PageResult<CategoryResponse>> pageQuery(ArticleCategoryRequest params);
}
