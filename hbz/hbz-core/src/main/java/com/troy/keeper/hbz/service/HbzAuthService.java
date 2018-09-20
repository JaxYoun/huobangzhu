package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzURLDTO;
import com.troy.keeper.hbz.po.HbzAuth;

import java.util.List;

/**
 * Created by leecheng on 2018/1/8.
 */
public interface HbzAuthService extends BaseEntityService<HbzAuth, HbzAuthDTO> {

    boolean setUrls(HbzAuthDTO auth, List<HbzURLDTO> urls);

}
