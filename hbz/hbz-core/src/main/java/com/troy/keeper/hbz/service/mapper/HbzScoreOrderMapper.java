package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzScoreOrderDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzScoreOrder;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzProductRepo;
import com.troy.keeper.hbz.repository.HbzScoreOrderRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/19.
 */
@Component
public class HbzScoreOrderMapper extends BaseMapper<HbzScoreOrder, HbzScoreOrderDTO> {

    @Autowired
    HbzProductRepo hbzProductRepo;

    @Autowired
    HbzProductMapper hbzProductMapper;

    @Autowired
    HbzScoreOrderRepository hbzScoreOrderRepository;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzAreaMapper areaMapper;

    @Autowired
    private HbzAreaRepository hbzAreaRepository;

    @Override
    public HbzScoreOrder newEntity() {
        return new HbzScoreOrder();
    }

    @Override
    public HbzScoreOrderDTO newDTO() {
        return new HbzScoreOrderDTO();
    }

    @Override
    public HbzScoreOrder find(Long id) {
        return hbzScoreOrderRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzScoreOrder entity, HbzScoreOrderDTO dto) {
        new Bean2Bean().addExcludeProp("user", "product", "area").copyProperties(entity, dto);

        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }

        if (entity.getProduct() != null) {
            dto.setProduct(hbzProductMapper.map(entity.getProduct()));
            dto.setProductId(entity.getProduct().getId());
        }

        if (entity.getArea() != null) {
            dto.setArea(areaMapper.map(entity.getArea()));
            dto.setAreaId(entity.getArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzScoreOrderDTO dto, HbzScoreOrder entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "product", "area"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else
            entity.setUser(null);

        if (dto.getProductId() != null)
            entity.setProduct(hbzProductRepo.findOne(dto.getProductId()));
        else if (dto.getProduct() != null && dto.getProduct().getId() != null)
            entity.setProduct(hbzProductRepo.findOne(dto.getProduct().getId()));
        else
            entity.setProduct(null);

        if (dto.getArea() != null && dto.getArea().getId() != null)
            entity.setArea(hbzAreaRepository.findOne(dto.getArea().getId()));
        else if (dto.getAreaId() != null)
            entity.setArea(hbzAreaRepository.findOne(dto.getAreaId()));
        else
            entity.setArea(null);
    }
}
