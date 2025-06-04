package com.bytescheduler.platformxadmin.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.platformxadmin.modules.system.entity.Menu;
import org.apache.ibatis.annotations.*;

/**
 * @author byte-scheduler
 * @date 2025/5/14 21:05
 * @description
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    @Update("UPDATE sys_menu SET " +
            "parent_id = #{parentId}, " +
            "menu_name = #{menuName}, " +
            "menu_type = #{menuType}, " +
            "path = #{path}, " +
            "component = #{component}, " +
            "perms = #{perms}, " +
            "icon = #{icon}, " +
            "sort = #{sort}, " +
            "status = #{status}, " +
            "is_external = #{isExternal}, " +
            "is_cache = #{isCache}, " +
            "is_visible = #{isVisible} " +
            "WHERE menu_id = #{menuId}")
    void update(Menu entity);

    @Select("SELECT COUNT(1) FROM sys_menu WHERE parent_id = #{parentId}")
    boolean existsByParentId(@Param("parentId") Long parentId);
}
