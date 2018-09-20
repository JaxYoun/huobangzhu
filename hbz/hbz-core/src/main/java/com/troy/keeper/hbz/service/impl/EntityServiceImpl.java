package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.po.BaseVersionLocked;
import com.troy.keeper.hbz.repository.EntityRepository;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.sys.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/8.
 */
@Service
@Transactional
public class EntityServiceImpl implements EntityService {

    @Autowired
    EntityRepository entityRepository;

    @Override
    public <T> T save(T t) {
        return entityRepository.save(t);
    }

    @Override
    public <D extends BaseDTO, E extends BaseAuditingEntity> D save(D t, BaseMapper<E, D> mapper) {
        return mapper.map(entityRepository.save(mapper.map(t)));
    }

    @Override
    public <T> T get(Class<T> ent, Serializable id) {
        return entityRepository.get(ent, id);
    }

    @Override
    public <T, R> R get(Class<T> ent, Function<T, R> function, Serializable id) {
        return function.apply(entityRepository.get(ent, id));
    }

    @Override
    public <T extends BaseAuditingEntity, R extends BaseDTO> R get(Class<T> ent, BaseMapper<T, R> mapper, Serializable id) {
        return mapper.map(entityRepository.get(ent, id));
    }

    @Override
    public <T> List<T> query(Class<T> entity, Object query, List<String> props, Sort.Order... orders) {
        List<T> list = entityRepository.query(entity, QueryBuilder.buildQuery(query), props, Arrays.asList(orders));
        return list;
    }

    @Override
    public <T> List<T> query(Class<T> entity, Object query, Sort.Order... orders) {
        List<T> list = entityRepository.query(entity, QueryBuilder.buildQuery(query), Arrays.asList(orders));
        return list;
    }

    @Override
    public <T> Page<T> queryPage(Class<T> entity, Pageable pageable, Object query, List<String> props, Sort.Order... orders) {
        Page<T> page = new PageImpl<>(entityRepository.queryPageList(entity, pageable, QueryBuilder.buildQuery(query), props, Arrays.asList(orders)), pageable, entityRepository.queryCount(entity, QueryBuilder.buildQuery(query)));
        return page;
    }

    @Override
    public <T> Page<T> queryPage(Class<T> entity, Pageable pageable, Object query, Sort.Order... orders) {
        Page<T> page = new PageImpl<>(entityRepository.queryPageList(entity, pageable, QueryBuilder.buildQuery(query), Arrays.asList(orders)), pageable, entityRepository.queryCount(entity, QueryBuilder.buildQuery(query)));
        return page;
    }

    @Override
    public <T extends BaseAuditingEntity, R extends BaseDTO> List<R> query(Class<T> entity, BaseMapper<T, R> mapper, Object query, List<String> props, Sort.Order... orders) {
        List<T> list = entityRepository.query(entity, QueryBuilder.buildQuery(query), props, Arrays.asList(orders));
        return list.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public <T extends BaseAuditingEntity, R extends BaseDTO> List<R> query(Class<T> entity, BaseMapper<T, R> mapper, Object query, Sort.Order... orders) {
        List<T> list = entityRepository.query(entity, QueryBuilder.buildQuery(query), Arrays.asList(orders));
        return list.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public <T extends BaseAuditingEntity, R extends BaseDTO> Page<R> queryPage(Class<T> entity, BaseMapper<T, R> mapper, Pageable pageable, Object query, List<String> props, Sort.Order... orders) {
        Page<T> page = new PageImpl<>(entityRepository.queryPageList(entity, pageable, QueryBuilder.buildQuery(query), props, Arrays.asList(orders)), pageable, entityRepository.queryCount(entity, QueryBuilder.buildQuery(query)));
        return page.map(mapper::map);
    }

    @Override
    public <T extends BaseAuditingEntity, R extends BaseDTO> Page<R> queryPage(Class<T> entity, BaseMapper<T, R> mapper, Pageable pageable, Object query, Sort.Order... orders) {
        Page<T> page = new PageImpl<>(entityRepository.queryPageList(entity, pageable, QueryBuilder.buildQuery(query), Arrays.asList(orders)), pageable, entityRepository.queryCount(entity, QueryBuilder.buildQuery(query)));
        return page.map(mapper::map);
    }

    @Override
    public <T, R> List<R> query(Class<T> entity, Function<T, R> function, Object query, List<String> props, Sort.Order... orders) {
        List<T> list = entityRepository.query(entity, QueryBuilder.buildQuery(query), props, Arrays.asList(orders));
        return list.stream().map(function::apply).collect(Collectors.toList());
    }

    @Override
    public <T, R> List<R> query(Class<T> entity, Function<T, R> function, Object query, Sort.Order... orders) {
        List<T> list = entityRepository.query(entity, QueryBuilder.buildQuery(query), Arrays.asList(orders));
        return list.stream().map(function::apply).collect(Collectors.toList());
    }

    @Override
    public <T, R> Page<R> queryPage(Class<T> entity, Function<T, R> function, Pageable pageable, Object query, List<String> props, Sort.Order... orders) {
        Page<T> page = new PageImpl<>(entityRepository.queryPageList(entity, pageable, QueryBuilder.buildQuery(query), props, Arrays.asList(orders)), pageable, entityRepository.queryCount(entity, QueryBuilder.buildQuery(query)));
        return page.map(function::apply);
    }

    public <T, R> Page<R> queryPage(Class<T> entity, Function<T, R> function, Pageable pageable, Object query, Sort.Order... orders) {
        Page<T> page = new PageImpl<>(entityRepository.queryPageList(entity, pageable, QueryBuilder.buildQuery(query), Arrays.asList(orders)), pageable, entityRepository.queryCount(entity, QueryBuilder.buildQuery(query)));
        return page.map(function::apply);
    }

    @Override
    public <T> Long count(Class<T> entity, Object query) {
        return entityRepository.queryCount(entity, QueryBuilder.buildQuery(query));
    }

    public <T extends BaseVersionLocked> List<T> queryTuple2(Class<T> targetEntity, Class otherEntity, Map<String, String> joinVar, Object targetSelector, Object otherSelector) {
        return entityRepository.queryTuple2(targetEntity, otherEntity, joinVar, targetSelector, otherSelector);
    }

    public <T extends BaseVersionLocked, K extends BaseDTO> List<K> queryTuple2(Class<T> targetEntity, Class otherEntity, Map<String, String> joinVar, Object targetSelector, Object otherSelector, Function<T, K> function) {
        return entityRepository.queryTuple2(targetEntity, otherEntity, joinVar, targetSelector, otherSelector).stream().map(function::apply).collect(Collectors.toList());
    }
}
