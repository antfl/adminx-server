package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.modules.system.dto.CommentRequest;
import com.bytescheduler.adminx.modules.system.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.username AS user_name " +
            "FROM comment c " +
            "JOIN sys_user u ON c.user_id = u.user_id " +
            "${ew.customSqlSegment}")
    Page<CommentRequest> selectCommentPage(Page<CommentRequest> page, @Param(Constants.WRAPPER) QueryWrapper<Comment> queryWrapper);
}
