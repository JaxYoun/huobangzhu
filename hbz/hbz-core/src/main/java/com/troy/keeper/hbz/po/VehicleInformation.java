package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author 李奥   车辆信息管理
 * @date 2017/12/22.
 */
@Getter
@Setter
@Entity
@Table(name = "vehicle_information")
public class VehicleInformation  extends  BaseVersionLocked {

    //车牌号
    @Column(columnDefinition = "varchar(7) comment '车牌号'")
    private  String numberPlate;

    //车辆编号
    @Column(columnDefinition = "varchar(64) comment '车辆编号'")
    private  String vehicleNumber;

    //车辆类型
    @Column(columnDefinition = "varchar(10) comment '车辆类型'")
    private String  vehicleType;

    //车主信息///////////
    //车主姓名
    @Column(columnDefinition = "varchar(15) comment '车主姓名'")
    private String ownersName;

    //车主电话
    @Column(columnDefinition = "varchar(11) comment '车主电话'")
    private String ownersTelephone;

    //车主地址
    @Column(columnDefinition = "varchar(128) comment '车主地址'")
    private String ownerAddress;

    //车主证件号
    @Column(columnDefinition = "varchar(32) comment '车主证件号'")
    private String ownerNumber;

    //司机信息/////////
    //司机姓名
    @Column(columnDefinition = "varchar(15) comment '司机姓名'")
    private String  driverName;

    //司机电话
    @Column(columnDefinition = "varchar(11) comment '司机电话'")
    private String  driverTelephone;

    //司机地址
    @Column(columnDefinition = "varchar(128) comment '司机地址'")
    private String driverAddress;

    //司机证件号
    @Column(columnDefinition = "varchar(32) comment '司机证件号'")
    private String driverNumber;


    //车辆证件信息///////////////
    //保险日期
    @Column(columnDefinition = "bigint comment '保险日期'")
    private  Long  insuranceDate;

    //大验日期
    @Column(columnDefinition = "bigint comment '大验日期'")
    private  Long bigDate;

    //小验日期
    @Column(columnDefinition = "bigint comment '小验日期'")
    private Long smallDate;
/////////////////////////////////////

    //车长
    @Column(columnDefinition = "int comment '车长'")
    private  Integer  carLength;


    //载重
    @Column(columnDefinition = "int comment '载重'")
    private  Integer  cargoLoad;

    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;












}
