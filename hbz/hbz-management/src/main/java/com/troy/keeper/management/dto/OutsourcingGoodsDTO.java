package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.annotation.ExcelCell;
import com.troy.keeper.hbz.po.OutsourcingDetails;
import com.troy.keeper.hbz.po.UserInformation;
import com.troy.keeper.hbz.type.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/15.
 */
@Getter
@Setter
public class OutsourcingGoodsDTO {

    private Long   id;

    private Long  userInformationId;

    //单位名称
    @ExcelCell(title = "公司名称",sortNo = 4)
    private  String  companyName;

    //司机姓名
    @ExcelCell(title = "司机名称",sortNo = 5)
    private String  driverName;

    //司机电话
    @ExcelCell(title = "司机电话",sortNo = 6)
    private String  driverTelephone;

    //车牌号
    @ExcelCell(title = "车牌号",sortNo = 3)
    private  String numberPlate;

    //分包时间
    @ExcelCell(title = "发车时间",sortNo = 8)
    private  String   receiptDate;

    //分包预计到达时间
    @ExcelCell(title = "预计到达时间",sortNo = 9)
    private  String   receiptToDate;

    //发车编号
    @ExcelCell(title = "发车编号", sortNo = 1)
    private String startNumber;

    //发出站区域
    @ExcelCell(title = "发站城市",sortNo = 7)
    private String originArea;
//    private Long   originAreaId;
    private  String  originAreaCode;

//    //发出站区域
    private String destArea;
    //    private Long   destAreaId;
    private  String  destAreaCode;
    //外包运单号
    @ExcelCell(title = "外包运单号",sortNo = 2)
    private  String  outNumber;

    //分包的客户信息
    private UserInformationDTO userInformationDTO;

    //分包详情
    private List<OutsourcingDetailsDTO> outsourcingDetailsDTOS;

    //运输状态
    private ShippingStatus shippingStatus;
    @ExcelCell(title = "发车单状态",sortNo = 10)
    private  String  shippingStatusValue;

    //备注
    @ExcelCell(title = "备注",sortNo = 11)
    private  String  remarks;

    //收货时间范围查询
    private Long  smallTime;
    private Long  bigTime;


    //省id
    private  Long provinceId;
    //市id
    private  Long cityId;
    //县id
    private  Long  countyId;

    private LinkedList<Long> startCity;
    private LinkedList<Long> endCity;


    //到站
    //省id
    private  Long provinceToId;
    //市id
    private  Long cityToId;
    //县id
    private  Long  countyToId;
}
