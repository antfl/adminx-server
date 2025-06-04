package com.bytescheduler.platformxadmin.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.platformxadmin.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND is_deleted = 0")
    SysUser selectByUsername(@Param("username") String username);

    @Update("UPDATE sys_user SET last_login_ip = #{loginIp}, last_login_time = #{loginTime} WHERE user_id = #{userId}")
    void updateLoginInfo(@Param("userId") Long userId,
                         @Param("loginIp") String loginIp,
                         @Param("loginTime") Date loginTime);

}
