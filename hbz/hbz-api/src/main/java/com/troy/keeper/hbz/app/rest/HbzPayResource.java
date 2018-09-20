package com.troy.keeper.hbz.app.rest;

import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.PayDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.IpHelper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzTransEnterpriseRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/28.
 */
@CommonsLog
@CrossOrigin
@RestController
@RequestMapping("/api/pay")
public class HbzPayResource {

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzPayService hbzPayService;

    @Config("com.tencent.wechat.appName")
    private String appName;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private HbzOrderRepository hbzOrderRepository;

    @Autowired
    private HbzTransEnterpriseRegistryRepository hbzTransEnterpriseRegistryRepository;

    /**
     * 支付
     *
     * @param pay
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/attach", method = RequestMethod.POST)
    public ResponseDTO pay(@RequestBody PayDTO pay, HttpServletRequest request, HttpServletResponse response) {

        if (StringHelper.isNullOREmpty(pay.getOrderNo())) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单号为空", null);
        }
        if (pay.getPayType() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "支付类型不能为空");
        }

        double amount = 0D;

        if (pay.getBusinessType() == BusinessType.ORDER) {
            HbzOrderDTO order = hbzOrderService.findByOrderNo(pay.getOrderNo());
            if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
                return new ResponseDTO(Const.STATUS_ERROR, "订单不存在[" + pay.getOrderNo() + "]", null);
            }
            amount = order.getAmount();
            ////检查此订单是否已经存在支付业务
            //HbzPayDTO queryPay = new HbzPayDTO();
            //queryPay.setStatus(Const.STATUS_ENABLED);
            //queryPay.setBusinessNo(order.getOrderNo());
            //queryPay.setBusinessType(pay.getBusinessType());
            //List<HbzPayDTO> orderPay = hbzPayService.query(queryPay);
            //if (orderPay != null && orderPay.size() > 0) {
            //    Long waitCount = orderPay.stream().filter(payIt -> Arrays.asList(PayProgress.APP_PAY_SUCCESS, PayProgress.APP_PAY_FAILURE).contains(payIt.getPayProgress())).count();
            //    Long successCount = orderPay.stream().filter(payIt -> Arrays.asList(PayProgress.SUCCESS, PayProgress.REFUNDED).contains(payIt.getPayProgress())).count();
            //    if (waitCount >= 1) {
            //        return new ResponseDTO(Const.STATUS_ERROR, "正在等待订单支付结果...");
            //    } else if (successCount >= 1) {
            //        return new ResponseDTO(Const.STATUS_ERROR, "已经结算，无法再发起...");
            //    } else {
            //        List<HbzPayDTO> pays = orderPay.stream().filter(payIt -> payIt.getPayProgress() == PayProgress.NEW).collect(Collectors.toList());
            //        if (pays != null && pays.size() > 0) {
            //            for (HbzPayDTO payIt : pays) {
            //                String tradeNo = payIt.getTradeNo();
            //                switch (payIt.getPayType()) {
            //                    case Alipay: {
            //                        AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
            //                        alipayTradeQueryModel.setOutTradeNo(payIt.getTradeNo());
            //                        Map<String, Object> p = alipayService.query(alipayTradeQueryModel);
            //                        log.debug(JsonUtils.toJson(p));
            //                        if (p != null) {
            //
            //                        } else {
            //                            hbzPayService.delete(payIt);
            //                        }
            //                    }
            //                    break;
            //                    case Wechat: {
            //                        Map<String, Object> p = wechatService.query(tradeNo);
            //                        log.debug(JsonUtils.toJson(p));
            //                        if ("SUCCESS".equals(p.get("return_code")) && "SUCCESS".equals(p.get("result_code"))) {
            //                            String trade_state = (String) p.get("trade_state");
            //                            if (p != null && !"NOTPAY".equals(trade_state) && !"PAYERROR".equals(trade_state)) {
            //                                return new ResponseDTO(Const.STATUS_ERROR, "该单已经发起支付生效...");
            //                            } else {
            //                                hbzPayService.delete(payIt);
            //                            }
            //                        }
            //                    }
            //                    break;
            //                }
            //            }
            //        }
            //    }
            //}
        } else if (pay.getBusinessType() == BusinessType.BOND) {
            HbzBondDTO bondQuery = new HbzBondDTO();
            bondQuery.setBondNo(pay.getOrderNo());
            bondQuery.setStatus("1");
            bondQuery.setBondStatus(0);
            List<HbzBondDTO> bonds = hbzBondService.query(bondQuery);
            if (bonds == null || bonds.size() != 1) {
                return new ResponseDTO(Const.STATUS_ERROR, "订单不存在或非法!");
            } else {
                HbzBondDTO bond = bonds.get(0);
                amount = bond.getAmount();
            }
        }  else if (pay.getBusinessType() == BusinessType.WORDER) {
            WarehouseEarnestOrder warehouseOrder = warehouseService.findByOrderNo(pay.getOrderNo());
            if (warehouseOrder == null || warehouseOrder.getStatus().equals(Const.STATUS_DISABLED)) {
                return new ResponseDTO(Const.STATUS_ERROR, "订单不存在[" + pay.getOrderNo() + "]", null);
            }
            amount = warehouseOrder.getEarnestPrice();
        }else {
            return new ResponseDTO(Const.STATUS_ERROR, "不支持的业务类型");
        }

        String tradeNo = hbzPayService.createTradeNo(pay.getBusinessType());
        HbzPayDTO hbzPay = new HbzPayDTO();
        hbzPay.setBill(0);
        hbzPay.setPayType(pay.getPayType());
        hbzPay.setBusinessNo(pay.getOrderNo());
        hbzPay.setBusinessType(pay.getBusinessType());
        hbzPay.setTradeNo(tradeNo);
        hbzPay.setStatus(Const.STATUS_ENABLED);
        hbzPay.setFee(amount);
        hbzPay.setPayProgress(PayProgress.NEW);

        hbzPay = hbzPayService.save(hbzPay);

        switch (pay.getPayType()) {
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
        return new ResponseDTO(Const.STATUS_ERROR, "操作失败", null);
    }

    //支付失败返回接口
    @RequestMapping(value = "/submitError", method = RequestMethod.POST)
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
    @RequestMapping(value = "/submitted", method = RequestMethod.POST)
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
