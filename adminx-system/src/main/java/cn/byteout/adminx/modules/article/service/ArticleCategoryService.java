package cn.byteout.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.article.dto.request.ArticleCategoryCreateRequest;
import cn.byteout.adminx.modules.article.dto.request.ArticleCategoryRequest;
import cn.byteout.adminx.modules.article.dto.response.CategoryResponse;
import cn.byteout.adminx.modules.article.entity.ArticleCategory;

/**
 * @author antfl
 * @since 2025/6/21
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {

    Result<ArticleCategory> saveUpdate(ArticleCategoryCreateRequest params);

    Result<String> deleteCategory(Long id);

    Result<PageResult<CategoryResponse>> pageQuery(ArticleCategoryRequest params);
}
