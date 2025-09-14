package cn.byteout.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.article.dto.request.ArticleQueryRequest;
import cn.byteout.adminx.modules.article.dto.request.ArticleSaveRequest;
import cn.byteout.adminx.modules.article.dto.response.ArticleDetailResponse;
import cn.byteout.adminx.modules.article.dto.response.ArticlePageResponse;
import cn.byteout.adminx.modules.article.entity.Article;

/**
 * @author antfl
 * @since 2025/6/21
 */
public interface ArticleService extends IService<Article> {

    Result<Article> saveUpdate(ArticleSaveRequest params);

    Result<String> deleteArticle(Long id);

    void incrementLikeCount(Long articleId);

    void decrementLikeCount(Long articleId);

    ArticleDetailResponse getArticleDetailById(Long id);

    Result<PageResult<ArticlePageResponse>> pageQuery(ArticleQueryRequest params);
}
