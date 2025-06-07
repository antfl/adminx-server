package com.bytescheduler.adminx.security.filter;

import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.security.config.JwtConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, JwtTokenUtil jwtTokenUtil, RedisTemplate<String, String> redisTemplate) {
        this.jwtConfig = jwtConfig;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(jwtConfig.getTokenHeader());
        if (header != null && header.startsWith(jwtConfig.getTokenPrefix())) {
            String authToken = header.substring(jwtConfig.getTokenPrefix().length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 验证Redis中的Token是否一致
                String redisToken = redisTemplate.opsForValue().get(username);
                if (authToken.equals(redisToken) && jwtTokenUtil.validateToken(authToken)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
