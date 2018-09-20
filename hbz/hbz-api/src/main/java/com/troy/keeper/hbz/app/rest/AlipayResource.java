package com.troy.keeper.hbz.app.rest;

import com.alipay.api.internal.util.AlipaySignature;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.po.Warehouse;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.repository.WarehouseRepository;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/25.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/alipay")
public class AlipayResource {

    private static final Log log = LogFactory.getLog(AlipayResource.class);

    @Config("com.alipay.timeout")
    private String timeoutExpress;

    @Autowired
    private HbzPayService hbzPayService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzBondService hbzBondService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseRepository warehouseRepository;

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

    @Label("App端 - 支付宝回调接口")
    @RequestMapping(value = {"/callback"})
    public String callback(HttpServletRequest request, HttpServletResponse response) {
        log.info("支付宝回调接口成功===");
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, appPublicKey, charset, signType);
            log.info("支付宝回调签名==="+flag);
            if (flag == true) {
                //商户订单号
                String out_trade_no = params.get("out_trade_no");
                //交易状态
                String trade_status = params.get("trade_status");
                //支付宝交易号
                String trade_no = params.get("trade_no");
                if("TRADE_SUCCESS".equals(trade_status)||"TRADE_CLOSED".equals(trade_status)) {
                    log.info("支付宝回调参数商户订单号：" + out_trade_no+"交易状态：" + trade_status);
                    HbzPayDTO pay = hbzPayService.findByTradeNo(out_trade_no);
                    if (pay.getBusinessType() == BusinessType.ORDER) {
                        hbzOrderService.completeOrder(pay.getBusinessNo());
                    } else if (pay.getBusinessType() == BusinessType.BOND) {
                        HbzBondDTO bond = new HbzBondDTO();
                        bond.setBondNo(pay.getBusinessNo());
                        List<HbzBondDTO> bonds = hbzBondService.query(bond);
                        if (bonds.size() == 1) {
                            bond = bonds.get(0);
                            bond.setBondStatus(1);
                            hbzBondService.save(bond);
                        } else {
                            throw new IllegalStateException("保证金非法");
                        }
                    } else if (pay.getBusinessType() == BusinessType.WORDER) {
                        WarehouseEarnestOrder warehouseOrder = warehouseService.findByOrderNo(pay.getBusinessNo());
                        warehouseOrder.setPayStatus(WarehouseEarnestPayStatusEnum.PAID);
                        warehouseService.save(warehouseOrder);
                        Warehouse warehouse = warehouseOrder.getWarehouse();
                        warehouse.setLifecycle(4);//已租
                        warehouseRepository.save(warehouse);
                    }
                    pay.setPayProgress(PayProgress.SUCCESS);//设置支付成功状态
                    pay.setCreatedNo(trade_no);//设置支付宝交易号
                    hbzPayService.save(pay);
                } else {
                    //回调失败
                }
            } else {
                return new String("false".getBytes("UTF-8"));
            }
            response.setStatus(200);
            return "success";
        } catch (Exception e) {
            response.setStatus(501);
            return "failure";
        }
    }

}
