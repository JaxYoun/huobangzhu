package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.annotation.ExcelCell;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/10.
 */
@Getter
@Setter
public class StartVehicleInformationDTO {



    //装车id
    private Long   id;

    //收货信息的id
    private Long  cargoInformationId;

    //车辆信息的id
    private  Long   vehicleInformationId;

    //车辆编号
    private  String vehicleNumber;

    //发车时间
    @ExcelCell(title = "发车时间",sortNo = 8)
    private  String   receiptDate;

    //预计到达时间
    @ExcelCell(title = "预计到达时间",sortNo = 9)
    private  String   receiptToDate;

    //发出站区域
    @ExcelCell(title = "发站城市",sortNo = 6)
    private String originArea;
//    private Long  originAreaId;
    private String originAreaCode;
    //到站区域
    @ExcelCell(title = "到站城市",sortNo = 7)
    private String destArea;
//    private Long  destAreaId;
    private String destAreaCode;
    //货物信息表
    private List<StartVehicleDTO> startVehicleDTOS;

    //基础车辆信息表
    private VehicleInformationDTO vehicleInformationDTO;

    //收货运输状态
    private ShippingStatus shippingStatus;
    @ExcelCell(title = "发车单状态",sortNo = 12)
    private  String  shippingStatusValue;


    //当前站点的 机构id
    private Long smOrgId;

    //备注
    @ExcelCell(title = "备注",sortNo = 14)
    private  String  remarks;

    private LinkedList<Long> startCity;
    private LinkedList<Long> endCity;



    //省id
    private  Long provinceId;
    //市id
    private  Long cityId;
    //县id
    private  Long  countyId;

    //到站
    //省id
    private  Long provinceToId;
    //市id
    private  Long cityToId;
    //县id
    private  Long  countyToId;

    //收货时间范围查询
    private Long  smallTime;
    private Long  bigTime;

    //在途状态
    private TransitState transitState;
    @ExcelCell(title = "车辆状态",sortNo = 13)
    private String   transitStateValue;

    //发车编号
    @ExcelCell(title = "发车编号",sortNo = 1)
    private String startNumber;

    //车牌号
    @ExcelCell(title = "车牌号",sortNo = 2)
    private  String numberPlate;

    //司机姓名
    @ExcelCell(title = "司机姓名",sortNo = 4)
    private String  driverName;

    //司机电话
    @ExcelCell(title = "司机电话",sortNo = 5)
    private String  driverTelephone;

    //车辆类型
    @ExcelCell(title = "车辆类型",sortNo = 3)
    private String  vehicleType;


    //重量
    private Double singleWeight;

    //体积
    private Double singleVolume;


    //已装重量
    @ExcelCell(title = "已装重量",sortNo = 10)
    private Double installedWeight;

    //已装体积
    @ExcelCell(title = "已装体积",sortNo = 11)
    private Double installedVolume;


    //设置当前用户新建时显示 1 显示  0 不显示
    private String newlyCreatedDisplay;





}
