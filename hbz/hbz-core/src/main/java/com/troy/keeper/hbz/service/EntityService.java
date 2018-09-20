package com.troy.keeper.hbz.service;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.po.BaseVersionLocked;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by leecheng on 2017/12/8.
 */
public interface EntityService {

    <T> T save(T t);

    <D extends BaseDTO, E extends BaseAuditingEntity> D save(D t, BaseMapper<E, D> mapper);

    <T> T get(Class<T> ent, Serializable id);

    <T, R> R get(Class<T> ent, Function<T, R> function, Serializable id);

    <T extends BaseAuditingEntity, R extends BaseDTO> R get(Class<T> ent, BaseMapper<T, R> mapper, Serializable id);

    <T> List<T> query(Class<T> entity, Object query, List<String> props, Sort.Order... orders);

    <T> List<T> query(Class<T> entity, Object query, Sort.Order... orders);

    <T> Page<T> queryPage(Class<T> entity, Pageable pageable, Object query, List<String> props, Sort.Order... orders);

    <T> Page<T> queryPage(Class<T> entity, Pageable pageable, Object query, Sort.Order... orders);

    <T extends BaseAuditingEntity, R extends BaseDTO> List<R> query(Class<T> entity, BaseMapper<T, R> mapper, Object query, List<String> props, Sort.Order... orders);

    <T extends BaseAuditingEntity, R extends BaseDTO> List<R> query(Class<T> entity, BaseMapper<T, R> mapper, Object query, Sort.Order... orders);

    <T extends BaseAuditingEntity, R extends BaseDTO> Page<R> queryPage(Class<T> entity, BaseMapper<T, R> mapper, Pageable pageable, Object query, List<String> props, Sort.Order... orders);

    <T extends BaseAuditingEntity, R extends BaseDTO> Page<R> queryPage(Class<T> entity, BaseMapper<T, R> mapper, Pageable pageable, Object query, Sort.Order... orders);

    <T, R> List<R> query(Class<T> entity, Function<T, R> function, Object query, List<String> props, Sort.Order... orders);

    <T, R> List<R> query(Class<T> entity, Function<T, R> function, Object query, Sort.Order... orders);

    <T, R> Page<R> queryPage(Class<T> entity, Function<T, R> function, Pageable pageable, Object query, List<String> props, Sort.Order... orders);

    <T, R> Page<R> queryPage(Class<T> entity, Function<T, R> function, Pageable pageable, Object query, Sort.Order... orders);

    <T> Long count(Class<T> entity, Object query);

    public <T extends BaseVersionLocked> List<T> queryTuple2(Class<T> targetEntity, Class otherEntity, Map<String, String> joinVar, Object targetSelector, Object otherSelector);

    public <T extends BaseVersionLocked, K extends BaseDTO> List<K> queryTuple2(Class<T> targetEntity, Class otherEntity, Map<String, String> joinVar, Object targetSelector, Object otherSelector, Function<T, K> function);
}
