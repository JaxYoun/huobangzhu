package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.helper.DomHelper;
import com.troy.keeper.hbz.po.Warehouse;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.repository.WarehouseRepository;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/11/1.
 */
@CrossOrigin
@Controller
@RequestMapping("/api/wechat")
public class WechatResource {

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

    @RequestMapping(value = {"/callback"})
    public void wechatCall(@RequestBody String xml, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document document = DomHelper.str2doc(xml);
        Map<String, Object> map = document2map(document);
        if (map.get("return_code").equals("SUCCESS")) {
            //支付成功
            String out_trade_no = (String) map.get("out_trade_no");
            String transaction_id = (String) map.get("transaction_id");
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
            pay.setCreatedNo(transaction_id);//设置微信支付订单号
            hbzPayService.save(pay);
        }else{
            //支付失败
        }
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("return_code", "SUCCESS");
        responseMap.put("return_msg", "OK");
        Document responseDocument = map2document(responseMap);
        response.getWriter().write(DomHelper.doc2str(responseDocument));
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

    public Document map2document(Map<String, Object> map) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement("xml");
            document.appendChild(root);
            for (String key : map.keySet()) {
                Object val = map.get(key);
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

}
