package com.troy.keeper.hbz.helper;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/13.
 */
public class BeanHelper {

    public static final Map<String, List<String>> cache = new HashMap<>();

    @SneakyThrows
    public static void copyProperties(Object src, Object dest, PropertyMapper... mappers) {
        List<String> excludes = Arrays.asList(mappers).stream().map(PropertyMapper::getName).collect(Collectors.toList());
        BeanUtils.copyProperties(src, dest, excludes.toArray(new String[excludes.size()]));
        for (PropertyMapper propertyMapper : mappers) {
            String propName = propertyMapper.getName();
            String getterName = "get" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
            Method getter = src.getClass().getMethod(getterName);
            Object val = getter.invoke(src);
            String setterName = "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
            for (Method setter : dest.getClass().getMethods()) {
                if (setter.getName().equals(setterName) && setter.getParameterCount() == 1) {
                    setter.invoke(dest, new Object[]{propertyMapper.map(val)});
                }
            }
        }
    }

    @SneakyThrows
    public static List<String> getFieldsByIncExcludeAt(Class<?> beanClass, Class<?>[] includeAt, Class<?>[] excludeAt) {
        String mainClassName = beanClass.getName();
        String includeClassNames = Arrays.asList(includeAt).stream().map(Class::getName).sorted().reduce((a, b) -> a + "," + b).get();
        String excludeClassNames = Arrays.asList(excludeAt).stream().map(Class::getName).sorted().reduce((a, b) -> a + "," + b).get();
        String cacheKey = mainClassName + "," + includeClassNames + "," + excludeClassNames;

        List<String> cacheObject = cache.get(cacheKey);
        if (cacheObject != null) return cacheObject;

        List<Field> fields = new LinkedList<>();
        Class<?> cc = beanClass;
        while (cc != Object.class) {
            fields.addAll(Arrays.asList(cc.getDeclaredFields()));
            cc = cc.getSuperclass();
        }
        LinkedList<String> finds = new LinkedList<>();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            String fieldName = field.getName();
            boolean isExclude = false;
            boolean isInclude = false;
            for (Annotation annotation : annotations) {
                for (Class<?> cls : excludeAt) {
                    if (annotation.annotationType() == cls) {
                        isExclude = true;
                    }
                }
                for (Class<?> cls : includeAt) {
                    if (annotation.annotationType() == cls) {
                        isInclude = true;
                    }
                }
            }
            if (!isExclude && isInclude) {
                finds.add(fieldName);
            }
        }
        cache.put(cacheKey, finds.stream().distinct().collect(Collectors.toList()));
        return cache.get(cacheKey);
    }

}
