package top.heyqing.otter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OtterApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtterApplication.class, args);
    }
} 