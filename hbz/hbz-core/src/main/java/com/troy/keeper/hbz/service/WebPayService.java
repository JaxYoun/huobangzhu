package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.PrepayOrderDTO;
import com.troy.keeper.hbz.vo.PrepayOrderVO;

/**
 * @Author：YangJx
 * @Description：支付Service
 * @DateTime：2017/12/20 17:06
 */
public interface WebPayService {

    /**
     * 根据订单号，调用支付平台接口，生成预支付订单，（后续会根据预支付code_url生成二维码）
     * @param orderNo
     * @return code_url
     */
    String getPreparePayCodeAndUrl(String orderNo);

    /**
     * 生成预支付订单，并保存
     * @param prepayOrderDTO
     * @return
     */
    PrepayOrderVO generatePrepayOrder(PrepayOrderDTO prepayOrderDTO);

}
