package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author 李奥  客户信息管理
 * @date 2017/12/22.
 */
@Getter
@Setter
@Entity
@Table(name = "user_information")
public class UserInformation  extends  BaseVersionLocked{

    //单位名称
    @Column(columnDefinition = "varchar(64) comment '单位名称'")
    private  String  companyName;

    //客户分类
    @Column(columnDefinition = "varchar(10) comment '客户分类'")
    private  String  userClassification;

    //联系人姓名
    @Column(columnDefinition = "varchar(20) comment '联系人姓名'")
    private String userName;

    //电话
    @Column(columnDefinition = "varchar(11) comment '电话'")
    private String  userTelephone;

    //身份证
    @Column(columnDefinition = "varchar(18) comment '身份证'")
    private  String  idCard;

    //联系地址
    @Column(columnDefinition = "varchar(128) comment '联系地址'")
    private  String  userAddress;

    //开户行
    @Column(columnDefinition = "varchar(15) comment '开户行'")
    private String bank;


    //银行账号
    @Column(columnDefinition = "bigint comment '银行账号'")
    private  Long bankAccount;



    //单位名称简评
    @Column(columnDefinition = "varchar(10) comment '单位名称简评'")
    private  String  jianpin;

    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;




}
