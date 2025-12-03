package org.slavbx.productcatalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс, представляющий конфигурацию Swagger
 */
@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
@EnableWebMvc
@Import({SpringDocConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        org.springdoc.webmvc.ui.SwaggerConfig.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        })
public class OpenApiConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }

//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public")
//                .pathsToMatch("/api/**", "/productcatalog/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("admin")
//                .pathsToMatch("/admin/**")
//                .build();
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v3/api-docs/");
    }
}