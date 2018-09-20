package com.troy.keeper.hbz.app.rest.web.payment.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.troy.keeper.core.base.dto.ResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author：YangJx
 * @Description：支付宝电脑版网页支付controller
 * @DateTime：2018/3/6 15:54
 */
@RestController
@RequestMapping("/api/web/aliPagePay")
public class AliPagePayResource {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
//    public static String app_id = "2018030102294391";
    public static String app_id = "2016072900119017";

    // 商户私钥，您的PKCS8格式RSA2私钥
//    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCMjvzfYxxO6FAidkyfmaTbipiyoU/i82dGzr82gLDVJET5dsYAIP/KtKKFzU0vd+o9fcJ6S92HuiIF2z4bxLRgY7eLI55H7xOMmMo9ErNEzkmVJzRREJV4YjI2jNmr6Q0oKkbkDM1G6eNFvJr8bfGqc+41wnw/sJUPK9d9SlNnbao4pAUaPxFt83/fJBaKtp12VYOAj4hbsK8bROOkRzpnF6f6yHSgH670O9hMP8/9+j7+s68LI1wHSfQmohUJwTMnUdeX7DNAt5slVczLx8lyvEhi5f0q6AdNK3RQNpYhYsJcWTscZ+s927bWIN0r/YsiDeS7BVEC78x+tmSrHl7zAgMBAAECggEAU0RBuXRVSMmG72N1cPbsGsK4QtodIS1geSnxTLBgEWQzGDWshlcGMWdhADLnDr/THCpsE1buHYG+tglzea/Fy3RHPz4Sfjq7Mgcxh4yUdqZeIOXgsEVKxs4lQPuf159wE+M3q9GJqgDUinN4vSHEkF2tOFvGj1RkeWBXf/mP/yyvOChH3B6OgZzsdrU/7fHNt0knfML9vJb1efXoBPGB+FlQONLy1xEvEzNgW7Vu93rVSvA3f4pHCfzA3pLTPn46v7BnY6Rr39jbdFu/s83YuieCPsIhu/EVjHdnmT0eKq5N5B2yyzXY6IOFPIk/gM2geEkyv6P6Mu7B5CWUCqqpwQKBgQDGbmhaSTkcGdSX3Ngh9gyi6n3P/vx/F17zfx8+lS8NQuyOLEaQ2QQLuO2ENVg2PcD3fB9zmnXE+IbnH+R+d9NInI7WyLx1WtJNL5faVVb2LcC9rrj0tbHDYFesnfTWZEL9SfVf6macc06wrmc8BR6pa20fWi5ZX7feacXbXxrnhQKBgQC1Vle7COWazpXsyQz61kZO40IbmO05Rd7u7jSB6HiBsE5IKRzLf8MiR5Sst4uw4EjPMzSUKzGHhVIlKg8uiV++j8CVK705Cw3LoYELu7gCUr++/cmKqOOly0w8e4XoMzQuPbpE9HDLxNMXkAYJ9E0s5au5InGe6vQPgbckYgTqFwKBgQCLzfIb2ocwM0rDjEeC9fZFOm1WBFMlv/OYHzKGR1mVQFeme69kEmJ1V9+QTH1QCgZQ19TZADSFiIyTfrbgG9By0ksZLOkKzehplWSp4YKZg/bOLbIGR5VtEA31gnykKJPCHT+SSnXtOJhyhw4ZDYpi11tUwZbyRSwoueE7BDDSeQKBgQCf3llGCZfzHUJMGQwacKJXlj+H+RDbN2BNoTAlf4AORhMdxzEdr6unIxhOFRtQest18JzgLBxkcAN1xFjSHEaOyEluubr++WHWLrKIV5zoX2+wwCnY1wCsAElb4UKy7cGJKt53QFHZM9MuqMjKKdmIRYtZkqY9BsLYkPGXX9sZTQKBgDWQhm6FPvBz391DTKHZTgDkSvleJ0tp0QJYjZZerdRxDkwBGbe45vvfOd9B08m4DinI2qx9dnzbC6i3G90/lhf5ryq0ndIl5qiBK9E2JUqXAW8Vye+pTd3XkGxkJEuLLVTbRyL9coemYeRfwc7BCHZnk2NFo5dbpTv9INZp4wS0";
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDSKlc7czcFXCkNj5ZNPksexC6tuGAXNY1XzI8agtMYjEHJ1yiwaXo7G3Vnan8ntIMp/FfIX7syVMRsYWQlcDYZX0qvJs47p8K5K6Jjj0zdJaNHmlVEljfRHUgQtZ8FfprXOIJyk6W1PbVODzPvUjsOHEtEXqSSiWahvARe7QOasxus3sDFwYugIgov7FlmIW7/NlXPfO68FXD1tku71hx55YAH3Ay7P6jSovQFuUWTTjdCbUhg3bKdqAghoWKfP3pSY0NQVZDeaTWjubZ9E03YdaeoB3R3AGkEUdSJBay/m91PE3ZE1hS63cpzYgf8KKmZCwrR/857aqIKVVCk/ASrAgMBAAECggEBAKEWlSBbUmwIVOsxCNqSCdA+PS0oqnZ/tKP8hh+i8RzavL5x7Z0ya98MT/QBnA8Z/INdCQfr4LdWY+xSxgkwNjdCZCNWkjMFhmqaOHEpcJf9dma2s+7LWWMul4M/QvFaNmMKzGn4I8aE0whO/NbOZ7eMFmEETg6HxKPpUkxUTTPv+1NozRPW4xMCUhM7guqk/z5ZwuW35s/imljoL0DL4vXQxsUfr6xWu54VAy/FhWcD/tWGNGpUD6KIorG5UB+ysz+G2lDS1zAPDnXi/8fqe+BhK9F8/VQHIRy/kn5bwLfvuEYHnKaE9XVmG4zAQpyq2SIbJCFygEc2DnrSOFW2vkECgYEA8FF5FKkr8+kpfftaw8QHdTo/+QVVsE/TaB6vbJkgGs/ndXMUyI1xC13bWQplTnZQ1n1uzvpiSXp57xVO+q5WiqeewKTsAUqhW2okpNetmFBzZ6LChGoXKr0L5XUQv1OVDlP9hAdglhIT9FAJWJtPlFij/IQPfuB5f0Juv4qpTssCgYEA3+EptRPqchbyUyEt96Ih6ouMRxJ0cGxc4/3Py62eaDEi1qAZlP7C8zD8dQ554WkhfaW2inrw65VIpJ9X94CdakWCeqZUoWeLQV+T3i8/4sLeyMUg/A1RBHzPl7VK6TbMkpTQkpZ+EmXYpX1vD5SjivW9t2e/fYI2lYS7zFt2haECgYAIVbsOVWO53O9ceDhIq+baUP/hAidbqxkY9l99M75iT62mbCxfXFB8ZW/zIaIr23yoQ8dxFEwOYloSSbnohd1oh7HA2rd4v/7ircbBlN1nd/rYsFNwxkExcUcVWT8Qc7Sqv4z+ZEccTVo10gTQ9uoGqiBd5LKo4t4z5Lj0mbmPiwKBgQCMrJLkcSsznS+PBJ45p+rf0efHENWRfsWvjOBw/xs2JQnbCqaIdLxSy0kW1I3JEaI1rVbSFQYlQZxvtKA1qN9A04R1ROaQtNtPrJrhVZczCXereBKNzmBGDZbzoLr9Z62LvW6BbYayUMSc2ABdLezFeDxJbito1dUuhkTq193VgQKBgGrpfhG91bDGx1EyBxS4f3IT/L1InJd0uFqBc26RvlHQVLkQd+P2YHYcvQR8x2m1QBPsKiqm7v8ZgLEcBcTIETx99/l8XWOrWn//aVZYNos8qB69QlHEWNZ5zKo1AKuB2QFDPw5nDsApyHMfncwbrnS+FCEJwQYn1XFDXbOzivUQ";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
//    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArWI+w2zkWHxH81hurnZSFz6ae1nHT73fjSInmZkyi4rP2Qp9rDeyuUlzqPpcCgc/ISUPzjxGoDLSAMHk5Us8iW+mN6lFzrASxdaF8Bn0AuqwxxPFB3JyL5UFXwdqsR93DHncfkmHcYugeae36S+kllSTQ24mevJ1V7wgOoGIh55mvnZOyvsN1OpKokf1YM0oVBoAogwFCz5LNvmMC8lrZkL7CZD+gJnqqPWDG2vxd9/JC7TYF+5tLlrode/4wQ3ahSkgJtZjGJqBwYweW4MjDDVcRP9LprKjMUVbRCrKYLDdAr1jfhbTU0fnCfyWO/vLxbon5//wCezqcKMHHYaOEwIDAQAB";
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSeVvH5C/OITtUtwnnxicsnW9GdSFDdJa3n0e370hyftVioPtcY02aZL2Qsl81TkZRS64rtxWew4h/2+tw1yPMxjjYcT7Nr2sFw+exKnGnw4g5Jh7vSxWfBgFf3QkXWhrHDokOinebE0HXtovv4drZNd5fh3a50l+alw6dA7O+91wfIhOI41O4a4fxU1tNFCsqs3otEyJqqv7kXMo03djhq18VBb8BQEf7Hk/WLLCYWYAAEshOeg6YKYpxWHdps99BgePVGiYcqEC1+lxm5bKRWsJQh6cMummNqwrN4RaH7AaQRdHveGnLJsRwp2JW4Lu55JdnsWfUv1X9NlK6qiswIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "https://www.baidu.com";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://www.baidu.com/";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
//    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "D:\\";

