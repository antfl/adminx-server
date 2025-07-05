package com.bytescheduler.adminx.modules.article.controller;

import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.CommentCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentUpdateRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CommentTreeResponse;
import com.bytescheduler.adminx.modules.article.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Api(tags = "文章评论")
@AllArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation("文章评论")
    @PostMapping("/save")
    public Result<String> saveComment(@RequestBody CommentCreateRequest params) {
        return commentService.saveComment(params) ? Result.success() : Result.failed("评论成功");
    }

    @ApiOperation("删除文章评论")
    @DeleteMapping("/del/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id) ? Result.success("评论删除成功") : Result.failed("评论删除失败");
    }

    @ApiOperation("修改评论")
    @PutMapping("/update")
    public Result<String> updateComment(@RequestBody CommentUpdateRequest params) {
        return commentService.updateComment(params) ? Result.success() : Result.failed("修改失败");
    }

    @ApiOperation("文章评论分页查询")
    @GetMapping("/page")
    public Result<PageResult<CommentTreeResponse>> pageComment(CommentQueryRequest params) {
        return commentService.pageQuery(params);
    }
}
