package tech.niua.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangzhen
 * @title: ApiVersion
 * @projectName niua_easy_parent
 * @description: 自定义版本控制注解
 * @date 2020/11/20 上午10:17
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    /**
     * @return 版本号
     */
    int value() default 1;
}
