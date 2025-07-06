package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.modules.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/15
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入角色菜单关联关系
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    @Insert({
            "<script>",
            "INSERT INTO sys_role_menu (role_id, menu_id) VALUES ",
            "<foreach collection='menuIds' item='menuId' separator=','>",
            "(#{roleId}, #{menuId})",
            "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("roleId") Long roleId,
                     @Param("menuIds") List<Long> menuIds);

    /**
     * 根据角色 ID 列表查询菜单 ID 列表
     * @param roleIds 角色 ID
     * @return 菜单 ID 集合
     */
    @Select("<script>" +
            "SELECT DISTINCT menu_id FROM sys_role_menu " +
            "WHERE role_id IN " +
            "<foreach item='roleId' collection='roleIds' open='(' separator=',' close=')'>" +
            "   #{roleId}" +
            "</foreach>" +
            "</script>")
    List<Long> selectMenuIdsByRoleIds(@Param("roleIds") List<Long> roleIds);
}
