package cn.byteout.adminx.web.filter;

import cn.byteout.adminx.web.wrapper.CachedBodyHttpServletRequest;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 缓存 HTTP 请求的 Body 数据，以便在后续处理中多次读取。
 *
 * @author antfl
 * @since 2025/8/1
 */
@Component
public class CachingRequestBodyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String method = request.getMethod().toUpperCase();
        String contentType = request.getContentType();

        if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
            if (contentType != null && contentType.contains("application/json")) {
                CachedBodyHttpServletRequest wrappedRequest =
                        new CachedBodyHttpServletRequest(request);
                filterChain.doFilter(wrappedRequest, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
