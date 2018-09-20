package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzRole;
import com.troy.keeper.hbz.repository.HbzRoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leecheng on 2017/10/16.
 */
@Service
public class HbzRoleMapper extends BaseMapper<HbzRole, HbzRoleDTO> {

    @Autowired
    private HbzRoleRepository hbzRoleRepository;

    @Override
    public HbzRole newEntity() {
        return new HbzRole();
    }

    @Override
    public HbzRoleDTO newDTO() {
        return new HbzRoleDTO();
    }

    @Override
    public HbzRole find(Long id) {
        return hbzRoleRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzRole entity, HbzRoleDTO dto) {
        BeanUtils.copyProperties(entity, dto, "users", "auths");
    }

    @Override
    public void dto2entity(HbzRoleDTO dto, HbzRole entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "users", "auths"));
    }
}
