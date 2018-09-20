package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzTenderDTO;
import com.troy.keeper.hbz.po.HbzTender;

/**
 * Created by leecheng on 2017/11/21.
 */
public interface HbzTenderService extends BaseEntityService<HbzTender, HbzTenderDTO> {

    HbzTenderDTO findByOrderId(Long orderid);

}
