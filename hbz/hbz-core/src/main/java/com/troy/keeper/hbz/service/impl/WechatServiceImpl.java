package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.helper.DomHelper;
import com.troy.keeper.hbz.helper.IpHelper;
import com.troy.keeper.hbz.helper.MD5;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.https.Https;
import com.troy.keeper.hbz.https.RequestX509TrustManager;
import com.troy.keeper.hbz.https.TrustAnyHostnameVerifier;
import com.troy.keeper.hbz.service.WechatService;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.sys.annotation.Config;
import lombok.extern.apachecommons.CommonsLog;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/30.
 */
@CommonsLog
@Service
public class WechatServiceImpl implements WechatService {

    @Config("com.tencent.wechat.appKey")
    private String appKey;

    @Config("com.tencent.wechat.appName")
    private String appName;

    @Config("com.tencent.wechat.appId")
    private String appid;

    @Config("com.tencent.wechat.mchId")
    private String mch_id;

    @Config("com.tencent.wechat.subAppId")
    private String sub_appid;

    @Config("com.tencent.wechat.subMchId")
    private String sub_mch_id;

    @Config("com.tencent.wechat.deviceInfo")
    private String device_info;

    @Config("com.tencent.wechat.signType")
    private String sign_type;

    @Config("com.tencent.wechat.feeType")
    private String fee_type;

    @Config("com.tencent.wechat.notifyUrl")
    private String notify_url;

    @Config("com.tencent.wechat.tradeType")
    private String trade_type;

    @Config("com.tencent.wechat.limitPay")
    private String limit_pay;

    @Config("com.tencent.wechat.sceneInfo")
    private String scene_info;

    @Config("com.tencent.wechat.unifiedorder")
    private String unifiedorder;

    @Config("com.tencent.wechat.refund")
    private String refund;

    @Config("com.tencent.wechat.refund.key.path")
    private String refundKeyPath;

    @Config("com.tencent.wechat.refund.key.password")
    private String refundKeyPasswd;

    @Config("com.tencent.wechat.refund.store.path")
    private String refundStorePath;

    @Config("com.tencent.wechat.refund.store.password")
    private String refundStorePasswd;

    @Config("com.tencent.wechat.transfers")
    private String transfers;

    @Config("com.tencent.wechat.transfers_mch_appid")
    private String transfers_mch_appid;

    @Config("com.tencent.wechat.transfers_mchid")
    private String transfers_mchid;

    @Config("com.tencent.wechat.query")
    private String query;

