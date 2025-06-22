package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.system.dto.CommentCreateRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentQueryRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentRequest;
import com.bytescheduler.adminx.modules.system.dto.CommentTreeResponse;
import com.bytescheduler.adminx.modules.system.entity.Comment;
import com.bytescheduler.adminx.modules.system.mapper.CommentMapper;
import com.bytescheduler.adminx.modules.system.service.ArticleService;
import com.bytescheduler.adminx.modules.system.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public boolean saveComment(CommentCreateRequest request) {
        Long currentUserId = getCurrentUserId();
        Comment comment = new Comment();
        comment.setArticleId(request.getArticleId());
        comment.setContent(request.getContent());
        comment.setParentId(request.getParentId());
        comment.setUserId(currentUserId);

        if (request.getParentId() != null && request.getParentId() != 0) {
            if (request.getReplyToUserId() != null) {
                comment.setReplyToUserId(request.getReplyToUserId());
            } else {
                Comment parentComment = getById(request.getParentId());
                if (parentComment != null) {
                    comment.setReplyToUserId(parentComment.getUserId());
                }
            }
        }
//        comment.setAuditStatus(0);
        return save(comment);
    }

    @Override
    public Result<Page<CommentTreeResponse>> getCommentPage(CommentQueryRequest queryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (queryRequest.getArticleId() != null) {
            queryWrapper.eq("c.article_id", queryRequest.getArticleId());
        } else {
            return Result.failed("文章ID不能为空");
        }
        if (queryRequest.getUserId() != null) {
            queryWrapper.eq("c.user_id", queryRequest.getUserId());
        }
        if (queryRequest.getAuditStatus() != null) {
            queryWrapper.eq("c.audit_status", queryRequest.getAuditStatus());
        }
        Page<CommentRequest> pageInfo = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        Long currentUserId = getCurrentUserId();
        Page<CommentRequest> resultPage = commentMapper.selectCommentPage(pageInfo, queryWrapper, currentUserId);
        List<CommentTreeResponse> commentTree = buildCommentTree(resultPage.getRecords());
        Page<CommentTreeResponse> finalPage = new Page<>(
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal()
        );
        finalPage.setRecords(commentTree);
        return Result.success(finalPage);
    }

    private List<CommentTreeResponse> buildCommentTree(List<CommentRequest> flatList) {
        Map<Long, List<CommentTreeResponse>> replyMap = flatList.stream()
                .filter(c -> c.getParentId() != 0)
                .map(this::convertToTreeData)
                .collect(Collectors.groupingBy(CommentTreeResponse::getParentId));

        return flatList.stream()
                .filter(c -> c.getParentId() == 0)
                .map(topComment -> {
                    CommentTreeResponse node = convertToTreeData(topComment);
                    node.setReplies(replyMap.getOrDefault(node.getId(), Collections.emptyList()));
                    return node;
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Result<?> auditComment(Long commentId, Integer auditStatus, String auditRemark) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
//        comment.setAuditStatus(auditStatus);
//        comment.setAuditRemark(auditRemark);
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

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }

    private CommentTreeResponse convertToTreeData(CommentRequest dto) {
        CommentTreeResponse vo = new CommentTreeResponse();
        vo.setId(dto.getCommentId());
        vo.setArticleId(dto.getArticleId());
        vo.setUserId(dto.getUserId());
        vo.setContent(dto.getContent());
        vo.setParentId(dto.getParentId());
        vo.setCreateTime(dto.getCreateTime());

        vo.setUserName(dto.getUserName());
        vo.setUserAvatar(dto.getUserAvatar());
        vo.setIsOwn(dto.getIsOwn());
        vo.setReplyTo(dto.getReplyToUserName());

        return vo;
    }
}
