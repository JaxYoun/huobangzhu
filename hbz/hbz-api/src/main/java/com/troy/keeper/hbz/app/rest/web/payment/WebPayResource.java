package com.troy.keeper.hbz.app.rest.web.payment;

import com.alipay.api.domain.AlipayTradeQueryModel;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.IpHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：Web端 扫码支付
 * @DateTime：2017/12/20 17:02
 */
@Slf4j
@RestController
@RequestMapping("/api/web/pay")
public class WebPayResource {

    @Config("com.tencent.wechat.appName")
    private String appName;

    @Autowired
    private WebPayService webPayService;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzPayService hbzPayService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private AlipayService alipayService;

    /**
     * 根据传入值生支付记录
     *
     * @param payRecordDTO
     * @return
     */
    @PostMapping("/webPay")
    public ResponseDTO addPayRecord(@RequestBody @Valid PayRecordDTO payRecordDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        double amount = 0D;
        HbzUserDTO currentUser = this.hbzUserService.currentUser();

        //查询判断
        switch (payRecordDTO.getBusinessType()) {
            //订单
            case ORDER: {
                HbzOrderDTO hbzOrderDTO = this.hbzOrderService.findByOrderNo(payRecordDTO.getOrderNo());
                if (hbzOrderDTO == null || hbzOrderDTO.getStatus().equals(Const.STATUS_DISABLED)) {
                    return new ResponseDTO(Const.STATUS_ERROR, "订单不存在[" + payRecordDTO.getOrderNo() + "]", null);
                }
                HbzPayDTO hbzPayDTO = new HbzPayDTO();
                hbzPayDTO.setStatus(Const.STATUS_ENABLED);
                hbzPayDTO.setBusinessNo(hbzOrderDTO.getOrderNo());
                hbzPayDTO.setBusinessType(payRecordDTO.getBusinessType());
                List<HbzPayDTO> orderPayList = hbzPayService.query(hbzPayDTO);
                if (orderPayList != null && orderPayList.size() > 0) {
                    long waitCount = orderPayList.stream().filter(payIt -> Arrays.asList(PayProgress.APP_PAY_SUCCESS, PayProgress.APP_PAY_FAILURE).contains(payIt.getPayProgress())).count();
                    long successCount = orderPayList.stream().filter(payIt -> Arrays.asList(PayProgress.SUCCESS, PayProgress.REFUNDED).contains(payIt.getPayProgress())).count();
                    if (waitCount >= 1) {
                        return new ResponseDTO(Const.STATUS_ERROR, "正在等待订单支付结果...");
                    } else if (successCount >= 1) {
                        return new ResponseDTO(Const.STATUS_ERROR, "已经结算，无法再发起...");
                    } else {
                        List<HbzPayDTO> newHbzPayDTOList = orderPayList.stream().filter(payIt -> payIt.getPayProgress() == PayProgress.NEW).collect(Collectors.toList());
                        if (newHbzPayDTOList != null && newHbzPayDTOList.size() > 0) {
                            for (HbzPayDTO it : newHbzPayDTOList) {
                                String tradeNo = it.getTradeNo();
                                switch (it.getPayType()) {
                                    case WebAlipay: {
                                        AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
                                        alipayTradeQueryModel.setOutTradeNo(it.getTradeNo());
                                        Map<String, Object> p = alipayService.query(alipayTradeQueryModel);
                                        log.debug(JsonUtils.toJson(p));
                                        if (p != null) {

                                        } else {
                                            hbzPayService.delete(it);
                                        }
                                        break;
                                    }
                                    case WebWechat: {
                                        Map<String, Object> p = wechatService.query(tradeNo);
                                        log.debug(JsonUtils.toJson(p));
                                        if ("SUCCESS".equals(p.get("return_code")) && "SUCCESS".equals(p.get("result_code"))) {
                                            String trade_state = (String) p.get("trade_state");
                                            if (p != null && !"NOTPAY".equals(trade_state) && !"PAYERROR".equals(trade_state)) {
                                                return new ResponseDTO(Const.STATUS_ERROR, "该单已经发起支付...");
                                            } else {
                                                hbzPayService.delete(it);
                                            }
                                        }
                                        break;
                                    }
                                    default: {
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }


                break;
            }
            //企业付款
            case COST: {
                break;
            }
            //保证金
            case BOND: {
                break;
            }
            //长处租赁诚意金
            case WORDER: {
                break;
            }
            default: {
                return new ResponseDTO(Const.STATUS_ERROR, "不支持的业务类型");
            }
        }
        //添加纪录
        String tradeNo = hbzPayService.createTradeNo(payRecordDTO.getBusinessType());
        HbzPayDTO hbzPay = new HbzPayDTO();
        hbzPay.setPayType(payRecordDTO.getPayType());
        hbzPay.setBusinessNo(payRecordDTO.getOrderNo());
        hbzPay.setBusinessType(BusinessType.ORDER);
        hbzPay.setTradeNo(tradeNo);
        hbzPay.setStatus(Const.STATUS_ENABLED);
        hbzPay.setFee(amount);
        hbzPay.setPayProgress(PayProgress.NEW);
        hbzPay = hbzPayService.save(hbzPay);
        //发起支付
        switch (payRecordDTO.getPayType()) {
            case WebAlipay: {
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
                    log.error(e.getMessage(), e);
                    return new ResponseDTO(Const.STATUS_ERROR, "创建支付定单失败");
                }
//                break;
            }
            case WebWechat: {
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
                    log.error(e.getMessage(), e);
                    return new ResponseDTO(Const.STATUS_ERROR, "创建微信订单失败");
                }
//                break;
            }
            case WebUnion: {
                break;
            }
            default: {
                break;
            }
        }
        return null;
    }

    /**
     * 支付统一回调接口
     *
     * @param request
     * @return
     */
    public String hbzWebPayCallback(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = request.getReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> map = new HashMap<>();//XMLParser.getMapFromXML(buffer.toString());
        //检验API返回的数据里面的签名是否合法
        boolean isCheck = false;// Signature.checkIsSignValidFromResponseString(buffer.toString());
        //更新支付状态
        if (isCheck && "SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))) {
            if (false){//payService.returnWxpay(map)) {
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[支付成功]]></return_msg></xml>";
            }
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[支付失败]]></return_msg></xml>";
        }
        return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[检验签名不合法]]></return_msg></xml>";
    }

}