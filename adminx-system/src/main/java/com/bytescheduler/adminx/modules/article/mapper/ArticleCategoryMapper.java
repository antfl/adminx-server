package com.bytescheduler.adminx.modules.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CategoryResponse;
import com.bytescheduler.adminx.modules.article.entity.ArticleCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Mapper
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategory> {

    IPage<CategoryResponse> selectCategoryPage(IPage<CategoryResponse> page, @Param("params") ArticleCategoryRequest params);
}
