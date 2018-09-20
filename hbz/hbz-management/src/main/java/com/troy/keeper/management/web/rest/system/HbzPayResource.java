package com.troy.keeper.management.web.rest.system;

import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.FundTransToaccountTransferModelDTO;
import com.troy.keeper.hbz.dto.WechatFundTransToaccountTransferModelDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.HbzDeliveryBoyRegistryRepository;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzPersonDriverRegistryRepository;
import com.troy.keeper.hbz.repository.HbzTransEnterpriseRegistryRepository;
import com.troy.keeper.hbz.service.AlipayService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.WechatService;
import com.troy.keeper.hbz.type.OrderTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Autohor: hecj
 * @Description: 平台打钱到用户
 * @Date: Created in 10:54  2018/3/28.
 * @Midified By:
 */
@RestController
@RequestMapping("/api/manager/pay")
public class HbzPayResource {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private HbzPayService hbzPayService;

    @Autowired
    private HbzOrderRepository hbzOrderRepository;

    @Autowired
    private HbzTransEnterpriseRegistryRepository hbzTransEnterpriseRegistryRepository;

    @Autowired
    private HbzPersonDriverRegistryRepository hbzPersonDriverRegistryRepository;

    @Autowired
    private HbzDeliveryBoyRegistryRepository hbzDeliveryBoyRegistryRepository;

