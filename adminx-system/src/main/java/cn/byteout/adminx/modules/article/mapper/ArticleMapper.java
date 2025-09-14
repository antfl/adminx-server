package cn.byteout.adminx.modules.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.byteout.adminx.modules.article.dto.request.ArticleQueryRequest;
import cn.byteout.adminx.modules.article.dto.response.ArticleDetailResponse;
import cn.byteout.adminx.modules.article.dto.response.ArticlePageResponse;
import cn.byteout.adminx.modules.article.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author antfl
 * @since 2025/6/21
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    ArticleDetailResponse selectArticleDetailById(@Param("id") Long id, @Param("userId") Long userId);

    Page<ArticlePageResponse> pageQueryArticle(@Param("page") Page<ArticlePageResponse> page, @Param("params") ArticleQueryRequest params);
}
