package com.wkyc.monitor.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface BeanValidate {

    /** 检验分组 */
    Class<?>[] groups() default {};
}