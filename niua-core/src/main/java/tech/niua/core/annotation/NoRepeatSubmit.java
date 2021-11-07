package tech.niua.core.annotation;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * @功能描述 防止重复提交标记注解
 * @author 王震
 * @date 2020
 */
public @interface NoRepeatSubmit {
}
