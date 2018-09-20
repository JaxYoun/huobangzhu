package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.hbz.dto.BaseDTO;

/**
 * Created by leecheng on 2017/9/13.
 */
public abstract class BaseMapper<ENTITY extends BaseAuditingEntity, DTO extends BaseDTO> {

    public abstract ENTITY newEntity();

    public abstract DTO newDTO();

    public abstract ENTITY find(Long id);

    public abstract void entity2dto(ENTITY entity, DTO dto);

    public abstract void dto2entity(DTO dto, ENTITY entity);

    public DTO map(ENTITY entity) {
        if (entity != null) {
            DTO dto = newDTO();
            entity2dto(entity, dto);
            return dto;
        } else {
            return null;
        }
    }

    public ENTITY map(DTO dto) {
        if (dto != null) {
            ENTITY entity = null;
            if (dto.getId() != null) {
                entity = find(dto.getId());
            }
            if (entity == null) {
                entity = newEntity();
            }
            dto2entity(dto, entity);
            return entity;
        } else {
            return null;
        }
    }

}
