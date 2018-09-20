package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.system.domain.SmOrg;

import java.util.List;

public interface HbzSmOrgService extends BaseEntityService<SmOrg, SmOrgDTO> {

    List<Long> getSubOrgIdArray(Long userId);

}
