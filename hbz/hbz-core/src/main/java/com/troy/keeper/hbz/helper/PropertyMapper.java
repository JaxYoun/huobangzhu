package com.troy.keeper.hbz.helper;

import java.util.function.Function;

/**
 * Created by leecheng on 2017/11/13.
 */
public class PropertyMapper<T, R> {

    private Function<T, R> function;

    private String name;

    public PropertyMapper(String name, Function<T, R> function) {
        this.name = name;
        this.function = function;
    }

    public R map(T t) {
        if (t == null) return null;
        return function.apply(t);
    }

    public String getName() {
        return this.name;
    }

}
