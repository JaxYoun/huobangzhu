package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzScoreOrderDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzScoreOrder;
import com.troy.keeper.hbz.repository.HbzScoreOrderRepository;
import com.troy.keeper.hbz.service.HbzScoreOrderService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2017/12/20.
 */
@Service
@Transactional
public class HbzScoreOrderServiceImp extends BaseEntityServiceImpl<HbzScoreOrder, HbzScoreOrderDTO> implements HbzScoreOrderService {

    @Autowired
    HbzScoreOrderRepository hbzScoreOrderRepository;

    @Autowired
    HbzScoreOrderMapper hbzScoreOrderMapper;

    @Override
    public BaseMapper<HbzScoreOrder, HbzScoreOrderDTO> getMapper() {
        return hbzScoreOrderMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzScoreOrderRepository;
    }

    @Override
    public String createOrderNo() {
        String oo;
        int i = 1;
        while (true) {
            oo = StringHelper.frontCompWithZore(i++, 7);
            List<HbzScoreOrder> os = hbzScoreOrderRepository.findByOrderNo(oo);
            if (os == null || os.size() == 0) return oo;
        }
    }
}
