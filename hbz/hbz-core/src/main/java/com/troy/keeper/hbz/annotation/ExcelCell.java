package com.troy.keeper.hbz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：YangJx
 * @Description：需要作为Excel单元格数据到处的字段
 * @DateTime：2017/12/14 16:34
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCell {

    /**
     * 字段名，需要与目标字段的名字一致
     *
     * @return
     */
    String name() default "";

    /**
     * excel中对应的表头名
     *
     * @return
     */
    String title();

    /**
     * 序号
     *
     * @return
     */
    byte sortNo() default 0;

}
