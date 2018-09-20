package com.troy.keeper.hbz.repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/8/25.
 */
public interface JdbcDao {

    List<Map<String, Object>> query(String table, Map<String, Object> query);

    List<Map<String, Object>> query(String table);

    int delete(String table, Map<String, Object> query);

    int update(String table, Map<String, Object> selector, Map<String, Object> data);

    int insert(String table, Map<String, Object> data);
}
