package com.bytescheduler.adminx.modules.article.controller;

import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.entity.ArticleCategory;
import com.bytescheduler.adminx.modules.article.service.ArticleCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ApiOperation("保存文章分类（新增或修改）")
    @PostMapping("/save")
    public Result<?> saveCategory(@RequestBody ArticleCategory category) {
        return categoryService.saveUpdate(category);
    }

    @ApiOperation("删除文章分类")
    @DeleteMapping("/del/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @ApiOperation("文章分类详情")
    @GetMapping("/{id}")
    public Result<ArticleCategory> getCategoryById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @ApiOperation("分页查询文章分类")
    @GetMapping("/page")
    public Result<PageResult<ArticleCategory>> pageCategory(@Valid ArticleCategoryRequest params) {
        return categoryService.pageQuery(params);
    }

    @ApiOperation("所有文章分类")
    @GetMapping("/list")
    public Result<List<ArticleCategory>> listCategory() {
        return Result.success(categoryService.list());
    }
}
