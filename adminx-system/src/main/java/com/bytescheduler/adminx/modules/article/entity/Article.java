package com.bytescheduler.adminx.modules.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章
 *
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
@TableName("article")
public class Article {

    /**
     * 文章 ID
     */
    @TableId(type = IdType.AUTO)
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章分类 ID
     */
    private Long categoryId;

    /**
     * 文章状态（0-仅自己查看，1-公开）
     */
    private Integer status;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer isDeleted;
}
