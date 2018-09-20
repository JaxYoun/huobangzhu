package com.troy.keeper.hbz.service.ali;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/1 15:33
 */
public class MyAliPayTest {

    public static AlipayClient alipayClient;

    public static String charset = "UTF-8";
    public static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDSKlc7czcFXCkNj5ZNPksexC6tuGAXNY1XzI8agtMYjEHJ1yiwaXo7G3Vnan8ntIMp/FfIX7syVMRsYWQlcDYZX0qvJs47p8K5K6Jjj0zdJaNHmlVEljfRHUgQtZ8FfprXOIJyk6W1PbVODzPvUjsOHEtEXqSSiWahvARe7QOasxus3sDFwYugIgov7FlmIW7/NlXPfO68FXD1tku71hx55YAH3Ay7P6jSovQFuUWTTjdCbUhg3bKdqAghoWKfP3pSY0NQVZDeaTWjubZ9E03YdaeoB3R3AGkEUdSJBay/m91PE3ZE1hS63cpzYgf8KKmZCwrR/857aqIKVVCk/ASrAgMBAAECggEBAKEWlSBbUmwIVOsxCNqSCdA+PS0oqnZ/tKP8hh+i8RzavL5x7Z0ya98MT/QBnA8Z/INdCQfr4LdWY+xSxgkwNjdCZCNWkjMFhmqaOHEpcJf9dma2s+7LWWMul4M/QvFaNmMKzGn4I8aE0whO/NbOZ7eMFmEETg6HxKPpUkxUTTPv+1NozRPW4xMCUhM7guqk/z5ZwuW35s/imljoL0DL4vXQxsUfr6xWu54VAy/FhWcD/tWGNGpUD6KIorG5UB+ysz+G2lDS1zAPDnXi/8fqe+BhK9F8/VQHIRy/kn5bwLfvuEYHnKaE9XVmG4zAQpyq2SIbJCFygEc2DnrSOFW2vkECgYEA8FF5FKkr8+kpfftaw8QHdTo/+QVVsE/TaB6vbJkgGs/ndXMUyI1xC13bWQplTnZQ1n1uzvpiSXp57xVO+q5WiqeewKTsAUqhW2okpNetmFBzZ6LChGoXKr0L5XUQv1OVDlP9hAdglhIT9FAJWJtPlFij/IQPfuB5f0Juv4qpTssCgYEA3+EptRPqchbyUyEt96Ih6ouMRxJ0cGxc4/3Py62eaDEi1qAZlP7C8zD8dQ554WkhfaW2inrw65VIpJ9X94CdakWCeqZUoWeLQV+T3i8/4sLeyMUg/A1RBHzPl7VK6TbMkpTQkpZ+EmXYpX1vD5SjivW9t2e/fYI2lYS7zFt2haECgYAIVbsOVWO53O9ceDhIq+baUP/hAidbqxkY9l99M75iT62mbCxfXFB8ZW/zIaIr23yoQ8dxFEwOYloSSbnohd1oh7HA2rd4v/7ircbBlN1nd/rYsFNwxkExcUcVWT8Qc7Sqv4z+ZEccTVo10gTQ9uoGqiBd5LKo4t4z5Lj0mbmPiwKBgQCMrJLkcSsznS+PBJ45p+rf0efHENWRfsWvjOBw/xs2JQnbCqaIdLxSy0kW1I3JEaI1rVbSFQYlQZxvtKA1qN9A04R1ROaQtNtPrJrhVZczCXereBKNzmBGDZbzoLr9Z62LvW6BbYayUMSc2ABdLezFeDxJbito1dUuhkTq193VgQKBgGrpfhG91bDGx1EyBxS4f3IT/L1InJd0uFqBc26RvlHQVLkQd+P2YHYcvQR8x2m1QBPsKiqm7v8ZgLEcBcTIETx99/l8XWOrWn//aVZYNos8qB69QlHEWNZ5zKo1AKuB2QFDPw5nDsApyHMfncwbrnS+FCEJwQYn1XFDXbOzivUQ";
    public static String alipayPulicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSeVvH5C/OITtUtwnnxicsnW9GdSFDdJa3n0e370hyftVioPtcY02aZL2Qsl81TkZRS64rtxWew4h/2+tw1yPMxjjYcT7Nr2sFw+exKnGnw4g5Jh7vSxWfBgFf3QkXWhrHDokOinebE0HXtovv4drZNd5fh3a50l+alw6dA7O+91wfIhOI41O4a4fxU1tNFCsqs3otEyJqqv7kXMo03djhq18VBb8BQEf7Hk/WLLCYWYAAEshOeg6YKYpxWHdps99BgePVGiYcqEC1+lxm5bKRWsJQh6cMummNqwrN4RaH7AaQRdHveGnLJsRwp2JW4Lu55JdnsWfUv1X9NlK6qiswIDAQAB";
    public static String serverUrl = "https://openapi.alipay.com/gateway.do";
    public static String sandboxServerUrl = "https://openapi.alipaydev.com/gateway.do";
    public static String appId = "2016072900119017";
    public static String format = "json";
    public static String signType = "RSA2";
    public static String notify_domain = "";

    static {
        alipayClient = new DefaultAlipayClient(sandboxServerUrl, appId, privateKey, format, charset, alipayPulicKey, signType);
    }

    public static String tradePrecreatePay(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradePrecreateResponse response = tradePrecreatePayToResponse(model, notifyUrl);
        return response.getBody();
    }

    public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.execute(request);
    }

    public static void tradePrecreatePay() {
        String subject = "Javen 支付宝扫码支付测试";
        String totalAmount = "86";
        String storeId = "123";
        String notifyUrl = notify_domain + "/alipay/precreate_notify_url";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo("123321");
        try {
            String resultStr = tradePrecreatePay(model, notifyUrl);
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            String qr_code = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
            System.out.println(qr_code);
//            renderText(qr_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        tradePrecreatePay();
    }
}
