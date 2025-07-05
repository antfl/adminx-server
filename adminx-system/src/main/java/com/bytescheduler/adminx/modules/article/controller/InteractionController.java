package com.bytescheduler.adminx.modules.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.modules.article.entity.Interaction;
import com.bytescheduler.adminx.modules.article.service.InteractionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Api(tags = "文章互动")
@AllArgsConstructor
@RestController
@RequestMapping("/interaction")
public class InteractionController {
    private final InteractionService interactionService;

    @ApiOperation("切换用户对文章的互动状态（点赞/收藏")
    @PostMapping("/toggle")
    public Result<String> toggleInteraction(@RequestBody Interaction interaction) {
        return interactionService.toggleInteraction(interaction);
    }

    @ApiOperation("查询用户的互动记录")
    @GetMapping("/user")
    public Result<List<Interaction>> getUserInteractions(
            @RequestParam Long userId,
            @RequestParam(required = false) String type) {
        LambdaQueryWrapper<Interaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interaction::getCreateUser, userId);

        if (StringUtils.isNotBlank(type)) {
            queryWrapper.eq(Interaction::getType, type);
        }

        return Result.success(interactionService.list(queryWrapper));
    }
}
