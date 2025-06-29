package com.bytescheduler.adminx.modules.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleRequest;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
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
    public Result<?> saveArticle(@RequestBody Article article) {
        return articleService.saveOrUpdate(article) ?
                Result.success() : Result.failed("保存失败");
    }

    @ApiOperation("删除文章")
    @DeleteMapping("del/{id}")
    public Result<?> deleteArticle(@PathVariable Long id) {
        return articleService.removeById(id) ?
                Result.success() : Result.failed("删除失败");
    }

    @ApiOperation("文章详情")
    @GetMapping("/{id}")
    public Result<ArticleDetailResponse> getArticleById(@PathVariable Long id) {
        return Result.success(articleService.getArticleDetailById(id));
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<Page<ArticleRequest>> getArticlePage(ArticleQueryRequest queryRequest) {
        return articleService.getArticlePage(queryRequest);
    }

    @ApiOperation("审核文章")
    @PostMapping("/audit")
    public Result<?> auditArticle(@RequestParam Long articleId,
                                  @RequestParam Integer auditStatus,
                                  @RequestParam(required = false) String auditRemark) {
        return articleService.auditArticle(articleId, auditStatus, auditRemark);
    }
}
