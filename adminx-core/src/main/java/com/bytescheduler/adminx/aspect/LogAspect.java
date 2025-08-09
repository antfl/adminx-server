package com.bytescheduler.adminx.aspect;

import com.bytescheduler.adminx.annotation.Log;
import com.bytescheduler.adminx.common.entity.SysOperationLog;
import com.bytescheduler.adminx.repository.service.OperationLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面
 *
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Aspect
@Component
public class LogAspect {

    private final OperationLogService logService;

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper, OperationLogService logService) {
        this.objectMapper = objectMapper;
        this.logService = logService;
    }

    @Pointcut("@annotation(com.bytescheduler.adminx.annotation.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        SysOperationLog log = new SysOperationLog();
        long beginTime = System.currentTimeMillis();
        Object result = null;
        Throwable exception = null;

        try {
            result = point.proceed();
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - beginTime;
            saveLog(point, duration, result, exception, log);
        }

        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long duration, Object result, Throwable exception, SysOperationLog log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.setDuration(duration);
        log.setOperationTime(LocalDateTime.now());

        // 从注解获取操作信息
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            log.setModule(logAnnotation.module());
            log.setType(logAnnotation.type().getValue());
            log.setDescription(logAnnotation.value());
        }

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.setRequestMethod(request.getMethod());
            log.setIp(getIpAddress(request));
        }

        // 序列化请求参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            try {
                log.setParams(objectMapper.writeValueAsString(args));
            } catch (Exception e) {
                log.setParams("参数序列化失败: " + e.getMessage());
            }
        }

        // 处理操作结果
        if (exception != null) {
            // 失败
            log.setStatus(0);
            log.setErrorMsg(exception.getMessage());
        } else {
            // 成功
            log.setStatus(1);
            if (result != null) {
                try {
                    // 限制结果大小，避免存储过大对象
                    String resultJson = objectMapper.writeValueAsString(result);
                    if (resultJson.length() > 2000) {
                        resultJson = resultJson.substring(0, 2000) + "...[TRUNCATED]";
                    }
                    log.setResult(resultJson);
                } catch (Exception e) {
                    log.setResult("结果序列化失败: " + e.getMessage());
                }
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.setOperator(authentication.getPrincipal().toString());

        logService.saveLog(log);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
