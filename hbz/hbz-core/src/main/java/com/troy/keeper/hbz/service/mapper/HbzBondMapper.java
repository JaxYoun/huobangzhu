package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzBond;
import com.troy.keeper.hbz.repository.HbzBondGradeRepository;
import com.troy.keeper.hbz.repository.HbzBondRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/25.
 */
@Component
public class HbzBondMapper extends BaseMapper<HbzBond, HbzBondDTO> {

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    HbzBondRepository hbzBondRepository;

    @Autowired
    HbzBondGradeMapper hbzBondGradeMapper;

    @Autowired
    HbzBondGradeRepository bondGradeRepository;

    @Override
    public HbzBond newEntity() {
        return new HbzBond();
    }

    @Override
    public HbzBondDTO newDTO() {
        return new HbzBondDTO();
    }

    @Override
    public HbzBond find(Long id) {
        return hbzBondRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzBond entity, HbzBondDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user", "bondGrade");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getBondGrade() != null) {
            dto.setBondGrade(hbzBondGradeMapper.map(entity.getBondGrade()));
            dto.setBondGradeId(entity.getBondGrade().getId());
        }
    }

    @Override
    public void dto2entity(HbzBondDTO dto, HbzBond entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "bondGrade"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);

        if (dto.getBondGradeId() != null)
            entity.setBondGrade(bondGradeRepository.findOne(dto.getBondGradeId()));
        else if (dto.getBondGrade() != null && dto.getBondGrade().getId() != null)
            entity.setBondGrade(bondGradeRepository.findOne(dto.getBondGrade().getId()));
        else entity.setBondGrade(null);
    }
}
