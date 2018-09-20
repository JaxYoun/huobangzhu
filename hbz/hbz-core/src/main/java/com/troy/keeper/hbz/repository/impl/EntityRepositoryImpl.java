package com.troy.keeper.hbz.repository.impl;

import com.troy.keeper.hbz.repository.EntityRepository;
import com.troy.keeper.hbz.sys.QueryBuilder;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/8.
 */
@CommonsLog
@Component
public class EntityRepositoryImpl implements EntityRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public <T> T save(T t) {
        entityManager.persist(t);
        return t;
    }

    public <T> T get(Class<T> entity, Serializable id) {
        Object o = entityManager.find(entity, id);
        return (T) o;
    }

    @SneakyThrows
    public <T> List<T> query(Class<T> clazz, Specification specification, List<String> selectors, List<Sort.Order> orders) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);
        List<Selection<?>> ss = new ArrayList<>();
        List<String> sss = new ArrayList<>();
        for (String key : selectors) {
            Selection select = root.get(key);
            sss.add(key);
            ss.add(select);
        }
        Selection s = builder.array(ss.toArray(new Selection[ss.size()]));
        query.select(s);
        if (orders != null && orders.size() > 0) {
            query.orderBy(orders.stream().map(
                    sortOrder -> {
                        switch (sortOrder.getDirection()) {
                            case ASC:
                                return builder.asc(root.get(sortOrder.getProperty()));
                            case DESC:
                                return builder.desc(root.get(sortOrder.getProperty()));
                            default:
                                return null;
                        }
                    }
            ).collect(Collectors.toList()).toArray(new Order[orders.size()]));
        }
        query.where(specification.toPredicate(root, query, builder));
        List<Object[]> results = (List<Object[]>) entityManager.createQuery(query).getResultList();
        List<T> result = new ArrayList<T>();
        for (Object[] props : results) {
            Map<String, Object> res = new LinkedHashMap<>();
            for (int i = 0; i < sss.size(); i++) {
                String key = sss.get(i);
                res.put(key, props[i]);
            }
            T obj = clazz.newInstance();
            BeanUtils.populate(obj, res);
            result.add(obj);
        }
        return result;
    }

    @SneakyThrows
    public <T> List<T> query(Class<T> clazz, Specification specification, List<Sort.Order> orders) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);
        if (orders != null && orders.size() > 0) query.orderBy(orders.stream().map(
                sortOrder -> {
                    switch (sortOrder.getDirection()) {
                        case ASC:
                            return builder.asc(root.get(sortOrder.getProperty()));
                        case DESC:
                            return builder.desc(root.get(sortOrder.getProperty()));
                        default:
                            return null;
                    }
                }
        ).collect(Collectors.toList()).toArray(new Order[orders.size()]));
        query.where(specification.toPredicate(root, query, builder));
        List<T> results = entityManager.createQuery(query).getResultList();
        return results;
    }


    @SneakyThrows
    public <T> List<T> queryPageList(Class<T> clazz, Pageable pageable, Specification specification, List<String> selectors, List<Sort.Order> orders) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);
        List<Selection<?>> ss = new ArrayList<>();
        List<String> sss = new ArrayList<>();
        for (String key : selectors) {
            Selection select = root.get(key);
            sss.add(key);
            ss.add(select);
        }
        Selection s = builder.array(ss.toArray(new Selection[ss.size()]));
        query.select(s);
        if (orders != null && orders.size() > 0) query.orderBy(orders.stream().map(
                sortOrder -> {
                    switch (sortOrder.getDirection()) {
                        case ASC:
                            return builder.asc(root.get(sortOrder.getProperty()));
                        case DESC:
                            return builder.desc(root.get(sortOrder.getProperty()));
                        default:
                            return null;
                    }
                }
        ).collect(Collectors.toList()).toArray(new Order[orders.size()]));
        query.where(specification.toPredicate(root, query, builder));
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        TypedQuery<T> t = entityManager.createQuery(query);
        t.setFirstResult(firstResult);
        t.setMaxResults(pageSize);
        List<Object[]> results = (List<Object[]>) t.getResultList();
        List<T> result = new ArrayList<T>();
        for (Object[] props : results) {
            Map<String, Object> res = new LinkedHashMap<>();
            for (int i = 0; i < sss.size(); i++) {
                String key = sss.get(i);
                res.put(key, props[i]);
            }
            T obj = clazz.newInstance();
            BeanUtils.populate(obj, res);
            result.add(obj);
        }
        return result;
    }

    @SneakyThrows
    public <T> List<T> queryPageList(Class<T> clazz, Pageable pageable, Specification specification, List<Sort.Order> orders) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);
        if (orders != null && orders.size() > 0) query.orderBy(orders.stream().map(
                sortOrder -> {
                    switch (sortOrder.getDirection()) {
                        case ASC:
                            return builder.asc(root.get(sortOrder.getProperty()));
                        case DESC:
                            return builder.desc(root.get(sortOrder.getProperty()));
                        default:
                            return null;
                    }
                }
        ).collect(Collectors.toList()).toArray(new Order[orders.size()]));
        query.where(specification.toPredicate(root, query, builder));
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        TypedQuery<T> t = entityManager.createQuery(query);
        t.setFirstResult(firstResult);
        t.setMaxResults(pageSize);
        List<T> results = t.getResultList();
        return results;
    }

    @SneakyThrows
    public <T> Long queryCount(Class<T> clazz, Specification specification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(clazz);
        query.select(builder.count(root));
        query.where(specification.toPredicate(root, query, builder));
        TypedQuery<Long> q = entityManager.createQuery(query);
        return q.getSingleResult();
    }

    @Override
    public <T> List<T> queryTuple2(Class<T> targetEntity, Class joined, Map<String, String> joinVar, Object targetCondition, Object joinedCondition) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(targetEntity);
        Root targetEntityRoot = query.from(targetEntity);
        Root joinEntityRoot = query.from(joined);
        List<Predicate> ps = new ArrayList<>();
        for (String joinKey : joinVar.keySet()) {
            ps.add(builder.equal(targetEntityRoot.get(joinKey), joinEntityRoot.get(joinVar.get(joinKey))));
        }
        ps.add(QueryBuilder.buildQuery(targetCondition).toPredicate(targetEntityRoot, query, builder));
        ps.add(QueryBuilder.buildQuery(joinedCondition).toPredicate(joinEntityRoot, query, builder));
        query.select(targetEntityRoot);
        query.where(ps.toArray(new Predicate[ps.size()]));
        return entityManager.createQuery(query).getResultList();
    }
}