package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import com.troy.keeper.hbz.po.HbzPlatformOrganization;

import java.util.List;

/**
 * Created by leecheng on 2018/1/16.
 */
public interface HbzPlatformOrganizationService extends BaseEntityService<HbzPlatformOrganization, HbzPlatformOrganizationDTO> {

    String getRelationship(Long pid);

    boolean setArea(HbzPlatformOrganizationDTO platformOrganization, List<HbzAreaDTO> areas);

    List<HbzAreaDTO> getAreas(HbzPlatformOrganizationDTO hbzPlatformOrganization);
}
