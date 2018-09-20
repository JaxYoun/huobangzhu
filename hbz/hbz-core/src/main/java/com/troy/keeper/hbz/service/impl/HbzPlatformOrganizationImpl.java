package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.HbzPlatformOrganization;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzPlatformOrganizationRepository;
import com.troy.keeper.hbz.service.HbzPlatformOrganizationService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzAreaMapper;
import com.troy.keeper.hbz.service.mapper.HbzPlatformOrganizationMapper;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.repository.SmOrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/16.
 */
@Service
@Transactional
public class HbzPlatformOrganizationImpl extends BaseEntityServiceImpl<HbzPlatformOrganization, HbzPlatformOrganizationDTO> implements HbzPlatformOrganizationService {

    @Autowired
    HbzPlatformOrganizationRepository hbzPlatformOrganizationRepository;

    @Autowired
    HbzPlatformOrganizationMapper hbzPlatformOrganizationMapper;

    @Autowired
    SmOrgRepository smOrgRepository;

    @Autowired
    HbzAreaRepository hbzAreaRepository;

    @Autowired
    HbzAreaMapper hbzAreaMapper;

    @Override
    public BaseMapper<HbzPlatformOrganization, HbzPlatformOrganizationDTO> getMapper() {
        return hbzPlatformOrganizationMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzPlatformOrganizationRepository;
    }

    @Override
    public String getRelationship(Long pid) {
        SmOrg smOrg = smOrgRepository.findOne(pid);
        if (smOrg.getRelationship() != null && smOrg.getRelationship().matches("\\.*\\d+")) {
            String[] part = smOrg.getRelationship().split("\\-");
            String lastNumber = part[part.length - 1];
            if (StringHelper.notNullAndEmpty(lastNumber) && lastNumber.matches("\\d+")) {
                Long last = Long.valueOf(lastNumber);
                Long nextNum = last + 1;
                String next = smOrg.getRelationship() + "-" + nextNum;
                return next;
            }
        }
        return null;
    }

    @Override
    public boolean setArea(HbzPlatformOrganizationDTO platformOrganizationD, List<HbzAreaDTO> areas) {
        HbzPlatformOrganization platformOrganization = hbzPlatformOrganizationRepository.findOne(platformOrganizationD.getId());
        List<HbzArea> as = areas.stream().map(HbzAreaDTO::getId).map(hbzAreaRepository::findOne).collect(Collectors.toList());
        platformOrganization.setServiceAreas(as);
        hbzPlatformOrganizationRepository.save(platformOrganization);
        return true;
    }

    @Override
    public List<HbzAreaDTO> getAreas(HbzPlatformOrganizationDTO hbzPlatformOrganization) {
        HbzPlatformOrganization platformOrganization = hbzPlatformOrganizationRepository.findOne(hbzPlatformOrganization.getId());
        List<HbzArea> serviceArea = platformOrganization.getServiceAreas();
        return serviceArea.stream().map(hbzAreaMapper::map).collect(Collectors.toList());
    }
}
