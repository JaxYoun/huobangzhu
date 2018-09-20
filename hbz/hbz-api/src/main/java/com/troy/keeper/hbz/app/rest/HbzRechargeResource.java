package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.HbzRechargeMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzRechargeDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.IpHelper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/12/4.
 */
@CommonsLog
@RestController
@RequestMapping("/api/recharge")
public class HbzRechargeResource {

    @Autowired
    HbzRechargeService hbzRechargeService;

    @Autowired
    HbzPayService hbzPayService;

    @Autowired
    HbzUserService hbzUserService;

    @Config("com.tencent.wechat.appName")
    private String appName;

    @Autowired
    AlipayService alipayService;

    @Autowired
    WechatService wechatService;

    //用户充值 - 如保证金等
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseDTO recharge(@RequestBody HbzRechargeMapDTO recharge) {
        HbzUserDTO user = hbzUserService.currentUser();

        HbzRechargeDTO rechargeDTO = new HbzRechargeDTO();
        String chargeNo = hbzRechargeService.makeChargeNo();
        rechargeDTO.setChargeNo(chargeNo);
        rechargeDTO.setStatus(Const.STATUS_ENABLED);
        rechargeDTO.setState(HbzRechargeService.NEW);
        rechargeDTO.setExecuteDate(new Date().getTime());
        rechargeDTO.setMoney(recharge.getMoney());
        rechargeDTO.setUser(user);
        rechargeDTO.setUserId(user.getId());
        hbzRechargeService.save(rechargeDTO);

        HbzPayDTO hbzPay = new HbzPayDTO();
        String tradeNo = hbzPayService.createTradeNo(BusinessType.BOND);
        hbzPay.setStatus(Const.STATUS_ENABLED);
        hbzPay.setPayProgress(PayProgress.NEW);
        hbzPay.setBusinessType(BusinessType.BOND);
        hbzPay.setBusinessNo(chargeNo);
        hbzPay.setFee(recharge.getMoney());
        hbzPay.setPayType(recharge.getPayType());
        hbzPay = hbzPayService.save(hbzPay);
        switch (hbzPay.getPayType()) {
            case Alipay: {
                DecimalFormat df = new DecimalFormat("######0.00");
                AlipayService.Order alipay = new AlipayService.Order();
                alipay.setBody(appName + "-" + "订单：[" + hbzPay.getBusinessNo() + "]");
                alipay.setOutTradeNo(tradeNo);
                alipay.setProductCode("QUICK_MSECURITY_PAY");
                alipay.setSubject("订单支付");
                alipay.setTotalAmount(df.format(hbzPay.getFee()));

                try {
                    Map<String, Object> alipyOrder = alipayService.payCreateOrder(alipay);
                    if (alipyOrder == null) {
                        throw new NullPointerException();
                    }
                    Map<String, Object> sdk = new LinkedHashMap<>();
                    sdk.put("tradeNo", hbzPay.getTradeNo());
                    sdk.put("alipay", alipyOrder);
                    //返回给App
                    return new ResponseDTO(Const.STATUS_OK, "OK", sdk);
                } catch (Exception e) {
                    log.error(e);
                    return new ResponseDTO(Const.STATUS_ERROR, "创建支付定单失败");
                }
            }
            case Wechat: {
                WechatService.Order wechatpay = new WechatService.Order();
                wechatpay.setBody(appName + "-" + "订单：[" + hbzPay.getBusinessNo() + "]");
                wechatpay.setOut_trade_no(tradeNo);
                wechatpay.setSpbill_create_ip(IpHelper.getIp());
                wechatpay.setTotal_fee((int) (hbzPay.getFee() * 100));

                try {
                    Map<String, Object> resp = wechatService.payCreateOrder(wechatpay, false);
                    if (resp == null) {
                        throw new NullPointerException();
                    }
                    //更新支付进度

                    Map<String, Object> sdk = new LinkedHashMap<>();
                    sdk.put("tradeNo", hbzPay.getTradeNo());
                    sdk.put("wechatpay", resp);
                    //返回给App
                    return new ResponseDTO(Const.STATUS_OK, "OK", sdk);
                } catch (Exception e) {
                    log.error(e);
                    return new ResponseDTO(Const.STATUS_ERROR, "创建微信订单失败");
                }
            }
            case Union: {
            }
            break;
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
    }

    //支付失败返回接口
    @RequestMapping(value = "/pay/submitError", method = RequestMethod.POST)
    public ResponseDTO paySubmitError(@RequestBody HbzPayDTO pay, HttpServletRequest request, HttpServletResponse response) {
        if (StringHelper.isNullOREmpty(pay.getTradeNo())) {
            return new ResponseDTO(Const.STATUS_ERROR, "没有支付单号");
        }
        HbzPayDTO payDTO = hbzPayService.findByTradeNo(pay.getTradeNo());
        if (payDTO != null) {
            payDTO.setPayProgress(PayProgress.APP_PAY_FAILURE);
            if (hbzPayService.save(payDTO) != null) {
                return new ResponseDTO(Const.STATUS_OK, "OK");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, null, null);
    }

    //支付成功
    @RequestMapping(value = "/pay/submit", method = RequestMethod.POST)
    public ResponseDTO paySubmit(@RequestBody HbzPayDTO pay, HttpServletRequest request, HttpServletResponse response) {
        if (StringHelper.isNullOREmpty(pay.getTradeNo())) {
            return new ResponseDTO(Const.STATUS_ERROR, "没有支付单号");
        }
        HbzPayDTO payDTO = hbzPayService.findByTradeNo(pay.getTradeNo());
        if (payDTO != null) {
            payDTO.setPayProgress(PayProgress.APP_PAY_SUCCESS);
            if (hbzPayService.save(payDTO) != null) {
                return new ResponseDTO(Const.STATUS_OK, "OK");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, null, null);
    }
}
