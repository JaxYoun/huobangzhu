package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 李奥     托运方 的用户信息
 * @date 2017/12/29.
 */
@Getter
@Setter
@Entity
@Table(name = "shipper_user")
public class ShipperUser  extends  BaseVersionLocked  {

    //单位名称
    @Column(columnDefinition = "varchar(64) comment '单位名称'")
    private  String  shipperUserCompanyName;

    //联系人
    @Column(columnDefinition = "varchar(15) comment '联系人'")
    private  String   shipperUserName;

    //电话
    @Column(columnDefinition = "varchar(11) comment '电话'")
    private  String  shipperUserTelephone;

    //地址
    @Column(columnDefinition = "varchar(128) comment '地址'")
    private  String  shipperUserAddress;
    //邮编
    @Column(columnDefinition = "varchar(6) comment '邮编'")
    private  String   shipperUserZipCode;




}
