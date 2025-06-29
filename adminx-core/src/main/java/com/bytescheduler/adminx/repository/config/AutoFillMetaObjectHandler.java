package com.bytescheduler.adminx.repository.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bytescheduler.adminx.common.utils.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Component
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        Long userId = getCurrentUserId();
        this.strictInsertFill(metaObject, "userId", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        Long userId = getCurrentUserId();
        this.strictInsertFill(metaObject, "userId", Long.class, userId);
    }

    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }
}
