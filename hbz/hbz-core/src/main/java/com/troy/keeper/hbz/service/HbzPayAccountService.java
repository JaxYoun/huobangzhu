package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzPayAccountDTO;
import com.troy.keeper.hbz.po.HbzPayAccount;

/**
 * Created by leecheng on 2017/11/3.
 */
public interface HbzPayAccountService extends BaseEntityService<HbzPayAccount, HbzPayAccountDTO> {

    public HbzPayAccountDTO getDefaultPayAccountByUID(Long uid);

}
