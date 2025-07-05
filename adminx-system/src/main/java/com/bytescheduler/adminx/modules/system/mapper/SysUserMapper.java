package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectByUsername(@Param("username") String username);

    void insertUser(SysUser user);

    void updateLoginInfo(@Param("userId") Long userId,
                         @Param("loginIp") String loginIp,
                         @Param("loginTime") Date loginTime);

}
