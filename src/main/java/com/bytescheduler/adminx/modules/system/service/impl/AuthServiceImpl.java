package com.bytescheduler.adminx.modules.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.modules.system.dto.CaptchaResponse;
import com.bytescheduler.adminx.modules.system.dto.LoginRequest;
import com.bytescheduler.adminx.modules.system.dto.RegisterRequest;
import com.bytescheduler.adminx.modules.system.dto.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.User;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytescheduler.adminx.security.config.CaptchaConfig;

import java.time.Duration;
import java.util.UUID;
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
    private final CaptchaConfig captchaConfig;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", request.getEmail());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getEmail());
        BeanUtils.copyProperties(request, user);
        user.setNickname(request.getNickname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatar("");

        userMapper.insert(user);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        String identifier = request.getUsername().trim();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .eq("username", identifier)
                .or()
                .eq("email", identifier)
        );
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String username = user.getUsername();
        String token = jwtTokenUtil.generateToken(username, user.getUserId());

        redisTemplate.opsForValue().set(
                username,
                token,
                jwtTokenUtil.getExpirationFromToken(token),
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse(token);
    }


    @Override
    public CaptchaResponse generateCaptcha() {
        // 生成验证码（宽、高、字符数、干扰线数）
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(
                captchaConfig.getWidth(),
                captchaConfig.getHeight(),
                captchaConfig.getCodeCount(),
                captchaConfig.getInterferenceCount()
        );

        // 验证码文本
        String code = captcha.getCode();

        // 验证码唯一 ID
        String captchaId = UUID.randomUUID().toString();

        // 存入 Redis（60 秒过期）
        redisTemplate.opsForValue().set(
                "captcha:" + captchaId,
                code,
                Duration.ofSeconds(captchaConfig.getExpireSeconds())
        );

        // 返回 Base64 图片 + ID
        String base64 = captcha.getImageBase64Data();
        return new CaptchaResponse(captchaId, base64);
    }
}
