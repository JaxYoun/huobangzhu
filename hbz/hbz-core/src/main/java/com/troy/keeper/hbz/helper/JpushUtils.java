package com.troy.keeper.hbz.helper;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author：YangJx
 * @Description：极光推送工具类
 * @DateTime：2018/1/23 17:22
 */
@Slf4j
@Component
public class JpushUtils {

    @Value("${jpush.appKey}")
    private String appKey;

    @Value("${jpush.masterSecret}")
    private String masterSecret;

//   private static String appKey = "06b469dabeb5f2bd8b6aeacc";
//    private static String masterSecret = "2a2e407caf545dc3d90b8a71";

    /**
     * 构建PushPayload
     *
     * @param alias
     * @param alert
     * @return
     */
    private PushPayload buildPushObject_android_ios_alias_alert(String[] alias, String title, Map<String, String> extraMap, Object alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(
                        Notification.newBuilder()
                                .addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtras(extraMap).setAlert(alert).build())
                                .addPlatformNotification(IosNotification.newBuilder().addExtras(extraMap).setAlert(alert).build())
                                .build()
                ).setOptions(
                        Options.newBuilder()
                                .setApnsProduction(false)
                                .setTimeToLive(864000L)  //10天
                                .build()
                ).build();
    }

    /**
     * 推送
     *
     * @param alias
     * @param alert
     * @return
     */
    private PushResult push(String[] alias, String title, Map<String, String> extraMap, Object alert) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        PushPayload payload = buildPushObject_android_ios_alias_alert(alias, title, extraMap, alert);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

    /**
     * 发送
     *
     * @param alias
     * @param message
     */
    public boolean jiguangPush(String[] alias, String title, Map<String, String> extraMap, Object message) {
        PushResult result = push(alias, title, extraMap, message);
        if (result != null && result.isResultOK()) {
            return true;
        } else {
            return false;
        }
    }

    /*public static boolean jiguangPushP(String[] alias, String title, Map<String, String> extraMap, Object message) {
        PushResult result = push(alias, title, extraMap, message);
        if (result != null && result.isResultOK()) {
            return true;
        } else {
            return false;
        }
    }*/

    /*public static void main(String[] args) {
        String[] list = {"hbz_18080818652", "hbz_18781983881"};
        Map<String, String> extraMap = new HashMap<>();
        extraMap.put("n_sitePushMessageId", "1");
        extraMap.put("n_title", "测试标题");
        extraMap.put("n_content", "测试正文");
        extraMap.put("n_createdDate", DateUtils.instantToString(Instant.now(), "yyyy年MM月dd日"));
        System.out.println(list.toString());
        jiguangPushP(list, "测试标题", extraMap, "测试摘要");
    }*/

}
