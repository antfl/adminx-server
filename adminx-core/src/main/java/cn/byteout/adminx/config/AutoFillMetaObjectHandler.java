package cn.byteout.adminx.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import cn.byteout.adminx.context.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author antfl
 * @since 2025/6/8
 */
@Component
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        Long userId = UserContextHolder.get();
        this.strictInsertFill(metaObject, "createUser", Long.class, userId);
        this.strictInsertFill(metaObject, "updateUser", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        Long userId = UserContextHolder.get();
        this.strictInsertFill(metaObject, "updateUser", Long.class, userId);
    }
}
