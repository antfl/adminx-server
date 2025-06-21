package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.CommentQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentRequest;
import com.bytescheduler.adminx.modules.system.entity.Comment;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface CommentService extends IService<Comment> {
    Result<Page<CommentRequest>> getCommentPage(CommentQueryRequest queryRequest);
    Result<?> auditComment(Long commentId, Integer auditStatus, String auditRemark);
}
