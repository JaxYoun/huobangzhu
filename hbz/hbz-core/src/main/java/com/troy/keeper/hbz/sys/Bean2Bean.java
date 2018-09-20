package com.troy.keeper.hbz.sys;

import com.troy.keeper.hbz.helper.PropertyMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/14.
 */
public class Bean2Bean {

    private List<PropertyMapper> mappers = new ArrayList<>();

    private List<String> excludeProps = new ArrayList<>();

    public Bean2Bean(PropertyMapper... mappers) {
        this.mappers.addAll(Arrays.asList(mappers));
    }

    public Bean2Bean addExcludeProp(String... props) {
        this.excludeProps.addAll(Arrays.asList(props));
        return this;
    }

    public Bean2Bean addPropMapper(PropertyMapper... propertyMapper) {
        this.mappers.addAll(Arrays.asList(propertyMapper));
        return this;
    }

    @SneakyThrows
    public void copyProperties(Object src, Object dest) {
        copyProperties(src, dest, false);
    }

    @SneakyThrows
    public void copyProperties(Object src, Object dest, boolean excludeNull) {
        List<String> nulls = new LinkedList<>();
        if (excludeNull) {
            Class<?> clz = src.getClass();
            List<String> fields = new ArrayList<>();
            while (clz != Object.class) {
                Field[] fs = clz.getDeclaredFields();
                if (fs != null) {
                    for (Field field : fs) {
                        if (!field.isAccessible()) field.setAccessible(true);
                        if (field.get(src) == null) fields.add(field.getName());
                    }
                }
                clz = clz.getSuperclass();
            }
            nulls.addAll(fields);
        }

        List<String> excludes = new ArrayList<>();
        excludes.addAll(mappers.stream().map(PropertyMapper::getName).collect(Collectors.toList()));
        excludes.addAll(excludeProps);
        excludes.addAll(nulls);
        BeanUtils.copyProperties(src, dest, excludes.toArray(new String[excludes.size()]));
        for (PropertyMapper propertyMapper : mappers) {
            String propName = propertyMapper.getName();
            String getterName = "get" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
            Method getter = src.getClass().getMethod(getterName);
            Object val = getter.invoke(src);
            if (excludeNull) if (val == null) continue;
            String setterName = "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
            for (Method setter : dest.getClass().getMethods()) {
                if (setter.getName().equals(setterName) && setter.getParameterCount() == 1) {
                    setter.invoke(dest, new Object[]{propertyMapper.map(val)});
                }
            }
        }
    }

}
