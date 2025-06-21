package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.system.entity.ArticleCategory;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {
    Result<Page<ArticleCategory>> getCategoryPage(ArticleCategoryRequest categoryRequest);
}
