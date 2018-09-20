package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzRechargeDTO;
import com.troy.keeper.hbz.po.HbzRecharge;
import com.troy.keeper.hbz.repository.HbzRechargeRepository;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.HbzRechargeService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzRechargeMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leecheng on 2017/12/4.
 */
@Service
@Transactional
public class HbzRechargeServiceImpl extends BaseEntityServiceImpl<HbzRecharge, HbzRechargeDTO> implements HbzRechargeService {

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    private HbzPayService hbzPayService;

    @Autowired
    HbzRechargeRepository hbzRechargeRepository;

    @Autowired
    HbzRechargeMapper m;

    @Override
    public BaseMapper getMapper() {
        return m;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzRechargeRepository;
    }

    @Override
    public boolean completeRecharge(String chargeNo) {
        //TODO 删除代码
        return true;
    }

    @Override
    public HbzRechargeDTO findByChargeNo(String charge) {
        return m.map(hbzRechargeRepository.findByChargeNo(charge));
    }

    @Override
    @SneakyThrows
    public String makeChargeNo() {
        String no;
        int id = 0;
        while (true) {
            no = "CHARGE-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + id;
            Long chargeCount = hbzRechargeRepository.countByChargeNo(no);
            if (chargeCount.longValue() == 0L) return no;
            id++;
        }
    }
}
