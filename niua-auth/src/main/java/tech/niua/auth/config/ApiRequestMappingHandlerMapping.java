package tech.niua.auth.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tech.niua.core.annotation.ApiVersion;

import java.lang.reflect.Method;

/**
 * @author wangzhen
 * @title: ApiRequestMappingHandlerMapping
 * @projectName niua_easy_parent
 * @description: 定义版本控制规则
 * @date 2020/11/20 上午10:37
 */
public class ApiRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private static final String VERSION_FLAG = "{version}";

    private static RequestCondition<ApiVersionCondition> createCondition(Class<?> clazz) {
        RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
        if (classRequestMapping == null) {
            return null;
        }
        StringBuilder mappingUrlBuilder = new StringBuilder();
        if (classRequestMapping.value().length > 0) {
            mappingUrlBuilder.append(classRequestMapping.value()[0]);
        }
        String mappingUrl = mappingUrlBuilder.toString();
        if (!mappingUrl.contains(VERSION_FLAG)) {
            return null;
        }
        ApiVersion apiVersion = clazz.getAnnotation(ApiVersion.class);
        return apiVersion == null ? new ApiVersionCondition(1) : new ApiVersionCondition(apiVersion.value());
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return createCondition(method.getClass());
    }

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return createCondition(handlerType);
    }
}
