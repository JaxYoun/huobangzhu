package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzUserAddressInfoDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzUserAddressInfo;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzUserAddressInfoRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/27.
 */
@Component
public class HbzUserAddressInfoMapper extends BaseMapper<HbzUserAddressInfo, HbzUserAddressInfoDTO> {
    @Autowired
    private HbzUserAddressInfoRepository hbzUserAddressInfoRepository;
    @Autowired
    private HbzUserRepository hbzUserRepository;
    @Autowired
    private HbzUserMapper hbzUserMapper;
    @Autowired
    private HbzAreaRepository areaRepository;
    @Autowired
    private HbzAreaMapper areaMapper;

    @Override
    public HbzUserAddressInfo newEntity() {
        return new HbzUserAddressInfo();
    }

    @Override
    public HbzUserAddressInfoDTO newDTO() {
        return new HbzUserAddressInfoDTO();
    }

    @Override
    public HbzUserAddressInfo find(Long id) {
        return hbzUserAddressInfoRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzUserAddressInfo entity, HbzUserAddressInfoDTO dto) {
        BeanUtils.copyProperties(entity, dto, new String[]{"area", "user"});
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getArea() != null) {
            dto.setArea(areaMapper.map(entity.getArea()));
            dto.setAreaId(entity.getArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzUserAddressInfoDTO dto, HbzUserAddressInfo entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "area"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);

        if (dto.getArea() != null && dto.getArea().getId() != null)
            entity.setArea(areaRepository.findOne(dto.getArea().getId()));
        else if (dto.getAreaId() != null)
            entity.setArea(areaRepository.findOne(dto.getAreaId()));
        else
            entity.setArea(null);

    }
}
