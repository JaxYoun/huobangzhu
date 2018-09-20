package com.troy.keeper.hbz.sys;

import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by leecheng on 2017/9/14.
 */
public class QueryBuilder {

    public static ThreadLocal<Map<String, Join>> joins = new ThreadLocal<>();

    public static Specification buildQuery(Object dto) {
        Specification specification;
        if (dto instanceof Map) {
            specification = buildMap((Map) dto);
        } else {
            specification = buildEntity(dto);
        }
        return specification;
    }

    private static Specification buildEntity(Object dto) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                criteriaQuery.distinct(true);
                joins.set(new HashMap<>());
                Class clazz = dto.getClass();
                List<Field> fields = new ArrayList<>();
                List<Predicate> query = new ArrayList<>();
                while (clazz != Object.class) {
                    fields.addAll(Arrays.asList(clazz.getFields()));
                    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                    clazz = clazz.getSuperclass();
                }
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Annotation annotation = field.getAnnotation(QueryColumn.class);
                    if (annotation != null) {
                        QueryColumn queryColumn = (QueryColumn) annotation;
                        String propName = queryColumn.propName();
                        if (propName.equals("")) {
                            propName = field.getName();
                        }
                        String queryOper = queryColumn.queryOper();
                        if (queryOper.equals("")) {
                            queryOper = "equal";
                        }
                        try {
                            query.addAll(builds(propName, queryOper, field.get(dto), root, criteriaQuery, criteriaBuilder));
                        } catch (Exception e) {
                            throw new RuntimeException("查询条件不可用");
                        }
                    }
                }
                Predicate[] queryPredicate = new Predicate[query.size()];
                joins.remove();
                return criteriaBuilder.and(query.toArray(queryPredicate));
            }
        };
        return specification;
    }

    public static Specification buildMap(Map<String, Object> queryMap) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                criteriaQuery.distinct(true);
                joins.set(new HashMap<>());
                Set<String> fields = queryMap.keySet();
                List<Predicate> query = new ArrayList<>();
                for (String field : fields) {
                    String[] parts = field.split("\\s+");
                    String propName = parts[0];
                    String queryOper = parts.length > 1 ? parts[1] : "=";
                    Object v = queryMap.get(field);
                    query.addAll(builds(propName, queryOper, v, root, criteriaQuery, criteriaBuilder));
                }
                Predicate[] queryPredicate = new Predicate[query.size()];
                joins.remove();
                return criteriaBuilder.and(query.toArray(queryPredicate));
            }
        };
        return specification;
    }

    public static List<Predicate> builds(String propName, String queryOper, Object val, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> query = new ArrayList<>();
        String[] props = propName.split("\\.");
        Object v = val;
        try {
            boolean isNull = false;
            if (v instanceof String) {
                String s = (String) v;
                isNull = StringHelper.isNullOREmpty(s);
            } else {
                isNull = v == null;
            }
            switch (queryOper.toUpperCase()) {
                case "=":
                case "EQUAL": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            query.add(criteriaBuilder.equal(join.get(props[props.length - 1]), v));
                        } else {
                            query.add(criteriaBuilder.equal(root.get(propName), v));
                        }
                    }
                }
                break;
                case "ISNULL": {
                    if (isNull) {
                        break;
                    }
                    if (props.length > 1) {
                        StringBuilder path = new StringBuilder();
                        Join join = null;
                        for (int i = 0; i < props.length - 1; i++) {
                            if (i == 0) {
                                path.append(props[i]);
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = root.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            } else {
                                path.append(".".concat(props[i]));
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = join.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            }
                        }
                        query.add(criteriaBuilder.isNull(join.get(props[props.length - 1])));
                    } else {
                        query.add(criteriaBuilder.isNull(root.get(propName)));
                    }
                }

                break;
                case "NOTNULL": {
                    if (isNull) {
                        break;
                    }
                    if (props.length > 1) {
                        StringBuilder path = new StringBuilder();
                        Join join = null;
                        for (int i = 0; i < props.length - 1; i++) {
                            if (i == 0) {
                                path.append(props[i]);
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = root.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            } else {
                                path.append(".".concat(props[i]));
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = join.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            }
                        }
                        query.add(criteriaBuilder.isNotNull(join.get(props[props.length - 1])));
                    } else {
                        query.add(criteriaBuilder.isNotNull(root.get(propName)));
                    }
                }
                break;
                case "ISEMPTY": {
                    if (isNull) {
                        break;
                    }
                    if (props.length > 1) {
                        StringBuilder path = new StringBuilder();
                        Join join = null;
                        for (int i = 0; i < props.length - 1; i++) {
                            if (i == 0) {
                                path.append(props[i]);
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = root.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            } else {
                                path.append(".".concat(props[i]));
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = join.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            }
                        }
                        query.add(criteriaBuilder.isEmpty(join.get(props[props.length - 1])));
                    } else {
                        query.add(criteriaBuilder.isEmpty(root.get(propName)));
                    }
                }
                break;
                case "ISNOTEMPTY": {
                    if (isNull) {
                        break;
                    }
                    if (props.length > 1) {
                        StringBuilder path = new StringBuilder();
                        Join join = null;
                        for (int i = 0; i < props.length - 1; i++) {
                            if (i == 0) {
                                path.append(props[i]);
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = root.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            } else {
                                path.append(".".concat(props[i]));
                                if (joins.get().get(path.toString()) != null) {
                                    join = joins.get().get(path.toString());
                                } else {
                                    join = join.join(props[i]);
                                    joins.get().put(path.toString(), join);
                                }
                            }
                        }
                        query.add(criteriaBuilder.isNotEmpty(join.get(props[props.length - 1])));
                    } else {
                        query.add(criteriaBuilder.isNotEmpty(root.get(propName)));
                    }
                }
                break;
                case "LIKE": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            if (((String) v).contains("%") || ((String) v).contains("_") || ((String) v).contains("?"))
                                query.add(criteriaBuilder.like(join.get(props[props.length - 1]), (String) v));
                            else
                                query.add(criteriaBuilder.like(join.get(props[props.length - 1]), "%" + (String) v + "%"));
                        } else {
                            if (((String) v).contains("%") || ((String) v).contains("_") || ((String) v).contains("?"))
                                query.add(criteriaBuilder.like(root.get(propName), (String) v));
                            else
                                query.add(criteriaBuilder.like(root.get(propName), "%" + (String) v + "%"));
                        }
                    }
                }
                break;
                case "<":
                case "LT": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            query.add(criteriaBuilder.lt(join.get(props[props.length - 1]), (Number) v));
                        } else {
                            query.add(criteriaBuilder.lt(root.get(propName), (Number) v));
                        }
                    }
                }
                break;
                case "<=":
                case "LE": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            query.add(criteriaBuilder.le(join.get(props[props.length - 1]), (Number) v));
                        } else {
                            query.add(criteriaBuilder.le(root.get(propName), (Number) v));
                        }
                    }
                }
                break;
                case ">":
                case "GT": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            query.add(criteriaBuilder.gt(join.get(props[props.length - 1]), (Number) v));
                        } else {
                            query.add(criteriaBuilder.gt(root.get(propName), (Number) v));
                        }
                    }
                }
                break;
                case ">=":
                case "GE": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            query.add(criteriaBuilder.ge(join.get(props[props.length - 1]), (Number) v));
                        } else {
                            query.add(criteriaBuilder.ge(root.get(propName), (Number) v));
                        }
                    }
                }
                break;
                case "IN": {
                    if (!isNull) {
                        if (props.length > 1) {
                            StringBuilder path = new StringBuilder();
                            Join join = null;
                            for (int i = 0; i < props.length - 1; i++) {
                                if (i == 0) {
                                    path.append(props[i]);
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = root.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                } else {
                                    path.append(".".concat(props[i]));
                                    if (joins.get().get(path.toString()) != null) {
                                        join = joins.get().get(path.toString());
                                    } else {
                                        join = join.join(props[i]);
                                        joins.get().put(path.toString(), join);
                                    }
                                }
                            }
                            if (v instanceof Collection) {
                                query.add(join.get(props[props.length - 1]).in((Collection) v));
                            } else if (v instanceof Object[]) {
                                query.add(join.get(props[props.length - 1]).in((Object[]) v));
                            } else {
                                throw new RuntimeException("错误");
                            }
                        } else {
                            if (v instanceof Collection) {
                                query.add(root.get(propName).in((Collection) v));
                            } else if (v instanceof Object[]) {
                                query.add(root.get(propName).in((Object[]) v));
                            } else {
                                throw new RuntimeException("错误");
                            }
                        }
                    }
                }
                break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return query;
    }

}
