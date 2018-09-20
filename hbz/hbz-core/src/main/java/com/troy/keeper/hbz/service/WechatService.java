package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.sys.annotation.Event;

import java.util.Map;

/**
 * Created by leecheng on 2017/10/30.
 */
public interface WechatService {

    @Event
    public Map<String, Object> payCreateOrder(WechatService.Order pay, Boolean subMch);

    @Event
    public Map<String, Object> payRefund(Map<String, Object> refund);

    @Event
    public Map<String, Object> payUser(Map<String, Object> pay);

    public Map<String, Object> bill_get(String date, String billType) throws Exception;

    public Map<String, Object> query(String tradeNo);

    public static class Order {
        private String appid;
        private String mch_id;
        private String subAppId;
        private String subMchid;
        private String device_info;
        private String nonce_str;
        private String sign;
        private String sign_type;
        private String body;
        private String detail;
        private String attach;
        private String out_trade_no;
        private String fee_type;
        private Integer total_fee;
        private String spbill_create_ip;
        private String time_start;
        private String time_expire;
        private String goods_tag;
        private String notify_url;
        private String trade_type;
        private String limit_pay;
        private String scene_info;

        public String getAppid() {
            return appid;
        }

        public String getSubAppId() {
            return subAppId;
        }

        public void setSubAppId(String subAppId) {
            this.subAppId = subAppId;
        }

        public String getSubMchid() {
            return subMchid;
        }

        public void setSubMchid(String subMchid) {
            this.subMchid = subMchid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getDevice_info() {
            return device_info;
        }

        public void setDevice_info(String device_info) {
            this.device_info = device_info;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getAttach() {
            return attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getFee_type() {
            return fee_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public Integer getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(Integer total_fee) {
            this.total_fee = total_fee;
        }

        public String getSpbill_create_ip() {
            return spbill_create_ip;
        }

        public void setSpbill_create_ip(String spbill_create_ip) {
            this.spbill_create_ip = spbill_create_ip;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getTime_expire() {
            return time_expire;
        }

        public void setTime_expire(String time_expire) {
            this.time_expire = time_expire;
        }

        public String getGoods_tag() {
            return goods_tag;
        }

        public void setGoods_tag(String goods_tag) {
            this.goods_tag = goods_tag;
        }

        public String getNotify_url() {
            return notify_url;
        }

        public void setNotify_url(String notify_url) {
            this.notify_url = notify_url;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getLimit_pay() {
            return limit_pay;
        }

        public void setLimit_pay(String limit_pay) {
            this.limit_pay = limit_pay;
        }

        public String getScene_info() {
            return scene_info;
        }

        public void setScene_info(String scene_info) {
            this.scene_info = scene_info;
        }
    }
}
