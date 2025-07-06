package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.modules.system.entity.SysUserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/6/16
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<SysUserRole> {

    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    @Insert({
            "<script>",
            "INSERT INTO sys_user_role (user_id, role_id) VALUES ",
            "<foreach collection='roleIds' item='roleId' separator=','>",
            "(#{userId}, #{roleId})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    // 根据用户 ID 查询角色 ID 列表
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
