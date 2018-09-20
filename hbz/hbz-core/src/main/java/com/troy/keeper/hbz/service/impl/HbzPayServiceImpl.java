package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzPay;
import com.troy.keeper.hbz.repository.HbzPayRepository;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.mapper.HbzPayMapper;
import com.troy.keeper.hbz.type.BillType;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by leecheng on 2017/10/30.
 */
@Service
@Transactional
public class HbzPayServiceImpl extends BaseEntityServiceImpl<HbzPay, HbzPayDTO> implements HbzPayService {

    @Autowired
    private HbzPayRepository repository;
    @Autowired
    private HbzPayMapper mapper;

    @Override
    public HbzPayMapper getMapper() {
        return mapper;
    }

    @Override
    public BaseRepository getRepository() {
        return repository;
    }

    @Override
    public String createTradeNo(BusinessType businessType) {
        HbzPayDTO q = new HbzPayDTO();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimestamp = format.format(new Date());
        String tradeNo;
        int init = 0;
        while (true) {
            tradeNo = businessType.toString() + "-" + currentTimestamp + "-" + StringHelper.frontCompWithZore(++init, 8);
            q.setTradeNo(tradeNo);
            long count = count(q);
            if (count == 0) {
                break;
            }
        }
        return tradeNo;
    }

    @Override
    public HbzPayDTO findByTradeNo(String tradeNo) {
        return mapper.map(repository.findByTradeNo(tradeNo));
    }

    @Override
    public HbzPay findByBusinessNo(String businessNo) {
        return repository.findByBusinessNo(businessNo);
    }

    @Override
    public void bill(HbzBillDTO bill) {
        if (bill.getBillType().equals(BillType.PAY)) {
            HbzPayDTO payQuery = new HbzPayDTO();
            payQuery.setStatus("1");
            payQuery.setTradeNo(bill.getTradeNo());
            payQuery.setPayProgresses(Arrays.asList(PayProgress.SUCCESS));
            payQuery.setBill(0);
            List<HbzPayDTO> pays = query(payQuery);
            pays.stream().forEach(pay -> {
                pay.setBill(1);
                save(pay);
            });
        }
    }
}
