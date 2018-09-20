package com.troy.keeper.hbz.sys.annotation;

import java.lang.annotation.*;

/**
 * Created by leecheng on 2017/10/17.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Event {
}
