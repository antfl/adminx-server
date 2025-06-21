package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.domain.Result;
import com.bytescheduler.adminx.modules.system.entity.Interaction;
import com.bytescheduler.adminx.modules.system.mapper.InteractionMapper;
import com.bytescheduler.adminx.modules.system.service.ArticleService;
import com.bytescheduler.adminx.modules.system.service.InteractionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@AllArgsConstructor
@Service
public class InteractionServiceImpl extends ServiceImpl<InteractionMapper, Interaction> implements InteractionService {
    private final ArticleService articleService;

    @Override
    @Transactional
    public Result<?> toggleInteraction(Interaction interaction) {
        if (!"like".equals(interaction.getType()) && !"favorite".equals(interaction.getType())) {
            return Result.failed("无效的互动类型");
        }
        LambdaQueryWrapper<Interaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interaction::getUserId, interaction.getUserId())
                .eq(Interaction::getArticleId, interaction.getArticleId())
                .eq(Interaction::getType, interaction.getType());

        boolean existed = remove(queryWrapper);

        if (existed) {
            if ("like".equals(interaction.getType())) {
                articleService.decrementLikeCount(interaction.getArticleId());
            }
            return Result.success("取消" + getTypeName(interaction.getType()));
        } else {
            save(interaction);
            if ("like".equals(interaction.getType())) {
                articleService.incrementLikeCount(interaction.getArticleId());
            }
            return Result.success("添加" + getTypeName(interaction.getType()));
        }
    }

    private String getTypeName(String type) {
        return "like".equals(type) ? "点赞" : "收藏";
    }
}
