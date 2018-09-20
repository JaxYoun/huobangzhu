package com.troy.keeper.hbz.helper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/8 11:06
 */
@Named("JsonArrToListMapper")
public class JsonArrToListMapper {

    @Named("platArrToList")
    public static List<String> platArrToList(String staticImagePrefix, String platArr) {
        if (StringUtils.isBlank(platArr)) {
            return null;
        }
        int len = platArr.length();
        String withoutFunKuoFu = platArr.substring(1, len - 1);
        if (StringUtils.isBlank(withoutFunKuoFu)) {
            return null;
        }
        List<String> resultList = new ArrayList<>(5);
        if (withoutFunKuoFu.indexOf(",") < 0) {
            resultList.add(staticImagePrefix + withoutFunKuoFu);
            return resultList;
        }
        return Arrays.asList(withoutFunKuoFu.split(",")).stream().map(it -> staticImagePrefix + it).collect(Collectors.toList());
    }

    public static String cutPrefix(String prefix, String stringArr) {
        if (StringUtils.isBlank(stringArr)) {
            return null;
        }
        int len = stringArr.length();
        String withoutFunKuoFu = stringArr.substring(1, len - 1);
        if (StringUtils.isBlank(withoutFunKuoFu)) {
            return null;
        }
        List<String> resultList = new ArrayList<>(5);
        if (withoutFunKuoFu.indexOf(",") < 0) {
            resultList.add(StringHelper.getTailFromFullImagePath(prefix, withoutFunKuoFu));
        }
        return Arrays.asList(withoutFunKuoFu.split(",")).stream().map(it -> StringHelper.getTailFromFullImagePath(prefix, it)).collect(Collectors.toList()).toString();
    }

}