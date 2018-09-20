package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.service.WebWechatPayService;
import io.netty.handler.codec.http.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/10 23:35
 */
public class WebWechatPayServiceImpl implements WebWechatPayService {

    private static final String APP_ID = "wx5240cc68660c1917";  //appid
    private static final String APP_SECRET = "";
    private static final String MCH_ID = "1317663601";  //商户号
    private static final String API_KEY = "";

    private static final String NOTITY_URL = "";
    private static final String UNIV_ORDER_PAY_URL = "";

    private static final String SEB_SEED_PACK_URL = "";
    private static final String CREATE_IP = "";

    public String wechatPay(String userId, String orderNo) {
        String out_trade_no = "" + System.currentTimeMillis(); //订单号 （调整为自己的生产逻辑）

        // 账号信息
        String appid = this.APP_ID;  // appid
        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret
        String mch_id = this.MCH_ID; // 商业号
        String key = this.API_KEY; // key

        String currTime = String.valueOf(System.currentTimeMillis());
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = this.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;

        // 获取发起电脑 ip
        String spbill_create_ip = this.CREATE_IP;
        // 回调接口
        String notify_url = this.NOTITY_URL;
        String trade_type = "NATIVE";

        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", "可乐");  //（调整为自己的名称）
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", "10"); //价格的单位为分
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        String sign = this.createSign("UTF-8", packageParams,key);
        packageParams.put("sign", sign);

        String requestXML = this.getRequestXml(packageParams);
        System.out.println(requestXML);

        String resXml = "";//HttpUtil.postData(this.UNIV_ORDER_PAY_URL, requestXML);

        Map map = new HashMap();//XMLUtil4jdom.doXMLParse(resXml);
        String urlCode = (String) map.get("code_url");

        return urlCode;
    }

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @return boolean
     */
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if(!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + API_KEY);

        //算出摘要
        String mysign = ""; //MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();

        //System.out.println(tenpaySign + "    " + mysign);
        return tenpaySign.equals(mysign);
    }

    /**
     * @author
     * @date 2016-4-22
     * @Description：sign签名
     * @param characterEncoding 编码格式
     * @param packageParams 请求参数
     * @return
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = "";//MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * @author
     * @date 2016-4-22
     * @Description：将请求参数转换为xml格式的string
     * @param parameters
     *            请求参数
     * @return
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

}
