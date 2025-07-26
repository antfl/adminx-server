package com.bytescheduler.adminx.modules.article.controller;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.enums.OperationType;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.ArticleCategoryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CategoryResponse;
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

    @Log(module = "文章分类管理", type = OperationType.INSERT, value = "保存文章分类")
    @ApiOperation("保存文章分类（新增或修改）")
    @PostMapping("/saveUpdate")
    public Result<?> saveUpdate(@Valid @RequestBody ArticleCategoryCreateRequest params) {
        return categoryService.saveUpdate(params);
    }

    @Log(module = "文章分类管理", type = OperationType.DELETE, value = "删除文章分类")
    @ApiOperation("删除文章分类")
    @DeleteMapping("/del/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @Log(module = "文章分类管理", type = OperationType.SELECT, value = "文章分类详情")
    @ApiOperation("文章分类详情")
    @GetMapping("/{id}")
    public Result<ArticleCategory> getCategoryById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @Log(module = "文章分类管理", type = OperationType.SELECT, value = "分页查询文章分类")
    @ApiOperation("分页查询文章分类")
    @GetMapping("/page")
    public Result<PageResult<CategoryResponse>> pageCategory(@Valid ArticleCategoryRequest params) {
        return categoryService.pageQuery(params);
    }

    @Log(module = "文章分类管理", type = OperationType.SELECT, value = "所有文章分类")
    @ApiOperation("所有文章分类")
    @GetMapping("/list")
    public Result<List<ArticleCategory>> listCategory() {
        return Result.success(categoryService.list());
    }
}
