package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.dto.CommentQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentRequest;
import com.bytescheduler.adminx.modules.system.entity.Comment;
import com.bytescheduler.adminx.modules.system.mapper.CommentMapper;
import com.bytescheduler.adminx.modules.system.service.ArticleService;
import com.bytescheduler.adminx.modules.system.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@AllArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final CommentMapper commentMapper;
    private final ArticleService articleService;

    @Override
    public Result<Page<CommentRequest>> getCommentPage(CommentQueryRequest queryRequest) {
        Page<CommentRequest> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        if (queryRequest.getArticleId() != null) {
            queryWrapper.eq("c.article_id", queryRequest.getArticleId());
        }
        if (queryRequest.getUserId() != null) {
            queryWrapper.eq("c.user_id", queryRequest.getUserId());
        }
        if (queryRequest.getAuditStatus() != null) {
            queryWrapper.eq("c.audit_status", queryRequest.getAuditStatus());
        }
        if (queryRequest.getParentId() != null) {
            queryWrapper.eq("c.parent_id", queryRequest.getParentId());
        } else {
            queryWrapper.eq("c.parent_id", 0);
        }

        queryWrapper.orderByDesc("c.create_time");
        return Result.success(commentMapper.selectCommentPage(page, queryWrapper));
    }

    @Override
    @Transactional
    public Result<?> auditComment(Long commentId, Integer auditStatus, String auditRemark) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setAuditStatus(auditStatus);
        comment.setAuditRemark(auditRemark);
        return updateById(comment) ? Result.success() : Result.failed("审核失败");
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(Comment comment) {
        boolean result = super.saveOrUpdate(comment);

        if (result && comment.getCommentId() == null) {
            articleService.updateCommentCount(comment.getArticleId());
        }
        return result;
    }
}
