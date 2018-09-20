package com.troy.keeper.hbz.sys.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by leecheng on 2017/7/7.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface ValueFormat {
    Validation[] validations() default {};
}
