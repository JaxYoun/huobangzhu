package com.troy.keeper.hbz.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by boolean on 2017/8/24.
 */
public class StringHelper {

    public static boolean isNullOREmpty(String s) {
        return s == null || s.equals("");
    }

    public static boolean notNullAndEmpty(String s) {
        return !isNullOREmpty(s);
    }

    public static List<String> sortAsc(List<String> src) {
        if (src == null) {
            throw new IllegalStateException("不能为空");
        } else {
            return src.stream().sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            }).collect(Collectors.toList());
        }
    }

    public static String makeRepeat(String c, int num) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < num; i++) {
            s.append(c);
        }
        return s.toString();
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }

    public static String frontCompWithZero(int source, int len) {
        return frontCompWithZore(source, len);
    }

    public static String frontCompWithZore(int source, int len) {
       /*
        * 0 指前面补充零
        * formatLength 字符总长度为 formatLength
        * d 代表为正数。
        */
        String newString = String.format("%0" + len + "d", source);
        return newString;
    }

    public static List<String> sortDesc(List<String> src) {
        if (src == null) {
            throw new IllegalStateException("不能为空");
        } else {
            return src.stream().sorted(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o2.compareTo(o1);
                }
            }).collect(Collectors.toList());
        }
    }

    public static String[] conact(String[] s1, String... s2) {
        int length = s1.length + s2.length;
        String[] s = new String[length];
        for (int i = 0; i < s1.length; i++) {
            s[i] = s1[i];
        }
        for (int i = 0; i < s2.length; i++) {
            s[s1.length + i] = s2[i];
        }
        return s;
    }


    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String joinUse(String[] s, String c) {
        if (s == null) return null;
        if (s.length == 0) return "";
        StringBuilder sl = new StringBuilder();
        sl.append(s[0]);
        for (int i = 1; i < s.length; i++) {
            sl.append(c);
            sl.append(s[i]);
        }
        String saxml1 = sl.toString();
        return saxml1;
    }

    /**
     * 从完整图片路径截取尾部
     *
     * @param prefix
     * @param fullIamgePath
     * @return
     */
    public static String getTailFromFullImagePath(String prefix, String fullIamgePath) {
        if (StringUtils.isNotBlank(fullIamgePath)) {
            String trimedPath = fullIamgePath.trim();
            if (trimedPath.contains(prefix)) {
                return trimedPath.substring(prefix.length());
            }
            return trimedPath;
        }
        return fullIamgePath;
    }

    /**
     * 根据当前数据库最大id生成编号
     *
     * @param maxId
     * @return
     */
    public static String contractCode(Long maxId, char... charArr) {
        StringBuilder stringBuilder = new StringBuilder();
        maxId++;
        String id = maxId.toString();
        int bound = charArr.length - id.length();
        for (int i = 0; i <= bound; i++) {
            stringBuilder.append(charArr[i]);
        }
        stringBuilder.append(id);
        return stringBuilder.toString();
    }

    /**
     * 切除文件名后缀
     *
     * @param name
     * @return
     */
    public static String cutPostfix(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("传入字符不能为空！");
        } else {
            if (name.lastIndexOf('.') >= 0) {
                return name.substring(0, name.lastIndexOf('.'));
            } else {
                return name;
            }
        }
    }

}
