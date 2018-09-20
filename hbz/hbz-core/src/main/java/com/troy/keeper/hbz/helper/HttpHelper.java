package com.troy.keeper.hbz.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by leecheng on 2017/8/24.
 */
public class HttpHelper {

    private static String[] macSplit = {"-", ":"};

    public static String collectValue(HttpServletRequest request, String name) {
        if (StringHelper.notNullAndEmpty(request.getParameter(name))) {
            return request.getParameter(name);
        }
        Cookie[] cs = request.getCookies();
        for (int i = 0; cs != null && i < cs.length; i++) {
            Cookie c = cs[i];
            if (c.getName().equals(name)) {
                String v = c.getValue();
                return v;
            }
        }
        if (request.getSession().getAttribute(name) != null) {
            Object v = request.getSession().getAttribute(name);
            if (v instanceof String) {
                return (String) v;
            }
        }
        return null;
    }


    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    public static String buildIpV4(String anyFormatIp) {
        if (anyFormatIp.matches("\\d+(\\.\\d+){3}")) {
            return anyFormatIp;
        } else if (anyFormatIp.matches("[\\%\\d]*\\%[\\%\\d]*")) {
            try {
                return URLDecoder.decode(anyFormatIp, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("IP地址解码失败", e);
            }
        }
        throw new RuntimeException("无法进行IP地址解析");
    }

    public static String buildMacWidthSplitChar(String rawformatMac, String splitChar) {
        rawformatMac = toMACNOSP(rawformatMac);
        char[] usermacChars = rawformatMac.toCharArray();
        boolean first = true;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < usermacChars.length; i = i + 2) {
            if (!first) {
                s.append(splitChar);
            }
            first = false;
            s.append(usermacChars[i]);
            s.append(usermacChars[i + 1]);
        }
        rawformatMac = s.toString();
        return rawformatMac;
    }

    public static String toUpcaseMACNOSP(String mac) {
        mac = mac.toUpperCase();
        for (String s : macSplit) {
            mac = mac.replace(s, "");
        }
        return mac;
    }

    public static String tolowcaseMACNOSP(String mac) {
        mac = mac.toLowerCase();
        for (String s : macSplit) {
            mac = mac.replace(s, "");
        }
        return mac;
    }

    public static String toMACNOSP(String mac) {
        for (String s : macSplit) {
            mac = mac.replace(s, "");
        }
        return mac;
    }
}
