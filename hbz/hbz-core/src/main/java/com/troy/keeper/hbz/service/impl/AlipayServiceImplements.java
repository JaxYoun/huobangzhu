package com.troy.keeper.hbz.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.google.gson.Gson;
import com.troy.keeper.hbz.service.AlipayService;
import com.troy.keeper.hbz.sys.annotation.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/24.
 */
@Service
@Transactional
public class AlipayServiceImplements implements AlipayService {

    private static final Log log = LogFactory.getLog(AlipayServiceImplements.class);

    @Config("com.alipay.timeout")
    private String timeoutExpress;

    @Config("com.alipay.notify.url")
    private String notifyUrl;

    @Config("com.alipay.service.url")
    private String serviceUrl;

    @Config("com.alipay.appId")
    private String appId;

    @Config("com.alipay.privateKey")
    private String appPrivateKey;

    @Config("com.alipay.publicKey")
    private String appPublicKey;

    @Config("com.alipay.charset")
    private String charset;

    @Config("com.alipay.format")
    private String format;

    @Config("com.alipay.sign.type")
    private String signType;

    public Map<String, Object> payCreateOrder(Order pay) {
        //实例化客户端
        Map<String, Object> res = new LinkedHashMap<>();
        AlipayClient alipayClient = new DefaultAlipayClient(serviceUrl, appId, appPrivateKey, format, charset, appPublicKey, signType);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(pay.getBody());
        model.setSubject(pay.getSubject());
        model.setOutTradeNo(pay.getOutTradeNo());
        model.setTimeoutExpress(timeoutExpress);
        model.setTotalAmount(pay.getTotalAmount());
        model.setProductCode(pay.getProductCode());
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            res.put("outTradeNo", response.getOutTradeNo());
            res.put("sellerId", response.getSellerId());
            res.put("body", response.getBody());
            res.put("code", response.getCode());
            res.put("msg", response.getMsg());
            res.put("subCode", response.getSubCode());
            res.put("totalAmount", response.getTotalAmount());
            res.put("tradeNo", response.getTradeNo());
            res.put("subMsg", response.getSubMsg());
            res.put("params", response.getParams());
            return res;
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AlipayTradeRefundResponse payRefund(AlipayTradeRefundModel refund) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(serviceUrl, appId, appPrivateKey, format, charset, appPublicKey, signType);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizModel(refund);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            return response;
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> query(AlipayTradeQueryModel queryModel) {
        AlipayClient alipayClient = new DefaultAlipayClient(serviceUrl, appId, appPrivateKey, format, charset, appPublicKey, signType);
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
        alipayTradeQueryRequest.setBizModel(queryModel);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequest);
            Map<String, Object> res = new Gson().fromJson(new Gson().toJson(response), Map.class);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> payUser(AlipayFundTransToaccountTransferModel pay) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(serviceUrl, appId, appPrivateKey, format, charset, appPublicKey, signType);
            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            request.setBizModel(pay);
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            Map<String, Object> res = new Gson().fromJson(new Gson().toJson(response), Map.class);
            if (response.isSuccess()) {
                log.info("单笔转账到支付宝账户接口调用成功");
            } else {
                log.info("单笔转账到支付宝账户接口调用失败");
            }
            return res;
        } catch (Exception e) {
            log.debug(e);
        }
        return null;
    }

    @Override
    public AlipayDataDataserviceBillDownloadurlQueryResponse bill_get(AlipayDataDataserviceBillDownloadurlQueryModel billDownloadurlQueryModel) {
        AlipayClient alipayClient = new DefaultAlipayClient(serviceUrl, appId, appPrivateKey, format, charset, appPublicKey, signType);
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizModel(billDownloadurlQueryModel);
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public AlipayTradeFastpayRefundQueryResponse refundQuery(AlipayTradeFastpayRefundQueryModel refund) {
        AlipayClient alipayClient = new DefaultAlipayClient(serviceUrl, appId, appPrivateKey, format, charset, appPublicKey, signType);
        AlipayTradeFastpayRefundQueryRequest req = new AlipayTradeFastpayRefundQueryRequest();
        req.setBizModel(refund);
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(req);
            return response;
        } catch (Exception e) {
            return null;
        }
    }
}
