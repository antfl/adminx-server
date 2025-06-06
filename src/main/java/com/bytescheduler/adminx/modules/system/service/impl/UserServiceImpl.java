package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.modules.system.dto.UserLoginRequest;
import com.bytescheduler.adminx.modules.system.dto.UserRegisterRequest;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public void register(UserRegisterRequest dto) {
        // 1. 修改用户名检查方式（使用 QueryWrapper）
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", dto.getUsername());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAvatar("https://default-avatar-url.png");

        userMapper.insert(user);
    }

    @Override
    public String login(UserLoginRequest dto) {
        SysUser user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus() == 1) {
            throw new BusinessException("账号已停用");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 更新登录信息
        userMapper.updateLoginInfo(user.getUserId(), getClientIP(), new Date());

        // 生成JWT
        return jwtTokenUtil.generateToken(user.getUsername());
    }

    private String getClientIP() {
        // 从RequestContextHolder获取IP
        return "127.0.0.1"; // 简化示例
    }
}
