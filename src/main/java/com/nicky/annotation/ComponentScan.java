package com.nicky.annotation;

import org.springframework.context.annotation.ComponentScans;

import java.lang.annotation.*;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/21 at 14:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {

    String[] basePackages() default {};
}
