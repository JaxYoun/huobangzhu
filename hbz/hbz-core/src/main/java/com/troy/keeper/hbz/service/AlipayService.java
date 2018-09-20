package com.troy.keeper.hbz.service;

import com.alipay.api.domain.*;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.troy.keeper.hbz.sys.annotation.Event;

import java.util.Map;

/**
 * Created by leecheng on 2017/10/24.
 */
public interface AlipayService {

    @Event
    Map<String, Object> payCreateOrder(Order pay);

    @Event
    AlipayTradeRefundResponse payRefund(AlipayTradeRefundModel refund);

    AlipayTradeFastpayRefundQueryResponse refundQuery(AlipayTradeFastpayRefundQueryModel refund);

    @Event
    Map<String, Object> payUser(AlipayFundTransToaccountTransferModel pay);

    Map<String, Object> query(AlipayTradeQueryModel queryModel);

    AlipayDataDataserviceBillDownloadurlQueryResponse bill_get(AlipayDataDataserviceBillDownloadurlQueryModel billDownloadurlQueryModel);

    public static class Order {
        private String body;
        private String subject;
        private String outTradeNo;
        private String totalAmount;
        private String productCode;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
    }


}
