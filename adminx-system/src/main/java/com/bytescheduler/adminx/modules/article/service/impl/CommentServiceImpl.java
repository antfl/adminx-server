package com.bytescheduler.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.entity.PageResult;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.modules.article.dto.request.CommentCreateRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentQueryRequest;
import com.bytescheduler.adminx.modules.article.dto.request.CommentUpdateRequest;
import com.bytescheduler.adminx.modules.article.dto.response.CommentResponse;
import com.bytescheduler.adminx.modules.article.dto.response.CommentTreeResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import com.bytescheduler.adminx.modules.article.entity.Comment;
import com.bytescheduler.adminx.modules.article.mapper.ArticleMapper;
import com.bytescheduler.adminx.modules.article.mapper.CommentMapper;
import com.bytescheduler.adminx.modules.article.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveComment(CommentCreateRequest params) {

        Comment comment = new Comment();
        comment.setArticleId(params.getArticleId());
        comment.setContent(params.getContent());
        comment.setParentId(params.getParentId());

        if (params.getParentId() != null && params.getParentId() != 0) {
            if (params.getReplyToUserId() != null) {
                comment.setReplyToUserId(params.getReplyToUserId());
            } else {
                Comment parentComment = getById(params.getParentId());
                if (parentComment != null) {
                    comment.setReplyToUserId(parentComment.getCreateUser());
                }
            }
        }
        return save(comment);
    }

    @Override
    public boolean deleteComment(Long id) {
        if (id == null) {
            throw new BusinessException("ID 不能为空");
        }

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentId, id);
        Comment comment = this.getOne(queryWrapper);

        Long currentUserId = UserContext.getCurrentUserId();
        if (!Objects.equals(comment.getCreateUser(), currentUserId)) {
            throw new BusinessException("只允许删除自己的评论");
        }

        return this.removeById(id);
    }

    @Override
    public boolean updateComment(CommentUpdateRequest params) {
        if (params.getId() == null) {
            throw new BusinessException("评论 ID 不能为空");
        }
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentId, params.getId());
        Comment comment = this.getOne(queryWrapper);

        Long currentUserId = UserContext.getCurrentUserId();
        if (!Objects.equals(comment.getCreateUser(), currentUserId)) {
            throw new BusinessException("只允许修改自己的评论");
        }

        comment.setContent(params.getContent());
        return updateById(comment);
    }

    @Override
    public Result<PageResult<CommentTreeResponse>> pageQuery(CommentQueryRequest params) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        if (params.getArticleId() == null) {
            return Result.failed("文章 ID 不能为空");
        }
        queryWrapper.eq("c.article_id", params.getArticleId());

        Page<CommentResponse> pageInfo = new Page<>(params.getCurrent(), params.getSize());

        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
        articleQueryWrapper.eq(Article::getArticleId, params.getArticleId());
        Article article = articleMapper.selectOne(articleQueryWrapper);

        Page<CommentResponse> result = commentMapper.selectCommentPage(pageInfo, queryWrapper, article.getCreateUser());
        List<CommentTreeResponse> commentTree = buildCommentTree(result.getRecords());

        return Result.success(PageResult.<CommentTreeResponse>builder()
                .total(result.getTotal())
                .current(result.getCurrent())
                .size(result.getSize())
                .pages(result.getPages())
                .records(commentTree)
                .build());
    }

    private List<CommentTreeResponse> buildCommentTree(List<CommentResponse> flatList) {
        Map<Long, CommentTreeResponse> commentMap = new HashMap<>();
        Map<Long, List<CommentTreeResponse>> topLevelRepliesMap = new HashMap<>();
        List<CommentTreeResponse> topLevelComments = new ArrayList<>();

        for (CommentResponse dto : flatList) {
            CommentTreeResponse vo = convertToTreeData(dto);
            commentMap.put(vo.getId(), vo);

            if (vo.getParentId() == 0) {
                topLevelComments.add(vo);
                topLevelRepliesMap.put(vo.getId(), new ArrayList<>());
            }
        }

        for (CommentResponse dto : flatList) {
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

    private CommentTreeResponse convertToTreeData(CommentResponse dto) {
        CommentTreeResponse vo = new CommentTreeResponse();
        vo.setId(dto.getCommentId());
        vo.setArticleId(dto.getArticleId());
        vo.setUserId(dto.getCreateUser());
        vo.setContent(dto.getContent());
        vo.setParentId(dto.getParentId());
        vo.setCreateTime(dto.getCreateTime());

        vo.setNickName(dto.getNickName());
        vo.setUserAvatar(dto.getUserAvatar());
        vo.setIsOwn(dto.getIsOwn());
        vo.setReplyTo(dto.getReplyToUserName());

        return vo;
    }
}
