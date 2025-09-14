package cn.byteout.adminx.modules.article.controller;

import cn.byteout.adminx.annotation.Log;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.enums.OperationType;
import cn.byteout.adminx.modules.article.dto.request.ArticleQueryRequest;
import cn.byteout.adminx.modules.article.dto.request.ArticleSaveRequest;
import cn.byteout.adminx.modules.article.dto.response.ArticleDetailResponse;
import cn.byteout.adminx.modules.article.dto.response.ArticlePageResponse;
import cn.byteout.adminx.modules.article.entity.Article;
import cn.byteout.adminx.modules.article.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author antfl
 * @since 2025/6/21
 */
@Api(tags = "文章管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @Log(module = "文章管理", type = OperationType.INSERT, value = "保存文章")
    @ApiOperation("保存文章（新增或修改）")
    @PostMapping("/save")
    public Result<Article> saveArticle(@Valid  @RequestBody ArticleSaveRequest params) {
        return articleService.saveUpdate(params);
    }

    @Log(module = "文章管理", type = OperationType.DELETE, value = "删除文章")
    @ApiOperation("删除文章")
    @DeleteMapping("del/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

    @Log(module = "文章管理", type = OperationType.SELECT, value = "文章详情")
    @ApiOperation("文章详情")
    @GetMapping("/{id}")
    public Result<ArticleDetailResponse> getArticleById(@PathVariable Long id) {
        return Result.success(articleService.getArticleDetailById(id));
    }

    @Log(module = "文章管理", type = OperationType.SELECT, value = "分页查询")
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult<ArticlePageResponse>> pageArticle(ArticleQueryRequest params) {
        return articleService.pageQuery(params);
    }
}
