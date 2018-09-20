package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.hbz.service.HbzSmOrgService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.SmOrgMapper;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.repository.SmOrgRepository;
import com.troy.keeper.system.repository.SmUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class HbzSmOrgServiceImpl extends BaseEntityServiceImpl<SmOrg, SmOrgDTO> implements HbzSmOrgService {

    @Autowired
    SmOrgMapper smOrgMapper;

    @Autowired
    SmOrgRepository smOrgRepository;

    @Autowired
    SmUserRepository smUserRepository;

    @Override
    public BaseMapper<SmOrg, SmOrgDTO> getMapper() {
        return smOrgMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return smOrgRepository;
    }

    @Override
    public List<Long> getSubOrgIdArray(Long userId) {
        SmUser user = smUserRepository.findOne(userId);
        return getSubOrgIdArrayByOrgId(user.getOrgId());
    }

    private List<Long> getSubOrgIdArrayByOrgId(Long orgId) {
        List<Long> ids = new LinkedList<>();
        List<SmOrg> subs = smOrgRepository.getListByPId(orgId);
        for (SmOrg smOrg : subs) {
            Long subid = smOrg.getId();
            ids.addAll(getSubOrgIdArrayByOrgId(subid));
        }
        ids.add(orgId);
        return ids;
    }
}
