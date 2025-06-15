package top.heyqing.otter.interceptor;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.heyqing.otter.annotation.RateLimit;
import top.heyqing.otter.common.Result;
import top.heyqing.otter.exception.BusinessException;

import java.io.IOException;
import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket bucket;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof org.springframework.web.method.HandlerMethod)) {
            return true;
        }

        org.springframework.web.method.HandlerMethod handlerMethod = (org.springframework.web.method.HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        if (rateLimit != null) {
            if (!bucket.tryConsume(1)) {
                handleLimitExceeded(response);
                return false;
            }
        }

        return true;
    }

    private void handleLimitExceeded(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(Result.error(429, "请求过于频繁，请稍后再试").toString());
    }
} 