package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytescheduler.adminx.modules.system.dto.ArticleRequest;
import com.bytescheduler.adminx.modules.system.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT a.*, c.category_name, u.username AS author_name " +
            "FROM article a " +
            "JOIN article_category c ON a.category_id = c.category_id " +
            "JOIN sys_user u ON a.user_id = u.user_id " +
            "${ew.customSqlSegment}")
    Page<ArticleRequest> selectArticlePage(Page<ArticleRequest> page, @Param(Constants.WRAPPER) QueryWrapper<Article> queryWrapper);
}
