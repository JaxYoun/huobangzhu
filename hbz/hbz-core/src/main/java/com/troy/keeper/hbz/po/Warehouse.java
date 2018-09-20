package com.troy.keeper.hbz.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * @Author：YangJx
 * @Description：仓储资讯实体类
 * @DateTime：2017/12/21 14:36
 */
@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hbz_warehouse")
public class Warehouse extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(32) comment '仓储名'")
    private String name;

    @Column(columnDefinition = "double comment '仓储容量'")
    private Double capacity;

    @Column(columnDefinition = "double comment '单价'")
    private Double unitPrice;

    @Column(columnDefinition = "int comment '起租时长，按月计'")
    private Integer minRentTime;

    @Column(columnDefinition = "bigint comment '发布时间'")
    private Long publishDate;

    @Column(columnDefinition = "varchar(32) comment '联系人'")
    private String ownerName;

    @Column(columnDefinition = "varchar(32) comment '联系短话'")
    private String telephone;

    @Column(columnDefinition = "varchar(100) comment '仓储地址'")
    private String address;

    @Column(columnDefinition = "double comment '地址经度'")
    private Double coordX;

    @Column(columnDefinition = "double comment '地址纬度'")
    private Double coordY;

    @Column(columnDefinition = "varchar(300) comment '仓储描述'")
    private String warehouseDescribe;

    @Column(columnDefinition = "varchar(1000) comment '标题图片路径-数组格式'")
    private String titleImageList;

    @Column(columnDefinition = "varchar(1000) comment '内容图片路径-数组格式'")
    private String contentImageList;

    @Column(columnDefinition = "int comment '仓储资讯生命流转周期, 0：新建，1：可用, 2：过期，3：失效，4：已租'")
    private Integer lifecycle;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private HbzUser createUser;

    @ManyToOne
    @JoinColumn(name = "originAreaId", columnDefinition = "bigint comment '仓储区域'")
    private HbzArea originArea;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getMinRentTime() {
        return minRentTime;
    }

    public void setMinRentTime(Integer minRentTime) {
        this.minRentTime = minRentTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCoordX() {
        return coordX;
    }

    public void setCoordX(Double coordX) {
        this.coordX = coordX;
    }

    public Double getCoordY() {
        return coordY;
    }

    public void setCoordY(Double coordY) {
        this.coordY = coordY;
    }

    public String getWarehouseDescribe() {
        return warehouseDescribe;
    }

    public void setWarehouseDescribe(String warehouseDescribe) {
        this.warehouseDescribe = warehouseDescribe;
    }

    public Integer getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Integer lifecycle) {
        this.lifecycle = lifecycle;
    }

    public HbzUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUser createUser) {
        this.createUser = createUser;
    }

    public String getTitleImageList() {
        return titleImageList;
    }

    public void setTitleImageList(String titleImageList) {
        this.titleImageList = titleImageList;
    }

    public String getContentImageList() {
        return contentImageList;
    }

    public void setContentImageList(String contentImageList) {
        this.contentImageList = contentImageList;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Long publishDate) {
        this.publishDate = publishDate;
    }

    public HbzArea getOriginArea() {
        return originArea;
    }

    public void setOriginArea(HbzArea originArea) {
        this.originArea = originArea;
    }
}