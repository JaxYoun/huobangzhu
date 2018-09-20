package com.troy.keeper.hbz.helper;

import com.troy.keeper.hbz.type.TransType;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举类工具
 */
@Named("EnumUtils")
public final class EnumUtils {

    private EnumUtils() {
        super();
    }

    /**
     * 通过中文名从车辆类型枚举中反向映射车辆类型
     *
     * @param chineseName 车辆类型中文名
     * @return
     */
    public static final TransType getTransTypByChineseName(String chineseName) {
        TransType transTypeValue = null;
        for (TransType transType : TransType.values()) {
            if (chineseName.equals(transType.getName())) {
                transTypeValue = transType;
                break;
            }
        }
        return transTypeValue;
    }

    /**
     * 通过枚举类名和枚举值的中文名，获取枚举对象
     *
     * @param enumClass
     * @param chineseName
     * @return
     */
    public static final Object getEnumInstanceByClassNameAndChineseName(Class<?> enumClass, String chineseName) throws Exception {
        if (enumClass == null) {
            throw new IllegalArgumentException("请输入枚举类名！");
        }
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("请输入正确的枚举类名！");
        }
        if (StringUtils.isBlank(chineseName)) {
            throw new IllegalArgumentException("请输入正确的枚举中文名！");
        }

        Object enumInstance = null;
        try {
            try {
                Method valuesMethod = enumClass.getMethod("values");
                try {
                    Object[] enumValueArr = (Object[]) valuesMethod.invoke(null);
                    for (Object enumValue : enumValueArr) {
                        Method getNameMethod = enumClass.getMethod("getName");
                        String name = (String) getNameMethod.invoke(enumValue);
                        if (chineseName.equals(name)) {
                            enumInstance = enumValue;
                        }
                    }
                } catch (InvocationTargetException e) {
                    throw e;
                }
            } catch (NoSuchMethodException e) {
                throw e;
            }
        } catch (IllegalAccessException e) {
            throw e;
        }
        return enumInstance;
    }
}