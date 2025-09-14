package cn.byteout.adminx.modules.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.byteout.adminx.modules.article.dto.request.ArticleCategoryRequest;
import cn.byteout.adminx.modules.article.dto.response.CategoryResponse;
import cn.byteout.adminx.modules.article.entity.ArticleCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author antfl
 * @since 2025/6/21
 */
@Mapper
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategory> {

    IPage<CategoryResponse> selectCategoryPage(IPage<CategoryResponse> page, @Param("params") ArticleCategoryRequest params);
}
