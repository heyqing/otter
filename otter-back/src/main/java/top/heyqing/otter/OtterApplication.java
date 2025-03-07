package top.heyqing.otter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.heyqing.otter.config.LogoConfig;

/**
 * ClassName:OtterApplication
 * Package:top.heyqing.otter
 * Description:
 * 项目启动类
 *
 * @Date:2025/3/7
 * @Author:Heyqing
 */
@SpringBootApplication
public class OtterApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtterApplication.class, args);
        LogoConfig.v();
    }
}
