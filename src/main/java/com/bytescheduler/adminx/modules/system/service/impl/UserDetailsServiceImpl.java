package com.bytescheduler.adminx.modules.system.service.impl;

import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    public UserDetailsServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询数据库中的用户
        com.bytescheduler.adminx.modules.system.entity.User sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 2. 检查账号状态
        if (sysUser.getStatus() == 1) {
            throw new UsernameNotFoundException("账号已停用");
        }

        // 3. 转换权限（根据你的实际权限字段调整）
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER") // 示例权限，需从数据库读取实际权限
        );

        // 4. 构建 Spring Security 的 UserDetails 对象
        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(),
                authorities
        );
    }
}