    //支付宝平台转账给车主
    @RequestMapping(value = "/payUser", method = RequestMethod.POST)
    public ResponseDTO payUser(@RequestBody FundTransToaccountTransferModelDTO pay, HttpServletRequest request, HttpServletResponse response){
        if(StringHelper.isNullOREmpty(pay.getOrderNo())){
            return new ResponseDTO(Const.STATUS_ERROR, "订单单号不能为空");
        }
        HbzPay hbzPay = hbzPayService.findByBusinessNo(pay.getOrderNo());
        if (null==hbzPay || hbzPay.getFee()<0){
            return new ResponseDTO(Const.STATUS_ERROR, "订单为空或者退款金额有误");
        }
        HbzOrder hbzOrder = hbzOrderRepository.findFirstByOrderNo(pay.getOrderNo());
        if (hbzOrder == null){
            return new ResponseDTO(Const.STATUS_ERROR, "订单不存在");
        }
        //企业打款
        HbzTransEnterpriseRegistry hbzTransEnterpriseRegistry = hbzTransEnterpriseRegistryRepository.findByUserId(hbzOrder.getTakeUser().getId());
        //司机打款
        HbzPersonDriverRegistry hbzPersonDriverRegistry = hbzPersonDriverRegistryRepository.findByUserId(hbzOrder.getTakeUser().getId());
        //运输员打款
        HbzDeliveryBoyRegistry hbzDeliveryBoyRegistry = hbzDeliveryBoyRegistryRepository.findByUserId(hbzOrder.getTakeUser().getId());
        if (hbzTransEnterpriseRegistry == null && hbzPersonDriverRegistry ==null && hbzDeliveryBoyRegistry==null){
            return new ResponseDTO(Const.STATUS_ERROR, "退款账号或账户类型不存在");
        }else if(hbzTransEnterpriseRegistry != null){
            if ("Alipay".equals(hbzTransEnterpriseRegistry.getReceiveAccountType().name())) {
                AlipayFundTransToaccountTransferModel attfm = new AlipayFundTransToaccountTransferModel();
                attfm.setAmount(hbzPay.getFee() + "");
                attfm.setOutBizNo(String.valueOf((int) (Math.random() * (10))) + String.valueOf((int) (Math.random() * (10))) + String.valueOf(System.currentTimeMillis()));
                attfm.setPayeeType("ALIPAY_LOGONID");//退款账号类型
                attfm.setPayeeAccount(hbzTransEnterpriseRegistry.getReceiveAccount());//退款账号
                Map<String, Object> sdk = alipayService.payUser(attfm);
                if (sdk == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "FAIL", "调用支付宝接口失败");
                } else {
                    if ("Success".equals(sdk.get("msg"))) {
                        hbzOrder.setOrderTrans(OrderTrans.LIQUIDATION_COMPLETED);//打款成功设置为清算状态
                        hbzOrderRepository.save(hbzOrder);
                        return new ResponseDTO(Const.STATUS_OK, "OK", sdk);
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "FAIL", sdk.get("subMsg"));
                    }
                }
            } else if ("Wechat".equals(hbzTransEnterpriseRegistry.getReceiveAccountType().name())) {
                //微信打钱
            }
        }else if (hbzPersonDriverRegistry != null) {
            if ( "Alipay".equals(hbzPersonDriverRegistry.getReceiveAccountType().name())) {
                AlipayFundTransToaccountTransferModel attfm = new AlipayFundTransToaccountTransferModel();
                attfm.setAmount(hbzPay.getFee() + "");
                attfm.setOutBizNo(String.valueOf((int) (Math.random() * (10))) + String.valueOf((int) (Math.random() * (10))) + String.valueOf(System.currentTimeMillis()));
                attfm.setPayeeType("ALIPAY_LOGONID");//退款账号类型
                attfm.setPayeeAccount(hbzPersonDriverRegistry.getReceiveAccount());//退款账号
                Map<String, Object> sdk = alipayService.payUser(attfm);
                if (sdk == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "FAIL", "调用支付宝接口失败");
                } else {
                    if ("Success".equals(sdk.get("msg"))) {
                        hbzOrder.setOrderTrans(OrderTrans.LIQUIDATION_COMPLETED);//打款成功设置为清算状态
                        hbzOrderRepository.save(hbzOrder);
                        return new ResponseDTO(Const.STATUS_OK, "OK", sdk);
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "FAIL", sdk.get("subMsg"));
                    }
                }
            } else if ("Wechat".equals(hbzPersonDriverRegistry.getReceiveAccountType().name())) {
                //微信打钱
            }
        }else if(hbzDeliveryBoyRegistry!=null){
            if ( "Alipay".equals(hbzDeliveryBoyRegistry.getReceiveAccountType().name())) {
                AlipayFundTransToaccountTransferModel attfm = new AlipayFundTransToaccountTransferModel();
                attfm.setAmount(hbzPay.getFee() + "");
                attfm.setOutBizNo(String.valueOf((int) (Math.random() * (10))) + String.valueOf((int) (Math.random() * (10))) + String.valueOf(System.currentTimeMillis()));
                attfm.setPayeeType("ALIPAY_LOGONID");//退款账号类型
                attfm.setPayeeAccount(hbzDeliveryBoyRegistry.getReceiveAccount());//退款账号
                Map<String, Object> sdk = alipayService.payUser(attfm);
                if (sdk == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "FAIL", "调用支付宝接口失败");
                } else {
                    if ("Success".equals(sdk.get("msg"))) {
                        hbzOrder.setOrderTrans(OrderTrans.LIQUIDATION_COMPLETED);//打款成功设置为清算状态
                        hbzOrderRepository.save(hbzOrder);
                        return new ResponseDTO(Const.STATUS_OK, "OK", sdk);
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "FAIL", sdk.get("subMsg"));
                    }
                }
            } else if ("Wechat".equals(hbzDeliveryBoyRegistry.getReceiveAccountType().name())) {
                //微信打钱
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "平台打款失败");
    }

    ////微信平台转账给车主
    //@RequestMapping(value = "/payWechatUser", method = RequestMethod.POST)
    //public ResponseDTO payWechatUser(@RequestBody WechatFundTransToaccountTransferModelDTO payDTO, HttpServletRequest request, HttpServletResponse response){
    //    if(StringHelper.isNullOREmpty(payDTO.getPartnerTradeNo())){
    //        return new ResponseDTO(Const.STATUS_ERROR, "订单单号不能为空");
    //    }
    //    if(StringHelper.isNullOREmpty(payDTO.getOpenid())){
    //        return new ResponseDTO(Const.STATUS_ERROR, "收款方账户OpenID不能为空");
    //    }
    //    if(StringHelper.isNullOREmpty(payDTO.getCheckName())){
    //        return new ResponseDTO(Const.STATUS_ERROR, "校验用户姓名选项不能为空");
    //    }
    //    if(StringHelper.isNullOREmpty(payDTO.getDesc())){
    //        return new ResponseDTO(Const.STATUS_ERROR, "企业付款描述信息不能为空");
    //    }
    //    HbzPay hbzPay = hbzPayService.findByBusinessNo(payDTO.getPartnerTradeNo());
    //    if (null==hbzPay || hbzPay.getFee()<0){
    //        return new ResponseDTO(Const.STATUS_ERROR, "订单为空或者退款金额有误");
    //    }
    //    Map<String, Object> pay = new HashMap<>();
    //    pay.put("check_name",payDTO.getCheckName());
    //    if("NO_CHECK".equals(payDTO.getCheckName())){
    //        pay.put("reUserName","");
    //    }else if("FORCE_CHECK".equals(payDTO.getCheckName())){
    //        pay.put("reUserName",payDTO.getReUserName());//必须为真是姓名
    //    }
    //    pay.put("partner_trade_no",payDTO.getPartnerTradeNo());
    //    pay.put("openid",payDTO.getOpenid());
    //    pay.put("amount",hbzPay.getFee()*100);
    //    pay.put("desc",payDTO.getDesc());
    //    Map<String, Object> sdk =  wechatService.payUser(pay);
    //    if(sdk==null){
    //        return new ResponseDTO(Const.STATUS_ERROR, "FAIL", "调用微信接口失败");
    //    }else{
    //        if("SUCCESS".equals(sdk.get("returnCode"))){
    //            return new ResponseDTO(Const.STATUS_OK, "OK", sdk);
    //        }else{
    //            return new ResponseDTO(Const.STATUS_ERROR, "FAIL", sdk.get("returnMsg"));
    //        }
    //    }
    //}

}
