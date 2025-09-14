package cn.byteout.adminx.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.article.entity.Interaction;

/**
 * @author antfl
 * @since 2025/6/21
 */
public interface InteractionService extends IService<Interaction> {

    Result<String> toggleInteraction(Interaction interaction);
}
