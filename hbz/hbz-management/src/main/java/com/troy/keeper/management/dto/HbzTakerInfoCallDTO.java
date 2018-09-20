package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.TransType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

/**
 * @author 李奥
 * @date 2017/12/6.
 */
public class HbzTakerInfoCallDTO {
   //征集条件


    //订单的id
    private  Long id;
    //运送者类型   1-司机、2-企业、3-不限
    private Integer need;
    //注册资金
    private Double registryMoney;
    //保证金
    private Double bond;
    //星级
    private Integer starLevel;
//////////////////////////////////////////////////////////////////



    //车辆征集信息


    //接单人姓名
    private String teakUser;
    //接单人电话
    private String teakUserTelephone;
    //所属公司
    private  String organizationName;
    //接单时间 及参与时间
    private  String  teakUserTime;
    //车牌号
    private String licensePlateNumber;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNeed() {
        return need;
    }

    public void setNeed(Integer need) {
        this.need = need;
    }

    public Double getRegistryMoney() {
        return registryMoney;
    }

    public void setRegistryMoney(Double registryMoney) {
        this.registryMoney = registryMoney;
    }

    public Double getBond() {
        return bond;
    }

    public void setBond(Double bond) {
        this.bond = bond;
    }

    public Integer getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    public String getTeakUser() {
        return teakUser;
    }

    public void setTeakUser(String teakUser) {
        this.teakUser = teakUser;
    }

    public String getTeakUserTelephone() {
        return teakUserTelephone;
    }

    public void setTeakUserTelephone(String teakUserTelephone) {
        this.teakUserTelephone = teakUserTelephone;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTeakUserTime() {
        return teakUserTime;
    }

    public void setTeakUserTime(String teakUserTime) {
        this.teakUserTime = teakUserTime;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }
}
