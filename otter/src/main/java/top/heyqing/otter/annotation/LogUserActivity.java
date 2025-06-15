package top.heyqing.otter.annotation;

import java.lang.annotation.*;

/**
 * 用户操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogUserActivity {
    /**
     * 操作描述
     */
    String value() default "";
} 