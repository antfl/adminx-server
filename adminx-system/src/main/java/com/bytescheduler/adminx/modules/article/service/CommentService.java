package com.bytescheduler.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.CommentCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CommentTreeResponse;
import com.bytescheduler.adminx.modules.article.entity.Comment;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface CommentService extends IService<Comment> {
    boolean saveComment(CommentCreateRequest request);

    Result<Page<CommentTreeResponse>> getCommentPage(CommentQueryRequest queryRequest);

    Result<?> auditComment(Long commentId, Integer auditStatus, String auditRemark);
}
