package com.bytescheduler.adminx.modules.article.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.modules.article.dto.response.CommentResponse;
import com.bytescheduler.adminx.modules.article.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.nickname AS nickname, u.avatar AS user_avatar, " +
            "IF(c.create_user = #{currentUserId}, 1, 0) AS is_own, " +
            "ru.nickname AS reply_to_user_name " +
            "FROM comment c " +
            "JOIN sys_user u ON c.create_user = u.user_id " +
            "LEFT JOIN sys_user ru ON c.reply_to_user_id = ru.user_id " +
            "${ew.customSqlSegment} " +
            "ORDER BY " +
            "   CASE WHEN c.parent_id = 0 THEN c.comment_id ELSE c.parent_id END, " +
            "   c.create_time ASC")
    Page<CommentResponse> selectCommentPage(
            Page<CommentResponse> page,
            @Param(Constants.WRAPPER) QueryWrapper<Comment> queryWrapper,
            @Param("currentUserId") Long currentUserId
    );
}
