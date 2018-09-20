package com.troy.keeper.hbz.app.rest.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.rest.web.payment.PayRecordDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.hbz.type.PayProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/7 14:58
 */
@Slf4j
@RestController
@RequestMapping("/api/web/pay")
public class WebPagePayResource {

    @Autowired
    private HbzPayService hbzPayService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzBondService hbzBondService;

    @Autowired
    private WarehouseService warehouseService;

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
//    public static String notify_url = "http://ahla.sc-troy.com/api/web/pay/aliPagePayNotify";
    public static String notify_url = "http://ahla.sc-troy.com/api/alipay/callback";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://ahla.sc-troy.com/api/web/pay/aliPagePayReturn";

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

    @PostMapping("/pagePay")
    public ResponseDTO pagePay(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid PayRecordDTO payRecordDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        } else {
            switch (payRecordDTO.getPayType()) {
                //支付宝
                case WebAlipay: {
                    switch (payRecordDTO.getBusinessType()) {
                        //运输订单
                        case ORDER: {
                            HbzOrderDTO hbzOrderDTO = this.hbzOrderService.findByOrderNo(payRecordDTO.getOrderNo());
                            if (hbzOrderDTO == null) {
                                return new ResponseDTO(Const.STATUS_ERROR, "订单无效", null);
                            } else if (Const.STATUS_DISABLED.equals(hbzOrderDTO.getStatus())) {
                                return new ResponseDTO(Const.STATUS_ERROR, "订单已删除", null);
                            } else {
                                String tradeNo = hbzPayService.createTradeNo(payRecordDTO.getBusinessType());
                                HbzPayDTO hbzPay = new HbzPayDTO();
                                hbzPay.setBill(0);
                                hbzPay.setPayType(payRecordDTO.getPayType());
                                hbzPay.setBusinessNo(payRecordDTO.getOrderNo());
                                hbzPay.setBusinessType(payRecordDTO.getBusinessType());
                                hbzPay.setTradeNo(tradeNo);
                                hbzPay.setStatus(Const.STATUS_ENABLED);
                                if (hbzOrderDTO.getAmount() != null && hbzOrderDTO.getAmount() instanceof Double) {
                                    hbzPay.setFee(hbzOrderDTO.getAmount());
                                } else {
                                    hbzPay.setFee(hbzOrderDTO.getExpectedPrice());
                                }
                                hbzPay.setPayProgress(PayProgress.NEW);
                                hbzPay = hbzPayService.save(hbzPay);
                                if (hbzPay != null) {
                                    return new ResponseDTO(Const.STATUS_OK, "成功", this.createPagePay(hbzPay.getTradeNo(), hbzPay.getFee().toString(), "订单名称", "订单描述"));
                                }
                            }
                            break;
                        }
                        //保证金
                        case BOND: {
                            HbzBondDTO bondQuery = new HbzBondDTO();
                            bondQuery.setBondNo(payRecordDTO.getOrderNo());
                            bondQuery.setStatus(Const.STATUS_ENABLED);
                            bondQuery.setBondStatus(0);
                            List<HbzBondDTO> bonds = hbzBondService.query(bondQuery);
                            if (bonds == null || bonds.size() != 1) {
                                return new ResponseDTO(Const.STATUS_ERROR, "订单不存在或非法!");
                            } else {
                                HbzBondDTO bond = bonds.get(0);
                                String tradeNo = hbzPayService.createTradeNo(payRecordDTO.getBusinessType());
                                HbzPayDTO hbzPay = new HbzPayDTO();
                                hbzPay.setBill(0);
                                hbzPay.setPayType(payRecordDTO.getPayType());
                                hbzPay.setBusinessNo(payRecordDTO.getOrderNo());
                                hbzPay.setBusinessType(payRecordDTO.getBusinessType());
                                hbzPay.setTradeNo(tradeNo);
                                hbzPay.setStatus(Const.STATUS_ENABLED);
                                hbzPay.setFee(bond.getAmount());
                                hbzPay.setPayProgress(PayProgress.NEW);
                                hbzPay = hbzPayService.save(hbzPay);
                                if (hbzPay != null) {
                                    return new ResponseDTO(Const.STATUS_OK, "成功", this.createPagePay(hbzPay.getTradeNo(), hbzPay.getFee().toString(), "订单名称", "订单描述"));
                                }
                            }
                            break;
                        }
                        //仓储诚意金
                        case WORDER: {
                            WarehouseEarnestOrder warehouseOrder = warehouseService.findByOrderNo(payRecordDTO.getOrderNo());
                            if (warehouseOrder == null || warehouseOrder.getStatus().equals(Const.STATUS_DISABLED)) {
                                return new ResponseDTO(Const.STATUS_ERROR, "订单不存在[" + payRecordDTO.getOrderNo() + "]", null);
                            } else {
                                String tradeNo = hbzPayService.createTradeNo(payRecordDTO.getBusinessType());
                                HbzPayDTO hbzPay = new HbzPayDTO();
                                hbzPay.setBill(0);
                                hbzPay.setPayType(payRecordDTO.getPayType());
                                hbzPay.setBusinessNo(payRecordDTO.getOrderNo());
                                hbzPay.setBusinessType(payRecordDTO.getBusinessType());
                                hbzPay.setTradeNo(tradeNo);
                                hbzPay.setStatus(Const.STATUS_ENABLED);
                                hbzPay.setFee(warehouseOrder.getEarnestPrice());
                                hbzPay.setPayProgress(PayProgress.NEW);
                                hbzPay = hbzPayService.save(hbzPay);
                                if (hbzPay != null) {
                                    return new ResponseDTO(Const.STATUS_OK, "成功", this.createPagePay(hbzPay.getTradeNo(), hbzPay.getFee().toString(), "订单名称", "订单描述"));
                                }
                            }
                            break;
                        }
                        //企业支付
                        case COST: {
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                    break;
                }
                //微信
                case WebWechat: {
                    break;
                }
                //银联
                case WebUnion: {
                    break;
                }
                default: {
                    break;
                }
            }
            return new ResponseDTO(Const.STATUS_ERROR, "支付失败", null);
        }
    }

    /**
     * 支付宝-网页支付回调接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/aliPagePayNotify")
    public String aliPagePayNotify(HttpServletRequest request) {
        System.out.println("Notify now===================================");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
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

        if (signVerified) {//验证成功
            String out_trade_no = null;
            try {
                out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String trade_no = null;
            try {
                trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String trade_status = null;
            try {
                trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String total_amount = null;
            try {
                trade_status = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String com_app_id = null;
            try {
                trade_status = new String(request.getParameter("app_id").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!app_id.equals(com_app_id)) {
                log.error("支付宝-网页支付-回调-appId不一致！");
                return "fail";
            }
            HbzPayDTO hbzPayDTO = this.hbzPayService.findByTradeNo(out_trade_no);
            if (hbzPayDTO == null) {
                log.error("支付宝-网页支付-回调-out_trade_no不一致！");
                return "fail";
            }
            if (Double.valueOf(total_amount) != hbzPayDTO.getFee()) {
                log.error("支付宝-网页支付-回调-金额不一致！");
                return "fail";
            }
            if ("TRADE_FINISHED".equals(trade_status)) {
                return "";
            } else if ("TRADE_SUCCESS".equals(trade_status)) {
                if (hbzPayDTO.getPayProgress() == PayProgress.NEW) {
                    hbzPayDTO.setPayProgress(PayProgress.SUCCESS);
                    hbzPayDTO.setCreatedNo(trade_no);
                    this.hbzPayService.save(hbzPayDTO);
                    return "success";
                }
            } else if ("WAIT_BUYER_PAY".equals(trade_status)) {
                return "交易创建！";

            } else if ("TRADE_CLOSED".equals(trade_status)) {
                return "交易已关闭！";
            } else {
                return "交易出现未知！";
            }
        } else {//验证失败
            log.error("支付宝-网页支付-回调-验签失败！");
            return "fail";
        }
        return "";
    }

    /**
     * 网页支付返回接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/aliPagePayReturn")
    public void aliPagePayReturn(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Return now===================================");
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
            try {
                response.sendRedirect("https://www.baidu.com");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            return "trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount;
        } else {
//            return "验签失败";
        }
    }

    /**
     * 发起支付宝网页支付
     *
     * @param outTradeNo 商户订单号
     * @param totalAmout 支付金额
     * @param subject    订单名称
     * @param body       订单描述
     */
    private String createPagePay(String outTradeNo, String totalAmout, String subject, String body) {
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, FORMAT, charset, alipay_public_key, sign_type);
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{'out_trade_no' : '").append(outTradeNo).append("',");
        stringBuilder.append("'total_amount' : '").append(totalAmout).append("',");
        stringBuilder.append("'subject' : '").append(subject).append("',");
        stringBuilder.append("'body' : '").append(body).append("',");
        stringBuilder.append("'product_code' : 'FAST_INSTANT_TRADE_PAY'}");
        alipayRequest.setBizContent(stringBuilder.toString());

        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/redirect")
    public void redirect(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect("https://www.baidu.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
