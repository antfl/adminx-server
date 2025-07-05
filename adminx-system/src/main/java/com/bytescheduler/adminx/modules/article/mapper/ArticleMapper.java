package com.bytescheduler.adminx.modules.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.modules.article.dto.response.ArticleDetailResponse;
import com.bytescheduler.adminx.modules.article.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT a.article_id, a.title, a.content, a.create_time, a.status, a.like_count, " +
            "u.user_id, u.nickname, u.avatar, " +
            "c.category_name, " +
            "(SELECT COUNT(*) FROM interaction i WHERE i.article_id = a.article_id AND i.type = 'favorite') AS favorite_count, " +
            "(SELECT COUNT(*) FROM comment cmt WHERE cmt.article_id = a.article_id) AS comment_count, " +
            "(CASE WHEN EXISTS(SELECT 1 FROM interaction il WHERE il.article_id = a.article_id AND il.user_id = #{userId} AND il.type = 'like') THEN 1 ELSE 0 END) AS isLiked, " +
            "(CASE WHEN EXISTS(SELECT 1 FROM interaction fav_interaction WHERE fav_interaction.article_id = a.article_id AND fav_interaction.user_id = #{userId} AND fav_interaction.type = 'favorite') THEN 1 ELSE 0 END) AS isFavorite " +
            "FROM article a " +
            "LEFT JOIN sys_user u ON a.user_id = u.user_id " +
            "LEFT JOIN article_category c ON a.category_id = c.category_id " +
            "WHERE a.article_id = #{id}")
    ArticleDetailResponse selectArticleDetailById(@Param("id") Long id, @Param("userId") Long userId);
}
