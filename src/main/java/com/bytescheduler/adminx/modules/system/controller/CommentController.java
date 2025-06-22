package com.bytescheduler.adminx.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.CommentCreateRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentTreeResponse;
import com.bytescheduler.adminx.modules.system.service.CommentService;
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

    @ApiOperation("文章评论（新增或修改）")
    @PostMapping("/save")
    public Result<?> saveComment(@RequestBody CommentCreateRequest request) {
        return commentService.saveComment(request) ?
                Result.success() : Result.failed("保存失败");
    }

    @ApiOperation("删除文章评论")
    @DeleteMapping("/{id}")
    public Result<?> deleteComment(@PathVariable Long id) {
        return commentService.removeById(id) ?
                Result.success("评论删除成功") : Result.failed("评论删除失败");
    }

    @ApiOperation("审核文章评论")
    @PostMapping("/audit")
    public Result<?> auditComment(@RequestParam Long commentId,
                                  @RequestParam Integer auditStatus,
                                  @RequestParam(required = false) String auditRemark) {
        return commentService.auditComment(commentId, auditStatus, auditRemark);
    }

    @ApiOperation("文章评论分页查询")
    @GetMapping("/page")
    public Result<Page<CommentTreeResponse>> getCommentPage(CommentQueryRequest queryRequest) {
        return commentService.getCommentPage(queryRequest);
    }
}
