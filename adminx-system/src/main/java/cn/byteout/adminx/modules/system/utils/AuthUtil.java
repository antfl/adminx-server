package cn.byteout.adminx.modules.system.utils;

import cn.byteout.adminx.common.utils.security.JwtTokenUtil;
import cn.byteout.adminx.context.UserContextHolder;
import cn.byteout.adminx.modules.system.dto.response.TokenResponse;
import cn.byteout.adminx.modules.system.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author antfl
 * @since 2025/8/13
 */
@RequiredArgsConstructor
@Component
public class AuthUtil {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 生成并存储用户 TOKEN
     */
    public TokenResponse generateToken(SysUser user) {
        String userId = user.getUserId().toString();
        String token = jwtTokenUtil.generateToken(userId, user.getUserId());

        redisTemplate.opsForValue().set(
                userId,
                token,
                jwtTokenUtil.getExpirationFromToken(token),
                TimeUnit.MILLISECONDS
        );

        return new TokenResponse(token, null);
    }

    /**
     * 退出登录
     */
    public void logout() {
        Long userId = UserContextHolder.get();
        if (userId != null) {
            redisTemplate.delete(String.valueOf(userId));
        }
        UserContextHolder.clear();
    }
}
