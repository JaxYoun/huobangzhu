package com.troy.keeper.hbz.sys.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by leecheng on 2017/7/10.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

    String use();

    String format();

    String msg();

    String conf() default "";

    int min() default -1;

    int max() default -1;

}
