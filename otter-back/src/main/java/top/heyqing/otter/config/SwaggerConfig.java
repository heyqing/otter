package top.heyqing.otter.config;

import com.google.common.base.Predicates;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@SpringBootConfiguration
@Log4j2
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();

    }

    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .title("Otter Test Platform")
                .description("用于 Otter 平台测试")
                .version("v1.0.0")
                .contact(new Contact("heyqing", "http://heyqing.top", "heyqings@163.com"))
                .build();
    }

}
