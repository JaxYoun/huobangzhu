package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.service.BaseEntityService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.sys.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/24.
 */
public abstract class BaseEntityServiceImpl<ENT extends BaseAuditingEntity, DTO extends BaseDTO> implements BaseEntityService<ENT, DTO> {

    public abstract BaseMapper<ENT, DTO> getMapper();

    public abstract BaseRepository<ENT, Serializable> getRepository();

    @Override
    public Iterable<DTO> save(Iterable<DTO> ll) {
        List<DTO> list = new ArrayList<DTO>();
        if (ll != null)
            for (DTO dto : ll) {
                list.add(save(dto));
            }
        return list;
    }

    public DTO findById(Long id) {
        return getMapper().map(getRepository().findOne(id));
    }

    @Override
    public DTO save(DTO dto) {
        BaseMapper<ENT, DTO> mapper = getMapper();
        BaseRepository repository = getRepository();
        return mapper.map((ENT) repository.save(mapper.map(dto)));
    }

    @Override
    public DTO get(DTO dto) {
        BaseMapper<ENT, DTO> mapper = getMapper();
        BaseRepository repository = getRepository();
        return mapper.map((ENT) repository.findOne(dto.getId()));
    }

    @Override
    public boolean delete(DTO dto) {
        if (dto == null) {
            return false;
        } else {
            BaseRepository repository = getRepository();
            ENT b = (ENT) repository.findOne(dto.getId());
            if (b == null) {
                return false;
            } else {
                b.setStatus(Const.STATUS_DISABLED);
                repository.save(b);
                return true;
            }
        }
    }

    @Override
    public List<DTO> query(DTO dto) {
        BaseMapper<ENT, DTO> mapper = getMapper();
        BaseRepository repository = getRepository();
        Sort sort = dto.getSort();
        List<ENT> l;
        if (sort != null)
            l = repository.findAll(QueryBuilder.buildQuery(dto), sort);
        else
            l = repository.findAll(QueryBuilder.buildQuery(dto), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        return l.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public Page<DTO> queryPage(DTO dto, Pageable pageable) {
        BaseRepository repository = getRepository();
        Sort haveSort = pageable.getSort();
        if (haveSort != null) {
            Iterator<Sort.Order> orders = haveSort.iterator();
            if (!orders.hasNext()) {
                pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
            }
        } else {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        }
        Page<ENT> p = repository.findAll(QueryBuilder.buildQuery(dto), pageable);
        return p.map(getMapper()::map);
    }

    @Override
    public Long count(DTO dto) {
        BaseRepository repository = getRepository();
        return repository.count(QueryBuilder.buildQuery(dto));
    }

    @Override
    public List<DTO> query(Map<String, Object> query) {
        BaseMapper<ENT, DTO> mapper = getMapper();
        BaseRepository repository = getRepository();
        List<ENT> l = repository.findAll(QueryBuilder.buildQuery(query), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        return l.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public List<DTO> query(Map<String, Object> query, Sort sort) {
        BaseMapper<ENT, DTO> mapper = getMapper();
        BaseRepository repository = getRepository();
        List<ENT> l = repository.findAll(QueryBuilder.buildQuery(query), sort);
        return l.stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public Page<DTO> queryPage(Map<String, Object> query, @NotNull Pageable pageable) {
        BaseMapper<ENT, DTO> mapper = getMapper();
        BaseRepository repository = getRepository();
        Sort haveSort = pageable.getSort();
        if (haveSort != null) {
            Iterator<Sort.Order> orders = haveSort.iterator();
            if (!orders.hasNext()) {
                pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
            }
        } else {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        }
        Page<ENT> l = repository.findAll(QueryBuilder.buildQuery(query), pageable);
        return l.map(mapper::map);
    }

    @Override
    public Long count(Map<String, Object> query) {
        BaseRepository repository = getRepository();
        return repository.count(QueryBuilder.buildQuery(query));
    }
}
