package com.troy.keeper.hbz.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leecheng on 2018/2/7.
 */
public class MapBuilder<K, V> {

    private Map<K, V> map = new HashMap<>();


    public Map<K, V> get() {
        return map;
    }

    public MapBuilder<K, V> put(K k, V v) {
        this.map.put(k, v);
        return this;
    }

}
