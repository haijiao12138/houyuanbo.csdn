package tech.niua.auth.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author Wangzhen
 * swagger相关配置类
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Autowired
    private ApplicationContext applicationContext;

    private Contact contact = new Contact("Wangzhen","localhost:8080/swagger-ui.html", "1053288979@qq.com");

    @Bean
    public Docket createRestApi() {
        ServletContext servletContext = applicationContext.getBean(ServletContext.class);
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(regex("/error.*")))
                .build()
                .apiInfo(apiInfo())
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getOperationPath(String operationPath) {
                        return operationPath.replaceAll("\\{version}", String.format("v%s", "1"));
                    }
                });
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("平台接口 v1.0")
                .description("平台接口")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
