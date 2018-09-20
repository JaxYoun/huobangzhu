package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzPledgeDTO;
import com.troy.keeper.hbz.helper.MapBuilder;
import com.troy.keeper.hbz.po.HbzBond;
import com.troy.keeper.hbz.po.HbzPledge;
import com.troy.keeper.hbz.repository.HbzPledgeRepository;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzPledgeService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzBondMapper;
import com.troy.keeper.hbz.service.mapper.HbzPledgeMapper;
import com.troy.keeper.hbz.sys.Counter;
import com.troy.keeper.hbz.type.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.List;

/**
 * Created by leecheng on 2018/1/31.
 */
@Service
@Transactional
public class HbzPledgeServiceImpl extends BaseEntityServiceImpl<HbzPledge, HbzPledgeDTO> implements HbzPledgeService {

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    HbzPledgeMapper hbzPledgeMapper;

    @Autowired
    HbzBondMapper hbzBondMapper;

    @Autowired
    HbzPledgeRepository hbzPledgeRepository;

    @Autowired
    EntityService entityService;

    @Override
    public BaseMapper<HbzPledge, HbzPledgeDTO> getMapper() {
        return hbzPledgeMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzPledgeRepository;
    }

    @Override
    public boolean pledge(HbzBondDTO bond, String bizNo, BizCode bizCode) {
        bond = hbzBondService.findById(bond.getId());
        if (bond != null && bond.getBondStatus().intValue() == 1) {
            bond.setBondStatus(2);
            hbzBondService.save(bond);

            HbzPledgeDTO pledge = new HbzPledgeDTO();
            pledge.setStatus("1");
            pledge.setBizNo(bizNo);
            pledge.setBizCode(bizCode);
            pledge.setBondNo(bond.getBondNo());
            pledge.setBizTime(Clock.systemUTC().millis());
            if (save(pledge) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int unPledge(String bizNo, BizCode bizCode) {
        HbzPledgeDTO pledgeQuery = new HbzPledgeDTO();
        pledgeQuery.setStatus("1");
        pledgeQuery.setBizNo(bizNo);
        pledgeQuery.setBizCode(bizCode);
        List<HbzPledgeDTO> pledges = query(pledgeQuery);
        int unPledge = 0;
        if (pledges != null && pledges.size() > 0) {
            for (HbzPledgeDTO pledge : pledges) {
                HbzBondDTO bondQuery = new HbzBondDTO();
                bondQuery.setStatus("1");
                bondQuery.setBondNo(pledge.getBondNo());
                List<HbzBondDTO> bonds = hbzBondService.query(bondQuery);
                for (HbzBondDTO bond : bonds) {
                    if (bond.getBondStatus().intValue() == 2) {
                        bond.setBondStatus(1);
                        hbzBondService.save(bond);
                        unPledge++;
                    }
                }
                pledge.setStatus("0");
                save(pledge);
            }
            return unPledge;
        } else {
            return 0;
        }
    }

    @Override
    public int unPledge(String bizNo, BizCode bizCode, String btype, String g) {

        final Counter bc = new Counter();
        final Counter pc = new Counter();

        List<HbzBondDTO> bondDTOS = entityService.queryTuple2(
                HbzBond.class,
                HbzPledge.class,
                new MapBuilder<String, String>()
                        .put("bondNo", "bondNo")
                        .get(),
                new MapBuilder<String, Object>()
                        .put("bondGrade.bondType =", btype)
                        .put("status =", "1")
                        .put("bondGrade.grade =", g)
                        .get(),
                new MapBuilder<String, Object>()
                        .put("bizNo =", bizNo)
                        .put("bizCode =", bizCode)
                        .put("status =", "1")
                        .get(),
                hbzBondMapper::map
        );

        List<HbzPledgeDTO> pledgeDTOS = entityService.queryTuple2(
                HbzPledge.class,
                HbzBond.class,
                new MapBuilder<String, String>()
                        .put("bondNo", "bondNo")
                        .get(),
                new MapBuilder<String, Object>()
                        .put("bizNo =", bizNo)
                        .put("bizCode =", bizCode)
                        .put("status =", "1")
                        .get(),
                new MapBuilder<String, Object>()
                        .put("bondGrade.bondType =", btype)
                        .put("status =", "1")
                        .put("bondGrade.grade =", g)
                        .get(),
                hbzPledgeMapper::map
        );

        bondDTOS.stream().forEach(bond -> {
            if (bond.getBondStatus().intValue() == 2) {
                bc.countAdd();
                bond.setBondStatus(1);
                hbzBondService.save(bond);
            }
        });

        pledgeDTOS.stream().forEach(pledge -> {
            pledge.setStatus("0");
            pc.countAdd();
            this.save(pledge);
        });

        return pc.getCount();
    }
}
