package com.troy.keeper.hbz.repository.impl;

import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.repository.JdbcDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/8/25.
 */
@Component
public class JdbcDaoImpl implements JdbcDao {

    private final static Log log = LogFactory.getLog(JdbcDaoImpl.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public int insert(String table, Map<String, Object> data) {
        List<String> columns = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        StringBuilder s = new StringBuilder();
        s.append("insert into " + table + "(");
        for (String key : data.keySet()) {
            String column = key;
            Object value = data.get(column);
            columns.add(column);
            objects.add(value);
        }
        boolean first = true;
        for (int i = 0; i < columns.size(); i++) {
            if (!first) {
                s.append(",");
            }
            first = false;
            String column = columns.get(i);
            s.append(column);
        }
        s.append(") values (");
        first = true;
        for (int i = 0; i < columns.size(); i++) {
            if (!first) {
                s.append(",");
            }
            first = false;
            s.append("?");
        }
        s.append(")");
        log.debug(s.toString() + "\n" + JsonUtils.toJson(objects));
        return jdbcTemplate.update(s.toString(), objects.toArray());
    }

    public int update(String table, Map<String, Object> selector, Map<String, Object> data) {
        StringBuilder s = new StringBuilder();
        s.append("update " + table + " set ");
        List<Object> args = new ArrayList<>();
        boolean first = true;
        for (String key : data.keySet()) {
            if (!first) {
                s.append(",");
            }
            s.append("" + key + " = ?");
            args.add(data.get(key));
            first = false;
        }
        if (selector != null && selector.keySet().size() > 0) {
            s.append(" where ");
            first = true;
            for (String key : selector.keySet()) {
                if (!first) {
                    s.append("and ");
                }
                first = false;
                s.append(key + " ? ");
                args.add(selector.get(key));
            }
        }
        log.debug(s.toString() + "\n" + JsonUtils.toJson(args));
        return jdbcTemplate.update(s.toString(), args.toArray());
    }

    @Override
    public List<Map<String, Object>> query(String table, Map<String, Object> queryWhere) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(table);
        List<String> columnQuery = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        boolean hasWhere = false;
        if (queryWhere != null) {
            for (String columnWidthC : queryWhere.keySet()) {
                hasWhere = true;
                columnQuery.add(columnWidthC);
                args.add(queryWhere.get(columnWidthC));
            }
        }
        if (hasWhere) {
            sb.append(" where ");
            boolean first = true;
            for (int i = 0; i < columnQuery.size(); i++) {
                if (!first) {
                    sb.append("and ");
                }
                first = false;
                sb.append(columnQuery.get(i) + " ? ");
            }
            log.debug("SQL:" + sb.toString() + "\n" + JsonUtils.toJson(args));
            List<Map<String, Object>> list = jdbcTemplate.query(sb.toString(), args.toArray(), (resultSet, i) -> {
                Map<String, Object> data = new HashMap<>();
                ResultSetMetaData meta = resultSet.getMetaData();
                int c = meta.getColumnCount();
                for (int j = 0; j < c; j++) {
                    String column = meta.getColumnName(j + 1);
                    data.put(column, resultSet.getObject(column));
                }
                return data;
            });
            return list == null ? new ArrayList<>() : list;
        } else {
            log.debug("SQL:" + sb.toString());
            List<Map<String, Object>> list = jdbcTemplate.query(sb.toString(), (resultSet, i) -> {
                Map<String, Object> data = new HashMap<>();
                ResultSetMetaData meta = resultSet.getMetaData();
                int c = meta.getColumnCount();
                for (int j = 0; j < c; j++) {
                    String column = meta.getColumnName(j + 1);
                    data.put(column, resultSet.getObject(column));
                }
                return data;
            });
            return list == null ? new ArrayList<>() : list;
        }
    }

    @Override
    public int delete(String table, Map<String, Object> queryWhere) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(table);
        List<String> columnQuery = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        boolean hasWhere = false;
        if (queryWhere != null) {
            for (String columnWidthC : queryWhere.keySet()) {
                hasWhere = true;
                columnQuery.add(columnWidthC);
                args.add(queryWhere.get(columnWidthC));
            }
        }
        if (hasWhere) {
            sb.append(" where ");
            boolean first = true;
            for (int i = 0; i < columnQuery.size(); i++) {
                if (!first) {
                    sb.append("and ");
                }
                first = false;
                sb.append(columnQuery.get(i) + " ? ");
            }
            log.debug("SQL:" + sb.toString() + "\n" + JsonUtils.toJson(args));
            return this.jdbcTemplate.update(sb.toString(), args);
        } else {
            log.debug("SQL:" + sb.toString());
            return this.jdbcTemplate.update(sb.toString());
        }
    }

    @Override
    public List<Map<String, Object>> query(String table) {
        return query(table, null);
    }
}
