package com.troy.keeper.hbz.app.rest;

import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.troy.keeper.core.base.dto.ResponseDTO;

import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCostDTO;
import com.troy.keeper.hbz.dto.HbzPayAccountDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayStatus;
import com.troy.keeper.hbz.type.PayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by leecheng on 2017/11/3.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/cost")
public class HbzCostResource {

    @Autowired
    private HbzCostService costService;

    @Autowired
    private HbzPayService payService;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private HbzUserService hbzUserService;
    @Autowired
    private HbzPayAccountService hbzPayAccountService;

    @RequestMapping("/query")
    public ResponseDTO query(@RequestBody HbzCostDTO query) {
        setCommonQueryParams(query);
        return new ResponseDTO(Const.STATUS_OK, "查询支付单消费", costService.query(query));
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseDTO pay(@RequestBody HbzCostDTO co) {
        HbzCostDTO cost = costService.get(co);
        HbzUserDTO user = hbzUserService.findByLogin(WebThreadHolder.currentUser());
        if (cost.getPayStatus() == PayStatus.UN_PAY) {
            HbzPayDTO pay = new HbzPayDTO();
            pay.setBusinessType(BusinessType.COST);
            pay.setBusinessNo(cost.getBusinessNo());
            pay.setPayProgress(PayProgress.NEW);
            pay.setStatus(Const.STATUS_ENABLED);
            pay.setTradeNo(payService.createTradeNo(BusinessType.COST));
            pay.setFee(cost.getAmount());
            pay.setPayType(co.getPayType());
            pay = payService.save(pay);
            HbzPayAccountDTO account = hbzPayAccountService.getDefaultPayAccountByUID(user.getId());
            if (account == null) {
                new ResponseDTO(Const.STATUS_ERROR, "未指定默认账户");
            }
            switch (co.getPayType()) {
                case Alipay: {
                    DecimalFormat df = new DecimalFormat("######0.00");
                    pay.setPayType(PayType.Alipay);
                    AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
                    model.setAmount(df.format(pay.getFee()));
                    model.setOutBizNo(pay.getTradeNo());
                    model.setPayeeAccount(account.getAccount());
                    model.setPayeeRealName("");
                }
                break;
                case Wechat: {
                }
                break;
                case Union: {
                }
                break;
                default: {
                }
                break;
            }

        }
        return new ResponseDTO(Const.STATUS_ERROR, "企业付款错误");
    }

    private void setCommonQueryParams(HbzCostDTO query) {
        query.setStatus(Const.STATUS_ENABLED);
        query.setPayStatus(PayStatus.UN_PAY);
        query.setSorts(Arrays.asList(new String[][]{{"amount", "desc"}, {"createdTime", "desc"}}));
    }
}
