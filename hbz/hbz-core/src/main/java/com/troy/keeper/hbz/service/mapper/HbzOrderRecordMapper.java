package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzOrderRecord;
import com.troy.keeper.hbz.repository.HbzOrderRecordRepository;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/3.
 */
@Component
public class HbzOrderRecordMapper extends BaseMapper<HbzOrderRecord, HbzOrderRecordDTO> {
    @Autowired
    private HbzOrderRecordRepository hbzOrderRecordRepository;
    @Autowired
    private HbzUserRepository hbzUserRepository;
    @Autowired
    private HbzUserMapper hbzUserMapper;
    @Autowired
    private HbzOrderRepository hbzOrderRepository;
    @Autowired
    private HbzOrderMapper hbzOrderMapper;

    @Override
    public HbzOrderRecord newEntity() {
        return new HbzOrderRecord();
    }

    @Override
    public HbzOrderRecordDTO newDTO() {
        return new HbzOrderRecordDTO();
    }

    @Override
    public HbzOrderRecord find(Long id) {
        return hbzOrderRecordRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzOrderRecord entity, HbzOrderRecordDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user", "order");
        if (entity.getOrder() != null) {
            dto.setOrder(hbzOrderMapper.map(entity.getOrder()));
            dto.setOrderId(entity.getOrder().getId());
        }
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzOrderRecordDTO dto, HbzOrderRecord entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "order"));
        if (dto.getOrderId() != null)
            entity.setOrder(hbzOrderRepository.findOne(dto.getOrderId()));
        else if (dto.getOrder() != null && dto.getOrder().getId() != null)
            entity.setOrder(hbzOrderRepository.findOne(dto.getOrder().getId()));
        else entity.setOrder(null);

        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
