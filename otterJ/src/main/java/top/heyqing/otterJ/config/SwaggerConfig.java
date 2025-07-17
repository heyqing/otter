package top.heyqing.otterJ.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置
 * 访问地址: /swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI otterOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Otter 智测链 API文档")
                        .description("自动化测试、区块链存证、风险可视化后端API文档")
                        .version("v1.0.0")
                        .contact(new Contact().name("Otter Team").email("support@otter.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目文档")
                        .url("https://github.com/heyqing/otterJ"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("otter-public")
                .pathsToMatch("/api/**")
                .build();
    }
} 