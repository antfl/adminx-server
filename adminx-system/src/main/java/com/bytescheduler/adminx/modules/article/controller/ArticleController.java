package com.bytescheduler.adminx.modules.article.controller;

import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
import com.bytescheduler.adminx.modules.article.dto.response.ArticlePageResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Api(tags = "文章管理")
@AllArgsConstructor
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @ApiOperation("保存文章（新增或修改）")
    @PostMapping("/save")
    public Result<Article> saveArticle(@RequestBody Article article) {
        return articleService.saveUpdate(article);
    }

    @ApiOperation("删除文章")
    @DeleteMapping("del/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

    @ApiOperation("文章详情")
    @GetMapping("/{id}")
    public Result<ArticleDetailResponse> getArticleById(@PathVariable Long id) {
        return Result.success(articleService.getArticleDetailById(id));
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult<ArticlePageResponse>> pageArticle(ArticleQueryRequest params) {
        return articleService.pageQuery(params);
    }
}