    @Override
    public Map<String, Object> payCreateOrder(Order pay, Boolean subMch) {
        try {
            pay.setAppid(appid);
            pay.setMch_id(mch_id);
            pay.setSubAppId(sub_appid);
            pay.setSubMchid(sub_mch_id);
            pay.setDevice_info(device_info);//设备号默认传WEB
            pay.setNonce_str(StringHelper.uuid());
            pay.setSign_type(sign_type);
            pay.setLimit_pay(limit_pay);
            pay.setTrade_type(trade_type);
            pay.setFee_type(fee_type);
            pay.setNotify_url(notify_url);
            FormatedDate now = new FormatedDate();
            FormatedDate expireTime = now.addMinute(30);
            pay.setTime_start(now.getFormat("yyyyMMddHHmmss"));
            pay.setTime_expire(expireTime.getFormat("yyyyMMddHHmmss"));
            Document document = getXml(getPropertiesMap(pay));
            StringWriter out = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            String xmlText = out.toString();

            Https https = new Https();
            String resTxt = https.postData(unifiedorder, xmlText, "utf-8");
            Document resp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(resTxt.getBytes()));
            Map<String, Object> res = document2map(resp);
            if (!res.get("return_code").equals("SUCCESS") || !res.get("result_code").equals("SUCCESS")) {
                throw new RuntimeException((String) res.get("return_msg"));
            }
            Map<String, Object> ret = new LinkedHashMap<>();
            if (subMch) {
                ret.put("appid", sub_appid);
                ret.put("partnerid", sub_mch_id);
            } else {
                ret.put("appid", appid);
                ret.put("partnerid", mch_id);
            }
            ret.put("prepayid", res.get("prepay_id"));
            ret.put("package", "Sign=WXPay");
            ret.put("noncestr", StringHelper.uuid());
            ret.put("timestamp", Long.valueOf((long) System.currentTimeMillis() / 1000));
            ret = getSignedMap(ret);
            return ret;
        } catch (Exception e) {
            log.info(pay, e);
            return null;
        }
    }


    @Override
    public Map<String, Object> query(String tradeNo) {
        try {
            Map<String, Object> query = new LinkedHashMap<>();
            query.put("appid", appid);
            query.put("mch_id", mch_id);
            query.put("out_trade_no", tradeNo);
            query.put("nonce_str", StringHelper.uuid());
            Document doc = getXml(query);
            String reqBody = DomHelper.doc2str(doc);
            Https https = new Https();
            String resTxt = https.postData(this.query, reqBody, "utf-8");
            Document resp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(resTxt.getBytes()));
            Map<String, Object> res = document2map(resp);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> payRefund(Map<String, Object> refund) {
        try {
            refund.put("appid", appid);
            refund.put("mch_id", mch_id);
            refund.put("nonce_str", StringHelper.uuid());
            refund.put("sign_type", sign_type);
            refund.put("refund_fee_type", fee_type);

            Document document = getXml(refund);
            String requestXml = DomHelper.doc2str(document);
            String response = post(this.refund, requestXml.getBytes());
            Document respXml = DomHelper.str2doc(response);
            Map<String, Object> rs = document2map(respXml);
            return rs;
        } catch (Exception e) {
            log.debug("错误", e);
            return null;
        }
    }

    @Override
    public Map<String, Object> payUser(Map<String, Object> pay) {
        try {
            pay.put("mch_appid", transfers_mch_appid);
            pay.put("mchid", transfers_mchid);
            pay.put("nonce_str", StringHelper.uuid());
            pay.put("spbill_create_ip", IpHelper.getIp());

            Document doc = getXml(pay);
            String xml = DomHelper.doc2str(doc);
            String response = post(transfers, xml.getBytes());
            Map<String, Object> responseMap = document2map(DomHelper.str2doc(response));
            return responseMap;
        } catch (Exception e) {
            log.debug(e);
            return null;
        }

    }

    public Map<String, Object> document2map(Document doc) {
        Element root = doc.getDocumentElement();
        Node node = root.getFirstChild();
        Map<String, Object> map = new LinkedHashMap<>();
        while (node != null) {
            if (node.getNodeType() == 1) {
                map.put(node.getNodeName(), node.getTextContent());
            }
            node = node.getNextSibling();
        }
        return map;
    }

    private Document getXml(Map<String, Object> order) {
        Map<String, Object> signedMap = getSignedMap(order);
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement("xml");
            document.appendChild(root);
            for (String key : signedMap.keySet()) {
                Object val = signedMap.get(key);
                Element element = document.createElement(key);
                if (val instanceof Integer) {
                    element.setTextContent((Integer) val + "");
                } else if (val instanceof String) {
                    element.setTextContent((String) val);
                } else if (val instanceof Long) {
                    element.setTextContent((Long) val + "");
                } else {
                    throw new RuntimeException("无法支持");
                }
                root.appendChild(element);
            }
            return document;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> getPropertiesMap(Order bean) {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            Class<?> clazz = bean.getClass();
            while (clazz != Object.class) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAccessible()) field.setAccessible(true);
                    map.put(field.getName(), field.get(bean));
                }
                clazz = clazz.getSuperclass();
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getSignedMap(Map<String, Object> ord) {
        ord.remove("sign");
        Map<String, Object> signMap = new LinkedHashMap<>();
        List<String> keys = ord.keySet().stream().filter(a -> ord.get(a) != null).sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList());
        System.out.println(keys);
        String stringA = "";
        for (int i = 0; i < keys.size(); i++) {
            if (i > 0) stringA += "&";
            String key = keys.get(i);
            Object val = ord.get(key);
            if (val instanceof Integer) {
                stringA += key + "=" + (Integer) val;
            } else if (val instanceof String) {
                stringA += key + "=" + (String) val;
            } else if (val instanceof Long) {
                stringA += key + "=" + (Long) val;
            } else {
                throw new RuntimeException("不支持的属性类型");
            }
        }
        stringA += "&key=" + appKey;
        String sign = MD5.getMessageDigest(stringA.getBytes());
        for (int i = 0; i < keys.size(); i++) {
            signMap.put(keys.get(i), ord.get(keys.get(i)));
        }
        signMap.put("sign", sign.toUpperCase());
        return signMap;
    }

    @Override
    public Map<String, Object> bill_get(String date, String billType) throws Exception {
        String nonce_str = StringHelper.uuid().toUpperCase();
        Map<String, Object> billQuery = new HashMap<>();
        billQuery.put("appid", appid);
        billQuery.put("bill_date", date);
        billQuery.put("bill_type", billType);
        billQuery.put("mch_id", mch_id);
        billQuery.put("nonce_str", nonce_str);
        billQuery.put("sign_type", "MD5");
        Document requestXml = getXml(getSignedMap(billQuery));
        String reqBody = DomHelper.doc2str(requestXml);
        Https https = new Https();
        String resTxt = https.postData(this.query, reqBody, "utf-8");
        Document resp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(resTxt.getBytes()));
        Map<String, Object> res = document2map(resp);
        
        return null;
    }

    public String post(String url, byte[] params) throws Exception {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(WechatService.class.getResourceAsStream(refundKeyPath), refundKeyPasswd.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, refundKeyPasswd.toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        /*
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(new FileInputStream(clientStoreP12), clientStorePasswd.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();
        */

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, new TrustManager[]{new RequestX509TrustManager(WechatService.class.getResourceAsStream(refundStorePath), refundStorePasswd)}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        URL urlObj = new URL(url);
        HttpsURLConnection httpsConn = (HttpsURLConnection) urlObj.openConnection();
        httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
        httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        httpsConn.setRequestMethod("POST");
        httpsConn.setRequestProperty("accept", "*/*");
        httpsConn.setRequestProperty("connection", "Keep-Alive");
        httpsConn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        httpsConn.setRequestProperty("Content-Length", String.valueOf(params.length));

        httpsConn.setDoOutput(true);
        httpsConn.setDoInput(true);
        httpsConn.connect();

        OutputStream outputStream = httpsConn.getOutputStream();
        outputStream.write(params);
        outputStream.flush();

        InputStream inputStream = httpsConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        char[] buff = new char[1024];
        int readCnt;
        StringBuilder s = new StringBuilder();
        while ((readCnt = inputStreamReader.read(buff)) != -1) {
            s.append(buff, 0, readCnt);
        }
        return s.toString();
    }

}
