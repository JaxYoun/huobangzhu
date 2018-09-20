package com.troy.keeper.hbz.sys;

import com.troy.keeper.hbz.helper.PropertyMapper;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/13.
 */
public class Bean2Map {

    private List<PropertyMapper> mappers = new ArrayList<>();
    private List<String> ignores = new LinkedList<>();


    public Bean2Map(PropertyMapper... mappers) {
        this.mappers.addAll(Arrays.asList(mappers));
    }

    public Bean2Map addIgnores(String... props) {
        ignores.addAll(Arrays.asList(props));
        return this;
    }

    public Bean2Map addPropMapper(PropertyMapper... mapper) {
        this.mappers.addAll(Arrays.asList(mapper));
        return this;
    }

    @SneakyThrows
    public Map<String, Object> map(Object bean) {
        if (bean == null) return null;
        List<String> excludeProperties = mappers.stream().map(PropertyMapper::getName).collect(Collectors.toList());
        Map<String, PropertyMapper> mapperMap = new HashMap<>();
        if (mappers.size() >= 1)
            mapperMap = mappers.stream().map(mapper -> {
                Map<String, PropertyMapper> map = new HashMap<>();
                map.put(mapper.getName(), mapper);
                return map;
            }).reduce((m1, m2) -> {
                m1.putAll(m2);
                return m1;
            }).get();
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Method> excludeGetters = new LinkedHashMap<>();
        Class clz = bean.getClass();
        {
            for (Method getter : clz.getMethods()) {
                if (getter.getName().startsWith("get") && getter.getParameterCount() == 0) {
                    String getterName = getter.getName();
                    String propName = getterName.substring(3);
                    if (propName.substring(0, 1).equals(propName.substring(0, 1).toUpperCase())) {
                        propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
                    }
                    if (ignores.contains(propName)) continue;
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
            {
                for (Map.Entry<String, Method> getterEnt : excludeGetters.entrySet()) {
                    String propName = getterEnt.getKey();
                    Method getter = getterEnt.getValue();
                    PropertyMapper mapper = mapperMap.get(propName);
                    Object val = getter.invoke(bean);
                    if (val != null)
                        result.put(propName, mapper.map(getter.invoke(bean)));
                }
            }
        }

        return result;
    }
}
