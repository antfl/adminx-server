package com.bytescheduler.adminx.modules.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.entity.ArticleCategory;
import com.bytescheduler.adminx.modules.article.service.ArticleCategoryService;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Api(tags = "文章分类管理")
@AllArgsConstructor
@RestController
@RequestMapping("/article/category")
public class ArticleCategoryController {
    private final ArticleCategoryService categoryService;
    private final ArticleService articleService;

    @ApiOperation("保存文章分类（新增或修改）")
    @PostMapping("/save")
    public Result<?> saveCategory(@RequestBody ArticleCategory category) {
        try {
            return categoryService.saveOrUpdate(category) ?
                    Result.success("分类保存成功") : Result.failed("分类保存失败");
        } catch (BusinessException e) {
            return Result.failed(e.getMessage());
        }
    }

    @ApiOperation("删除文章分类")
    @DeleteMapping("/del/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getCategoryId, id);
        int articleCount = Math.toIntExact(articleService.count(articleQuery));

        if (articleCount > 0) {
            return Result.failed("该分类下有文章，无法删除");
        }

        return categoryService.removeById(id) ?
                Result.success("分类删除成功") : Result.failed("分类删除失败");
    }

    @ApiOperation("文章分类详情")
    @GetMapping("/{id}")
    public Result<ArticleCategory> getCategoryById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @ApiOperation("分页查询文章分类")
    @GetMapping("/page")
    public Result<Page<ArticleCategory>> getCategoryPage(ArticleCategoryRequest query) {
        return categoryService.getCategoryPage(query);
    }

    @ApiOperation("所有文章分类")
    @GetMapping("/all")
    public Result<List<ArticleCategory>> getAllCategories() {
        return Result.success(categoryService.list());
    }
}
