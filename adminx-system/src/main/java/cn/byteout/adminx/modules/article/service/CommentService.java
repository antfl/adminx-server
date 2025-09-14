package cn.byteout.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.article.dto.request.CommentCreateRequest;
import cn.byteout.adminx.modules.article.dto.request.CommentQueryRequest;
import cn.byteout.adminx.modules.article.dto.request.CommentUpdateRequest;
import cn.byteout.adminx.modules.article.dto.response.CommentTreeResponse;
import cn.byteout.adminx.modules.article.entity.Comment;

/**
 * @author antfl
 * @since 2025/6/21
 */
public interface CommentService extends IService<Comment> {

    boolean saveComment(CommentCreateRequest params);

    boolean deleteComment(Long id);

    boolean updateComment(CommentUpdateRequest params);

    Result<PageResult<CommentTreeResponse>> pageQuery(CommentQueryRequest params);
}
