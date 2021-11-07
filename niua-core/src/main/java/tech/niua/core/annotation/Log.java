package tech.niua.core.annotation;



import tech.niua.common.enums.BusinessType;
import tech.niua.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{

    public String value() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

}
