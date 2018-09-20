package com.troy.keeper.hbz.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/12/11.
 */
public interface EntityRepository {

    <T> T save(T t);

    <T> T get(Class<T> entity, Serializable id);

    <T> List<T> query(Class<T> clazz, Specification specification, List<String> selectors, List<Sort.Order> orders);

    <T> List<T> query(Class<T> clazz, Specification specification, List<Sort.Order> orders);

    <T> List<T> queryPageList(Class<T> clazz, Pageable pageable, Specification specification, List<String> selectors, List<Sort.Order> orders);

    <T> List<T> queryPageList(Class<T> clazz, Pageable pageable, Specification specification, List<Sort.Order> orders);

    <T> Long queryCount(Class<T> clazz, Specification specification);

    <T> List<T> queryTuple2(Class<T> targetEntity, Class joined, Map<String, String> joinVar, Object entity1Condition, Object entity2Condition);

}
