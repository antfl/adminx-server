package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.modules.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface SysUserMapper extends BaseMapper<User> {
    User selectByUsername(@Param("username") String username);

    void insertUser(User user);

    void updateLoginInfo(@Param("userId") Long userId,
                         @Param("loginIp") String loginIp,
                         @Param("loginTime") Date loginTime);

}
