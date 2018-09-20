package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.helper.LinkaiSmsClient;
import com.troy.keeper.hbz.po.SmsRecord;
import com.troy.keeper.hbz.service.SmsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/25.
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private CommonRepository commonRepository;

    private static final Log log = LogFactory.getLog(SmsServiceImpl.class);

    @Value("${sms.url}")
    private String url;

    @Value("${sms.corpID}")
    private String corpID;

    @Value("${sms.secure}")
    private String secure;

    @Override
    public boolean send(String phoneNo, String msg) {
        boolean success = false;
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("CorpID", this.corpID);
        parameter.put("Pwd", this.secure);
        parameter.put("Mobile", phoneNo);
        parameter.put("Content", msg);

        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setMessage(msg);
        smsRecord.setPhoneNo(phoneNo);
        smsRecord.setSendStatus(Const.STATUS_ENABLED);

        try {
            /**
             String state = new LinkaiSmsClient().handle(url, "POST", parameter);
             if (state != null && state.matches("\\d+")) {
             success = true;
             smsRecord.setSendStatus(Const.STATUS_ENABLED);
             }
             */
            int state = sendSMS(phoneNo, msg, "");
            if (state >= 0) {
                success = true;
                smsRecord.setSendStatus(Const.STATUS_ENABLED);
            }

        } catch (Exception e) {
            log.debug(e);
        } finally {
            smsRecord.setSendStatus(success ? Const.STATUS_ENABLED : Const.STATUS_DISABLED);
            this.commonRepository.add(smsRecord);
        }
        return success;
    }

    /*
     * 发送方法
	 */
    public int sendSMS(String Mobile, String Content, String send_time) throws MalformedURLException, UnsupportedEncodingException {
        URL url = null;
        String password = URLEncoder.encode(this.secure, "GB2312");
        String send_content = URLEncoder.encode(Content.replaceAll("<br/>", " "), "GB2312");//发送内容
        url = new URL("http://mb345.com:999/ws/BatchSend2.aspx?CorpID=" + this.corpID + "&Pwd=" + password + "&Mobile=" + Mobile + "&Content=" + send_content + "&Cell=&SendTime=" + send_time);
        BufferedReader in = null;
        int inputLine = 0;
        try {
            System.out.println("开始发送短信手机号码为 ：" + Mobile);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            inputLine = new Integer(in.readLine()).intValue();
        } catch (Exception e) {
            System.out.println("网络异常,发送短信失败！");
            inputLine = -2;
        }
        System.out.println("结束发送短信返回值：  " + inputLine);
        return inputLine;
    }
}