    public static String FORMAT = "json";

    @RequestMapping("/kk")
    public void kk() {
        System.err.println("999999999999999");

    }

    /**
     * 发起页面支付
     *
     * @param request
     * @param response
     */
    @RequestMapping("/createPagePay")
    public ResponseDTO createPagePay(HttpServletRequest request, HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, FORMAT, charset, alipay_public_key, sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

//        String out_trade_no = "123452226";
//        String total_amount = "1.1";
//        String subject = "车票";
//        String body = "车票描述";

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = null;
        try {
            out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //付款金额，必填
        String total_amount = null;
        try {
            total_amount = new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //订单名称，必填
        String subject = null;
        try {
            subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //商品描述，可空
        String body = null;
        try {
            body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{'out_trade_no' : '").append(out_trade_no).append("',");
        stringBuilder.append("'total_amount' : '").append(total_amount).append("',");
        stringBuilder.append("'subject' : '").append(subject).append("',");
        stringBuilder.append("'body' : '").append(body).append("',");
        stringBuilder.append("'product_code' : 'FAST_INSTANT_TRADE_PAY'}");
        alipayRequest.setBizContent(stringBuilder.toString());

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
//            try {
//                response.getWriter().write(result);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new ResponseDTO("", "", result);
    }

    /**
     * 网页支付回调接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/pagePayNotify")
    public String pagePayNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }

        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset, sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
    1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if (signVerified) {//验证成功
            //商户订单号
            try {
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //支付宝交易号
            try {
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //交易状态
            String trade_status = null;
            try {
                trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }
            return "success";
        } else {//验证失败
            return "fail";
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }
    }

    /**
     * 网页支付返回接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/pagePayReturn")
    public String pagePayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }
        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset, sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String out_trade_no = null;
            try {
                out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //支付宝交易号
            String trade_no = null;
            try {
                trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //付款金额
            String total_amount = null;
            try {
                total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount;
        } else {
            return "验签失败";
        }
    }

}
