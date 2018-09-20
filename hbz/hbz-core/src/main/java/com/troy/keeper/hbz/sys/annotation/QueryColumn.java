package com.troy.keeper.hbz.sys.annotation;

import java.lang.annotation.*;

/**
 * Created by leecheng on 2017/9/14.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryColumn {

    String propName() default "";

    String queryOper() default "";
    
}
