package com.troy.keeper.hbz.service.impl;

import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.troy.keeper.hbz.dto.HbzRefundDTO;
import com.troy.keeper.hbz.service.AlipayService;
import com.troy.keeper.hbz.service.HbzRefundAutoService;
import com.troy.keeper.hbz.service.HbzRefundService;
import com.troy.keeper.hbz.type.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2018/3/7.
 */
@Service
@Transactional
public class HbzRefundAutoServiceImpl implements HbzRefundAutoService {

    @Autowired
    HbzRefundService hbzRefundService;

    @Autowired
    AlipayService alipayService;

    @Override
    public void auto() {
        HbzRefundDTO refund = new HbzRefundDTO();
        refund.setStatus("1");
        refund.setRefundStatus(RefundStatus.REFUNDING);
        List<HbzRefundDTO> list = hbzRefundService.query(refund);
        list.forEach(r -> {
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            model.setOutRequestNo(r.getRequestNo());
            model.setOutTradeNo(r.getPay().getTradeNo());
            AlipayTradeFastpayRefundQueryResponse response = alipayService.refundQuery(model);
            if (response != null) {
                if (!response.isSuccess()) {
                    r.setRefundStatus(RefundStatus.FAILURE);
                    hbzRefundService.save(r);
                }
            }
        });
    }

}