package com.bytescheduler.adminx.modules.system.service.impl;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.common.utils.MailUtil;
import com.bytescheduler.adminx.modules.system.dto.request.LoginRequest;
import com.bytescheduler.adminx.modules.system.dto.request.PasswordResetRequest;
import com.bytescheduler.adminx.modules.system.dto.request.RegisterRequest;
import com.bytescheduler.adminx.modules.system.dto.response.CaptchaResponse;
import com.bytescheduler.adminx.modules.system.dto.response.MailCodeResponse;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.SysUser;
import com.bytescheduler.adminx.modules.system.mapper.SysUserMapper;
import com.bytescheduler.adminx.modules.system.service.AuthService;
import com.bytescheduler.adminx.repository.config.CaptchaConfig;
import com.bytescheduler.adminx.repository.config.EmailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;
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
    private final EmailConfig emailConfig;
    private final MailUtil mailUtil;

    @Override
    @Transactional
    public void register(RegisterRequest params) {
        String email = params.getEmail();
        String captchaId = email + emailConfig.getKeyHead() + params.getCaptchaId();

        // 判断是否走邮箱验证
        if (emailConfig.isVerificationEnabled()) {
            String rds_code = redisTemplate.opsForValue().get(captchaId);
            String code = params.getCode();

            if (emailConfig.isIgnoreCase()) {
                // 忽略大小写比较
                if (rds_code == null || !rds_code.equalsIgnoreCase(code)) {
                    throw new BusinessException("验证码错误");
                }
            } else {
                // 严格区分大小写
                if (rds_code == null || !rds_code.equals(code)) {
                    throw new BusinessException("验证码错误");
                }
            }
        }

        LambdaQueryWrapper<SysUser> queryUserEmail = new LambdaQueryWrapper<>();
        queryUserEmail.eq(SysUser::getEmail, params.getEmail());
        if (userMapper.selectCount(queryUserEmail) > 0) {
            throw new BusinessException("用户已存在");
        }

        LambdaQueryWrapper<SysUser> queryUserNickname = new LambdaQueryWrapper<>();
        queryUserNickname.eq(SysUser::getNickname, params.getNickname());
        if (userMapper.selectCount(queryUserNickname) > 0) {
            throw new BusinessException("该昵称已被使用");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(params, user);
        user.setPassword(passwordEncoder.encode(params.getPassword()));
        user.setAvatar("BOY_AVATAR_A");
        redisTemplate.delete(captchaId);
        userMapper.insert(user);
    }

    @Override
    public TokenResponse login(LoginRequest params) {
        String identifier = params.getUsername().trim();
        String captchaId = captchaConfig.getKeyHead() + params.getCaptchaId();

        // 获取 Redis 中验证码
        String rds_code = redisTemplate.opsForValue().get(captchaId);
        if (rds_code == null) {
            throw new BusinessException("验证码不存在或已过期");
        }

        // 处理忽略大小写
        String code = params.getCode();
        if (captchaConfig.isIgnoreCase()) {
            if (code != null) {
                code = code.toLowerCase();
            }
            rds_code = rds_code.toLowerCase();
        }

        if (!Objects.equals(rds_code, code)) {
            throw new BusinessException("验证码错误");
        }

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .eq("username", identifier)
                .or()
                .eq("email", identifier)
        );

        SysUser user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(params.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        redisTemplate.delete(captchaId);

        String userId = user.getUserId().toString();
        String token = jwtTokenUtil.generateToken(userId, user.getUserId());

        redisTemplate.opsForValue().set(
                userId,
                token,
                jwtTokenUtil.getExpirationFromToken(token),
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse(token);
    }

    @Override
    public void passwordReset(PasswordResetRequest params) {

        if(!Objects.equals(params.getPassword(), params.getConfirmPassword())) {
            throw new BusinessException("确认密码与设置的密码不一致");
        }

        String email = params.getEmail();
        String captchaId = email + emailConfig.getKeyHead() + params.getCaptchaId();

        String rdsCode = redisTemplate.opsForValue().get(captchaId);
        String code = params.getCode();

        // 校验验证码
        if (rdsCode == null || !rdsCode.equalsIgnoreCase(code)) {
            throw new BusinessException("验证码错误");
        }

        LambdaQueryWrapper<SysUser> queryUserEmail = new LambdaQueryWrapper<>();
        queryUserEmail.eq(SysUser::getEmail, params.getEmail());
        SysUser sysUser = userMapper.selectOne(queryUserEmail);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, sysUser.getUserId());
        String newPassword = passwordEncoder.encode(params.getPassword());
        // 重置密码
        updateWrapper.set(StringUtils.isNotBlank(newPassword), SysUser::getPassword, newPassword);
        userMapper.update(null, updateWrapper);
        // 清除当前 TOKEN
        redisTemplate.delete(sysUser.getUserId() + "");
        redisTemplate.delete(captchaId);
    }

    @Override
    public CaptchaResponse generateCaptcha() {
        RandomGenerator numberGenerator = new RandomGenerator("0123456789", captchaConfig.getCodeCount());

        // 生成验证码（宽、高、验证码、干扰线数）
        LineCaptcha captcha = new LineCaptcha(
                captchaConfig.getWidth(),
                captchaConfig.getHeight(),
                numberGenerator,
                captchaConfig.getInterferenceCount()
        );

        // 验证码文本
        String code = captcha.getCode();

        // 验证码唯一 ID
        String captchaId = UUID.randomUUID().toString();

        // 存入 Redis（60 秒过期）
        redisTemplate.opsForValue().set(
                captchaConfig.getKeyHead() + captchaId,
                code,
                Duration.ofSeconds(captchaConfig.getExpireSeconds())
        );

        // 返回 Base64 图片 + ID
        String base64 = captcha.getImageBase64Data();
        return new CaptchaResponse(captchaId, base64);
    }

    @Override
    public MailCodeResponse generateMailCode(String email, String type) {
        if (Objects.equals(type, "REGISTER")) {
            // 如果已经注册的邮箱不发验证码
            QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("email", email);
            SysUser sysUser = userMapper.selectOne(userQueryWrapper);
            if (sysUser != null) {
                throw new BusinessException("该用户已注册");
            }
        }

        // 生成验证码
        String code = mailUtil.generateComplexCode(emailConfig.getCodeCount());

        // 验证码唯一 ID
        String captchaId = UUID.randomUUID().toString();

        // 存入 Redis
        redisTemplate.opsForValue().set(
                email + emailConfig.getKeyHead() + captchaId,
                code,
                Duration.ofSeconds(emailConfig.getExpireSeconds())
        );
        mailUtil.sendVerifyCode(email, code);

        // 返回 Code ID
        return new MailCodeResponse(captchaId);
    }
}
