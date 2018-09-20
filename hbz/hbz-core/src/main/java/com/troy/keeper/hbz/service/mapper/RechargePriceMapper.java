package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.dto.RechargePriceDTO;
import com.troy.keeper.hbz.po.RechargePrice;
import org.springframework.stereotype.Service;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 11:26
 */
@Service
public class RechargePriceMapper extends BaseMapper<RechargePrice, RechargePriceDTO> {
    @Override
    public RechargePrice newEntity() {
        return null;
    }

    @Override
    public RechargePriceDTO newDTO() {
        return null;
    }

    @Override
    public RechargePrice find(Long id) {
        return null;
    }

    @Override
    public void entity2dto(RechargePrice entity, RechargePriceDTO dto) {

    }

    @Override
    public void dto2entity(RechargePriceDTO dto, RechargePrice entity) {

    }
}
