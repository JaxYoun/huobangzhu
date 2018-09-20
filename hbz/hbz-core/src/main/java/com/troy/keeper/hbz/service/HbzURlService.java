package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzURLDTO;
import com.troy.keeper.hbz.po.HbzUrl;

import java.util.List;

/**
 * Created by leecheng on 2018/1/8.
 */
public interface HbzURlService extends BaseEntityService<HbzUrl, HbzURLDTO> {

    boolean setAuths(HbzURLDTO url, List<HbzAuthDTO> auths);

}
