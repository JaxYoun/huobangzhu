package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzLinkInfoDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzLinkInfo;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzLinkInfoRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/30.
 */
@Component
public class HbzLinkInfoMapper extends BaseMapper<HbzLinkInfo, HbzLinkInfoDTO> {

    @Autowired
    private HbzLinkInfoRepository hbzLinkInfoRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    HbzAreaMapper hbzAreaMapper;

    @Autowired
    HbzAreaRepository repository;

    @Override
    public HbzLinkInfo newEntity() {
        return new HbzLinkInfo();
    }

    @Override
    public HbzLinkInfoDTO newDTO() {
        return new HbzLinkInfoDTO();
    }

    @Override
    public HbzLinkInfo find(Long id) {
        return hbzLinkInfoRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzLinkInfo entity, HbzLinkInfoDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user", "area");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getArea() != null) {
            dto.setArea(hbzAreaMapper.map(entity.getArea()));
            dto.setAreaId(entity.getArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzLinkInfoDTO dto, HbzLinkInfo entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, new String[]{"area", "user"}));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);

        if (dto.getArea() != null && dto.getArea().getId() != null)
            entity.setArea(repository.findOne(dto.getArea().getId()));
        else if (dto.getAreaId() != null)
            entity.setArea(repository.findOne(dto.getAreaId()));
        else
            entity.setArea(null);
    }
}
