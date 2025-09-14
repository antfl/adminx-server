package cn.byteout.adminx.modules.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章分类
 *
 * @author antfl
 * @since 2025/6/21
 */
@Data
@TableName("article_category")
public class ArticleCategory {

    /**
     * 文章分类 ID
     */
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

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
