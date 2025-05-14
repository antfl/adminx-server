package com.bytescheduler.platformxadmin.modules.system.mapper;

import com.bytescheduler.platformxadmin.modules.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author byte-scheduler
 * @date 2025/5/14 21:05
 * @description
 */
@Mapper
public interface MenuMapper {
    void insert(Menu entity);
    void update(Menu entity);
    void deleteById(@Param("menuId") Long menuId);
    Menu selectById(@Param("menuId") Long menuId);
    List<Menu> selectAll();
    boolean existsByParentId(@Param("parentId") Long parentId);
}
