package tech.niua.auth.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author wangzhen
 * @title: WebMvcRegistrationsConfig
 * @projectName niua_easy_parent
 * @description: TODO
 * @date 2020/11/20 上午10:36
 */
@Configuration
public class WebMvcRegistrationsConfig  implements WebMvcRegistrations {
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiRequestMappingHandlerMapping();
    }
}
