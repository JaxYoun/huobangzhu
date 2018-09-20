package com.troy.keeper.hbz.service;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.hbz.dto.BaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/9/14.
 */
public interface BaseEntityService<E extends BaseAuditingEntity, T extends BaseDTO> {

    T findById(Long id);

    Iterable<T> save(Iterable<T> ll);

    T save(T t);

    T get(T t);

    boolean delete(T t);

    List<T> query(T t);

    Page<T> queryPage(T t, Pageable pageable);

    Long count(T t);

    List<T> query(Map<String, Object> query);

    List<T> query(Map<String, Object> query, Sort sort);

    Page<T> queryPage(Map<String, Object> query, Pageable pageParameter);

    Long count(Map<String, Object> query);

}
