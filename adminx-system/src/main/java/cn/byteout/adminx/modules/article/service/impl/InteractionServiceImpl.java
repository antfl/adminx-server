package cn.byteout.adminx.modules.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.context.UserContextHolder;
import cn.byteout.adminx.modules.article.entity.Interaction;
import cn.byteout.adminx.modules.article.mapper.InteractionMapper;
import cn.byteout.adminx.modules.article.service.ArticleService;
import cn.byteout.adminx.modules.article.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author antfl
 * @since 2025/6/21
 */
@RequiredArgsConstructor
@Service
public class InteractionServiceImpl extends ServiceImpl<InteractionMapper, Interaction> implements InteractionService {
    private final ArticleService articleService;

    @Override
    @Transactional
    public Result<String> toggleInteraction(Interaction interaction) {

        Long currentUserId = UserContextHolder.get();

        if (!"like".equals(interaction.getType()) && !"favorite".equals(interaction.getType())) {
            return Result.failed("无效的互动类型");
        }
        LambdaQueryWrapper<Interaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interaction::getCreateUser, currentUserId)
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
