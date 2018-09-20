package com.troy.keeper.hbz.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leecheng on 2018/1/10.
 */
public class Resource {

    final static public Map<String, Object> path = new HashMap<>();

    public static void put(String url, Object loc) {
        path.put(url, loc);
    }

    public static boolean arrive(String url) {
        return path.keySet().contains(url);
    }
}
