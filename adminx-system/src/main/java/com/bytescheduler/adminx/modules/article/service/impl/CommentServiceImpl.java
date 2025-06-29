package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.article.dto.request.CommentCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CommentTreeResponse;
import com.bytescheduler.adminx.modules.article.entity.Comment;
import com.bytescheduler.adminx.modules.article.mapper.CommentMapper;
import com.bytescheduler.adminx.modules.article.service.ArticleService;
import com.bytescheduler.adminx.modules.article.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        return save(comment);
    }

    @Override
    public Result<Page<CommentTreeResponse>> getCommentPage(CommentQueryRequest queryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        if (queryRequest.getArticleId() == null) {
            return Result.failed("文章 ID 不能为空");
        }
        queryWrapper.eq("c.article_id", queryRequest.getArticleId());

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
        Map<Long, CommentTreeResponse> commentMap = new HashMap<>();
        Map<Long, List<CommentTreeResponse>> topLevelRepliesMap = new HashMap<>();
        List<CommentTreeResponse> topLevelComments = new ArrayList<>();

        for (CommentRequest dto : flatList) {
            CommentTreeResponse vo = convertToTreeData(dto);
            commentMap.put(vo.getId(), vo);

            if (vo.getParentId() == 0) {
                topLevelComments.add(vo);
                topLevelRepliesMap.put(vo.getId(), new ArrayList<>());
            }
        }

        for (CommentRequest dto : flatList) {
            if (dto.getParentId() != 0) {
                CommentTreeResponse reply = convertToTreeData(dto);
                Long rootCommentId = findRootCommentId(dto.getCommentId(), commentMap);

                if (rootCommentId != null) {
                    List<CommentTreeResponse> replies = topLevelRepliesMap.get(rootCommentId);
                    if (replies != null) {
                        replies.add(reply);
                    }
                }
            }
        }

        for (CommentTreeResponse top : topLevelComments) {
            List<CommentTreeResponse> replies = topLevelRepliesMap.get(top.getId());
            if (replies != null) {
                replies.sort(Comparator.comparing(CommentTreeResponse::getCreateTime));
                top.setReplies(replies);
            }
        }

        return topLevelComments;
    }

    private Long findRootCommentId(Long commentId, Map<Long, CommentTreeResponse> commentMap) {
        Long currentId = commentId;
        int maxDepth = 10;
        while (maxDepth-- > 0) {
            CommentTreeResponse current = commentMap.get(currentId);
            if (current == null) return null;

            if (current.getParentId() == 0) {
                return current.getId();
            }

            currentId = current.getParentId();
        }

        return null;
    }


    @Override
    @Transactional
    public Result<?> auditComment(Long commentId, Integer auditStatus, String auditRemark) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
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
