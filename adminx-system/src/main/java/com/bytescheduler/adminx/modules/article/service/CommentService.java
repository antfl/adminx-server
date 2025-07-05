package com.bytescheduler.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.dto.request.CommentCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentUpdateRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CommentTreeResponse;
import com.bytescheduler.adminx.modules.article.entity.Comment;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public interface CommentService extends IService<Comment> {

    boolean saveComment(CommentCreateRequest params);

    boolean deleteComment(Long id);

    boolean updateComment(CommentUpdateRequest params);

    Result<PageResult<CommentTreeResponse>> pageQuery(CommentQueryRequest params);
}
