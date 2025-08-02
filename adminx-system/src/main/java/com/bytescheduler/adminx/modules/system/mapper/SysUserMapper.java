package com.bytescheduler.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytescheduler.adminx.modules.system.dto.response.ActiveUserResponse;
import com.bytescheduler.adminx.modules.system.dto.response.RecentUserResponse;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectByUsername(@Param("username") String username);

    List<RecentUserResponse> selectRecentUsers(@Param("startTime") LocalDateTime startTime);

    List<ActiveUserResponse> selectActiveUsers(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("limit") int limit
    );
}
