package com.troy.keeper.hbz.service.impl;

import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzPay;
import com.troy.keeper.hbz.po.HbzRefund;
import com.troy.keeper.hbz.repository.HbzPayRepository;
import com.troy.keeper.hbz.repository.HbzRefundRepo;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzrefundMapper;
import com.troy.keeper.hbz.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by leecheng on 2018/3/6.
 */
@Service
@Transactional
public class HbzRefundServiceImpl extends BaseEntityServiceImpl<HbzRefund, HbzRefundDTO> implements HbzRefundService {
    @Autowired
    HbzPayService hbzPayService;
    @Autowired
    HbzrefundMapper hbzrefundMapper;
    @Autowired
    HbzRefundRepo hbzRefundRepositroy;
    @Autowired
    HbzBondService hbzBondService;
    @Autowired
    HbzPayRepository hbzPayRepository;
    @Autowired
    AlipayService alipayService;
    @Autowired
    HbzOrderService hbzOrderService;

    @Override
    public BaseMapper<HbzRefund, HbzRefundDTO> getMapper() {
        return hbzrefundMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzRefundRepositroy;
    }

    @Override
    public String createRefundNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimestamp = format.format(new Date());
        String refundNo;
        int init = 0;
        while (true) {
            refundNo = "RU" + "-" + currentTimestamp + "-" + StringHelper.frontCompWithZore(++init, 8);
            List<HbzRefund> list = hbzRefundRepositroy.findByRefundNo(refundNo);
            if (list == null || list.size() == 0) {
                break;
            }
        }
        return refundNo;
    }

    @Override
    public String createReqNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimestamp = format.format(new Date());
        String requestNo;
        int init = 0;
        while (true) {
            requestNo = "RQ" + "-" + currentTimestamp + "-" + StringHelper.frontCompWithZore(++init, 8);
            HbzRefund hbzRefund = hbzRefundRepositroy.findByRequestNo(requestNo);
            if (hbzRefund == null) {
                break;
            }
        }
        return requestNo;
    }

    @Override
    public void bill(HbzBillDTO bill) {
        if (bill.getBillType().equals(BillType.REFUND)) {
            HbzRefundDTO rq = new HbzRefundDTO();
            rq.setStatus("1");
            rq.setRequestNo(bill.getRequestNo());
            rq.setTradeNo(bill.getTradeNo());
            rq.setBill(0);
            rq.setRefundStatus(RefundStatus.REFUNDING);
            List<HbzRefundDTO> refunds = query(rq);
            refunds.stream().forEach(refund -> {
                HbzPay pay = hbzPayRepository.findOne(refund.getPayId());
                pay.setPayProgress(PayProgress.REFUNDED);
                hbzPayRepository.save(pay);
                refund.setBill(1);
                refund.setRefundStatus(RefundStatus.SUCCESS);
                save(refund);
                switch (pay.getBusinessType()) {
                    case BOND: {
                        HbzBondDTO queryBond = new HbzBondDTO();
                        queryBond.setStatus("1");
                        queryBond.setBondNo(pay.getBusinessNo());
                        queryBond.setBondStatuses(Arrays.asList(1));
                        List<HbzBondDTO> bonds = hbzBondService.query(queryBond);
                        bonds.forEach(bond -> {
                            bond.setBondStatus(3);
                            hbzBondService.save(bond);
                        });
                    }
                    break;
                    case ORDER: {
                        HbzOrderDTO queryOrder = new HbzOrderDTO();
                        queryOrder.setStatus("1");
                        queryOrder.setOrderNo(pay.getBusinessNo());
                        List<HbzOrderDTO> orders = hbzOrderService.query(queryOrder);
                        orders.forEach(order -> {
                            order.setOrderTrans(OrderTrans.REFUND_FINISHT);
                            hbzOrderService.save(order);
                        });
                    }
                    break;
                }

            });
        }
    }

    @Override
    public boolean refundImmediate(String businessNo, BusinessType businessType, HbzUserDTO user) {
        HbzPayDTO query = new HbzPayDTO();
        query.setStatus("1");
        query.setBusinessNo(businessNo);
        query.setBusinessType(businessType);
        query.setPayProgresses(Arrays.asList(PayProgress.SUCCESS));
        List<HbzPayDTO> pays = hbzPayService.query(query);

        if (pays != null && pays.size() == 1) {
            HbzPayDTO pay = pays.get(0);
            HbzRefundDTO refundQuery = new HbzRefundDTO();
            refundQuery.setStatus("1");
            refundQuery.setRefundStatuses(Arrays.asList(RefundStatus.SUCCESS, RefundStatus.SUBMIT, RefundStatus.REFUNDING, RefundStatus.NEW));
            refundQuery.setPayId(pay.getId());
            refundQuery.setTradeNo(pay.getTradeNo());
            Long count = count(refundQuery);
            if (count > 0L) {
                return false;
            }

            HbzRefundDTO hbzRefund = new HbzRefundDTO();
            if (user != null)
                hbzRefund.setUserId(user.getId());
            String refundNo = createRefundNo();
            String requestNo = createReqNo();
            hbzRefund.setCreateTime(Clock.systemUTC().millis());
            hbzRefund.setPayId(pay.getId());
            hbzRefund.setRefundStatus(RefundStatus.SUBMIT);
            hbzRefund.setCreateTime(System.currentTimeMillis());
            hbzRefund.setStatus("1");
            hbzRefund.setBill(0);
            hbzRefund.setRefundNo(refundNo);
            hbzRefund.setRequestNo(requestNo);
            hbzRefund = save(hbzRefund);
            refundWith(hbzRefund);
            return true;
        }
        return false;
    }

    @Override
    public boolean refundWith(HbzRefundDTO hbzRefund) {
        HbzPayDTO pay = hbzRefund.getPay();
        switch (pay.getPayType()) {
            case WebAlipay:
            case Alipay: {
                AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
                refundModel.setOutTradeNo(pay.getTradeNo());
                refundModel.setOutRequestNo(hbzRefund.getRequestNo());
                refundModel.setRefundAmount(new DecimalFormat("#.##").format(pay.getFee()));
                AlipayTradeRefundResponse response = alipayService.payRefund(refundModel);
                if (response != null) {
                    if (response.isSuccess()) {
                        hbzRefund.setRefundStatus(RefundStatus.REFUNDING);
                    } else {
                        hbzRefund.setRefundStatus(RefundStatus.FAILURE);
                    }
                }
            }
            break;
            case Wechat: {

            }
            break;
        }
        save(hbzRefund);
        return true;
    }

    @Override
    public boolean refund(String businessNo, BusinessType businessType, HbzUserDTO user) {
        HbzPayDTO query = new HbzPayDTO();
        query.setStatus("1");
        query.setBusinessNo(businessNo);
        query.setBusinessType(businessType);
        query.setPayProgresses(Arrays.asList(PayProgress.SUCCESS));
        List<HbzPayDTO> pays = hbzPayService.query(query);

        if (pays != null && pays.size() == 1) {
            HbzPayDTO pay = pays.get(0);
            HbzRefundDTO refundQuery = new HbzRefundDTO();
            refundQuery.setStatus("1");
            refundQuery.setRefundStatuses(Arrays.asList(RefundStatus.SUCCESS, RefundStatus.SUBMIT, RefundStatus.REFUNDING, RefundStatus.NEW));
            refundQuery.setPayId(pay.getId());
            refundQuery.setTradeNo(pay.getTradeNo());
            Long count = count(refundQuery);
            if (count > 0L) {
                return false;
            }

            HbzRefundDTO hbzRefund = new HbzRefundDTO();
            if (user != null)
                hbzRefund.setUserId(user.getId());
            String refundNo = createRefundNo();
            String requestNo = createReqNo();
            hbzRefund.setCreateTime(Clock.systemUTC().millis());
            hbzRefund.setPayId(pay.getId());
            hbzRefund.setRefundStatus(RefundStatus.SUBMIT);
            hbzRefund.setCreateTime(System.currentTimeMillis());
            hbzRefund.setStatus("1");
            hbzRefund.setBill(0);
            hbzRefund.setRefundNo(refundNo);
            hbzRefund.setRequestNo(requestNo);
            save(hbzRefund);
            return true;
        }
        return false;
    }
}
