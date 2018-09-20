package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.annotation.ExcelCell;
import com.troy.keeper.hbz.type.ServiceMethodType;
import com.troy.keeper.hbz.type.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * @author 李奥  收货信息导出DTO
 * @date 2018/2/8.
 */
@Getter
@Setter
public class ExprotCargoInformationDTO {


    //物流编号
    @ExcelCell(title = "物流编号",sortNo = 1)
    private String  trackingNumber;
    //运单编号
    @ExcelCell(title = "运单编号",sortNo = 2)
    private String waybillNumber;
    //运送货物的类型  1--代表整车  0---代表零担
    @ExcelCell(title = "收件类型",sortNo = 3)
    private String goodsType;
    //发出站区域
    @ExcelCell(title = "发站城市",sortNo = 4)
    private String originArea;
    private  String originAreaCode;
    //到站区域
    @ExcelCell(title = "到站城市",sortNo = 5)
    private String destArea;
    private  String destAreaCode;
    //货物名称
    @ExcelCell(title = "货物名称",sortNo = 6)
    private String commodityName;
    //数量
    @ExcelCell(title = "数量",sortNo = 7)
    private  Integer  amount;
    //单位名称  收货方
    @ExcelCell(title = "收货单位",sortNo = 8)
    private  String  receiverUserCompanyName;
    //电话    收货方
    @ExcelCell(title = "收货电话",sortNo = 9)
    private  String  receiverUserTelephone;
    //包装单位
    private  String packageUnit;
    @ExcelCell(title = "包装",sortNo = 10)
    private String  packageUnitValue;
    //实际重量
    @ExcelCell(title = "重量",sortNo = 11)
    private Double   weight;
    //体积
    @ExcelCell(title = "体积",sortNo = 12)
    private Double  volume;
    //已付
    @ExcelCell(title = "已付",sortNo = 13)
    private Double paid;
    //提付
    @ExcelCell(title = "提付",sortNo = 14)
    private Double pay;
    //回付
    @ExcelCell(title = "回付",sortNo = 15)
    private Double payback;
    //代收款
    @ExcelCell(title = "代收款",sortNo = 16)
    private  Double onCollection;
    //垫付费用
    @ExcelCell(title = "垫付费用",sortNo = 17)
    private Double advancePayment;
    //中转战
    @ExcelCell(title = "中转战",sortNo = 18)
    private  String inWar;
    //付款方式   数据字典
    private String  paymentMethod;
    @ExcelCell(title = "付款方式",sortNo = 19)
    private  String  paymentMethodName;
    //收货运输状态
    private ShippingStatus shippingStatus;
    @ExcelCell(title = "运输状态",sortNo = 20)
    private  String   shippingStatusValue;
    //收货时间范围查询
    private Long  smallTime;
    private Long  bigTime;
    //是否回单
    private  String   isReceipt;
    //等话放货
    private String isDelivery;















}
