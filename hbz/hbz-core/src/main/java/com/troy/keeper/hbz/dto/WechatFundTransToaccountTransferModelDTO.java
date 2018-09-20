package com.troy.keeper.hbz.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Autohor: hecj
 * @Description:
 * @Date: Created in 15:16  2018/3/14.
 * @Midified By:
 */
@Getter
@Setter
@ToString
public class WechatFundTransToaccountTransferModelDTO {
    //商户订单号，需保持唯一性
    private String partnerTradeNo;
    //用户openid
    private String openid;
    //校验用户姓名选项	NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
    private String checkName;
    //收款用户姓名 可选
    private String reUserName;
    //企业付款金额，单位为分
    private Integer amount;
    //企业付款描述信息
    private String desc;
}
