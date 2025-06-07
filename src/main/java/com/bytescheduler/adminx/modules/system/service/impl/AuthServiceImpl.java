package com.bytescheduler.adminx.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.modules.system.dto.LoginRequest;
import com.bytescheduler.adminx.modules.system.dto.RegisterRequest;
import com.bytescheduler.adminx.modules.system.dto.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.User;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 1. 修改用户名检查方式（使用 QueryWrapper）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", request.getUsername());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setNickname(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatar("https://default-avatar-url.png");

        userMapper.insert(user);
    }

    @Override
    public TokenResponse login(LoginRequest request) {

        String username = request.getUsername();
        String token = jwtTokenUtil.generateToken(username);

        redisTemplate.opsForValue().set(
                username,
                token,
                jwtTokenUtil.getExpirationFromToken(token),
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse(token);
    }
}
