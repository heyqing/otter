package top.heyqing.otter.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.heyqing.otter.entity.User;
import top.heyqing.otter.entity.UserActivityLog;
import top.heyqing.otter.repository.UserActivityLogRepository;
import top.heyqing.otter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户操作日志切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class UserActivityLogAspect {
    
    private final UserActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    
    @Around("@annotation(top.heyqing.otter.annotation.LogUserActivity)")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return joinPoint.proceed();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return joinPoint.proceed();
        }
        
        User user = userOpt.get();
        
        // 创建日志记录
        UserActivityLog log = new UserActivityLog();
        log.setUser(user);
        log.setAction(UserActivityLog.UserAction.valueOf(joinPoint.getSignature().getName().toUpperCase()));
        log.setIpAddress(getClientIp(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setDetails(createLogDetails(joinPoint));
        
        // 保存日志
        activityLogRepository.save(log);
        
        // 继续执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 创建日志详情
     */
    private String createLogDetails(ProceedingJoinPoint joinPoint) {
        StringBuilder details = new StringBuilder();
        details.append("Method: ").append(joinPoint.getSignature().getName()).append("\n");
        details.append("Args: ");
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                details.append(", ");
            }
            details.append(args[i]);
        }
        return details.toString();
    }
} 