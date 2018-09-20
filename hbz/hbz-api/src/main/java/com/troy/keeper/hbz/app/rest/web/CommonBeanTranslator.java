package com.troy.keeper.hbz.app.rest.web;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：Bean转换公共类
 * @DateTime：2017/12/1 14:55
 */
@Slf4j
public class CommonBeanTranslator {

    @SneakyThrows
    public static void beanToBean(Object source, Object target, PropertyMachine... propertyMachineArr) {
        List<String> ignorePropertyNameList = Arrays.asList(propertyMachineArr).stream().map(PropertyMachine::getName).collect(Collectors.toList());
        BeanUtils.copyProperties(source, target, ignorePropertyNameList.toArray(new String[ignorePropertyNameList.size()]));

        for (PropertyMachine machine : propertyMachineArr) {
            String propName = machine.getName();

            String getterName = "get" + propName.substring(0, 1).toUpperCase() + propName.substring(1);  //根据属性名，拼接对应的getter
            Method getter = source.getClass().getMethod(getterName);  //依据反射，用拼接得到getter名从源对象获取getter方法
            Object val = getter.invoke(source);  //执行得到的getter方法获得源对象对应的属性值

            String setterName = "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);  //根据属性名，拼接对应的getter
            for (Method setter : target.getClass().getMethods()) {  //遍历目标对象方法集合，用拼接得到getter名从目标对象获取getter方法
                if (setter.getName().equals(setterName) && setter.getParameterCount() == 1) {  //一一匹配
                    setter.invoke(target, new Object[]{machine.translate(val)});  //如果找到了匹配的，就调用该setter方法将源的属性值放进去
                }
            }
        }

    }
}

/**
 * 属性名及其转换器组合
 *
 * @param <S>
 * @param <T>
 */
class PropertyMachine<S, T> {

    /**
     * 属性转换函数
     */
    private Function<S, T> translator;

    /**
     * 属性名
     */
    private String name;

    /**
     * 构造函数
     *
     * @param translator
     * @param name
     */
    public PropertyMachine(Function<S, T> translator, String name) {
        this.translator = translator;
        this.name = name;
    }

    /**
     * 执行转换
     *
     * @param source
     * @return
     */
    public T translate(S source) {
        if (source == null) return null;
        return translator.apply(source);
    }

    public String getName() {
        return name;
    }
}

/**
 *
 */
class BeanToMap {

    private List<PropertyMachine> propertyMachineList = new LinkedList<>();

    private List<String> ignorePropertyNameList = new LinkedList<>();

    /**
     * 构造函数
     *
     * @param propertyMachineArr
     */
    public BeanToMap(PropertyMachine... propertyMachineArr) {
        this.propertyMachineList.addAll(Arrays.asList(propertyMachineArr));
    }

    /**
     * 添加忽略字段名
     *
     * @param otherPropertyNameArr
     * @return
     */
    public BeanToMap addIgnores(String... otherPropertyNameArr) {
        this.ignorePropertyNameList.addAll(Arrays.asList(otherPropertyNameArr));
        return this;
    }

    /**
     * 添加属性机
     *
     * @param otherPropertyMachineArr
     * @return
     */
    public BeanToMap addPropMapper(PropertyMachine... otherPropertyMachineArr) {
        this.propertyMachineList.addAll(Arrays.asList(otherPropertyMachineArr));
        return this;
    }

    /**
     * 将对象转换为Map
     *
     * @param bean
     * @return
     */
    @SneakyThrows
    public Map<String, Object> translateBeanToMap(Object bean) {
        if (bean == null) {
            return null;
        }
        List<String> excludeProperties = propertyMachineList.stream().map(PropertyMachine::getName).collect(Collectors.toList());
        Map<String, PropertyMachine> mapperMap = new LinkedHashMap<>();
        if (propertyMachineList.size() >= 1) {
            mapperMap = propertyMachineList.stream().map(machine -> {
                Map<String, PropertyMachine> map = new HashMap<>();
                map.put(machine.getName(), machine);
                return map;
            }).reduce((m1, m2) -> {
                m1.putAll(m2);
                return m1;
            }).get();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Method> excludeGetters = new LinkedHashMap<>();
        Class clazz = bean.getClass();

        for (Method getter : clazz.getMethods()) {
            if (getter.getName().startsWith("get") && getter.getParameterCount() == 0) {
                String getterName = getter.getName();
                String propName = getterName.substring(3);
                if (propName.substring(0, 1).equals(propName.substring(0, 1).toUpperCase())) {
                    propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
                }
                if (ignorePropertyNameList.contains(propName)) continue;
                if (excludeProperties.contains(propName)) {
                    excludeGetters.put(propName, getter);
                    continue;
                } else {
                    Object val = getter.invoke(bean);
                    if (val == null) {
                        continue;
                    } else {
                        result.put(propName, val);
                    }
                }
            }
        }
        for (Map.Entry<String, Method> getterEnt : excludeGetters.entrySet()) {
            String propName = getterEnt.getKey();
            Method getter = getterEnt.getValue();
            PropertyMachine mapper = mapperMap.get(propName);
            Object val = getter.invoke(bean);
            if (val != null) {
                result.put(propName, mapper.translate(getter.invoke(bean)));
            }
        }
        return result;
    }
}