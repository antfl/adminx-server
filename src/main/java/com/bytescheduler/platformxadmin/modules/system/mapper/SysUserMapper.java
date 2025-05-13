package com.bytescheduler.platformxadmin.modules.system.mapper;

import com.bytescheduler.platformxadmin.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface SysUserMapper {
    SysUser selectByUsername(@Param("username") String username);

    void insertUser(SysUser user);

    void updateLoginInfo(@Param("userId") Long userId,
                         @Param("loginIp") String loginIp,
                         @Param("loginTime") Date loginTime);

}
