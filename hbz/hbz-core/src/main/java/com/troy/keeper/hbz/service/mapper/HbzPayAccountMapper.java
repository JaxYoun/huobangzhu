package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayAccountDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzPayAccount;
import com.troy.keeper.hbz.repository.HbzPayAccountRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.HbzUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/3.
 */
@Component
public class HbzPayAccountMapper extends BaseMapper<HbzPayAccount, HbzPayAccountDTO> {
    @Autowired
    private HbzUserService hbzUserService;
    @Autowired
    private HbzPayAccountRepository hbzPayAccountRepository;
    @Autowired
    private HbzUserRepository hbzUserRepository;
    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Override
    public HbzPayAccount newEntity() {
        return new HbzPayAccount();
    }

    @Override
    public HbzPayAccountDTO newDTO() {
        return new HbzPayAccountDTO();
    }

    @Override
    public HbzPayAccount find(Long id) {
        return hbzPayAccountRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzPayAccount entity, HbzPayAccountDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzPayAccountDTO dto, HbzPayAccount entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserMapper.map(hbzUserService.findUser(dto.getUserId())));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserMapper.map(hbzUserService.findUser(dto.getUser().getId())));

    }
}
