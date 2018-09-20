package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
@Getter
@Setter
public class VehicleInformationDTO {
    //id
    private Long id;

    //车牌号
    private  String numberPlate;

    //车辆编号
    private  String vehicleNumber;

    //车辆类型
    private String  vehicleType;
    private  String vehicleTypeValue;

    //车主信息///////////
    //车主姓名
    private String ownersName;

    //车主电话
    private String ownersTelephone;

    //车主地址
    private String ownerAddress;

    //车主证件号
    private String ownerNumber;

    //司机信息/////////
    //司机姓名
    private String  driverName;

    //司机电话
    private String  driverTelephone;

    //司机地址
    private String driverAddress;

    //司机证件号
    private String driverNumber;


    //车辆证件信息///////////////
    //保险日期
    private  Long  insuranceDate;

    //大验日期
    private  Long bigDate;

    //小验日期
    private Long smallDate;
/////////////////////////////////////

    //车长
    private  Integer  carLength;

    //载重
    private  Integer  cargoLoad;

    //备注
    private  String  remarks;

    //状态
    private String dataStatus;





}
