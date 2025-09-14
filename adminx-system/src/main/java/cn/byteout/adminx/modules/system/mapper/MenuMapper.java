package cn.byteout.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.byteout.adminx.modules.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {

    // 根据菜单 ID 列表查询有效菜单
    @Select("<script>" +
            "SELECT * FROM sys_menu " +
            "WHERE id IN " +
            "<foreach item='menuId' collection='menuIds' open='(' separator=',' close=')'>" +
            "   #{menuId}" +
            "</foreach>" +
            " AND status = 1 " +
            " AND is_deleted = 0 " +
            " AND menu_type IN (0, 1) " + // 只查询目录和菜单类型
            " ORDER BY sort_order ASC" +
            "</script>")
    List<SysMenu> selectMenusByIds(@Param("menuIds") List<Long> menuIds);

    // 查询按钮权限标识
    @Select("<script>" +
            "SELECT DISTINCT permission FROM sys_menu " +
            "WHERE id IN " +
            "<foreach item='menuId' collection='menuIds' open='(' separator=',' close=')'>" +
            "   #{menuId}" +
            "</foreach>" +
            " AND status = 1 " +
            " AND is_deleted = 0 " +
            " AND menu_type = 2 " + // 按钮类型
            " AND permission IS NOT NULL" +
            "</script>")
    List<String> selectPermissionsByIds(@Param("menuIds") List<Long> menuIds);
}