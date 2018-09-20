package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 16:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseVO extends BaseDTO {

    private String name;

    private Double capacity;

    private Double unitPrice;

    private Integer minRentTime;

    private String ownerName;

    private String telephone;

    private String address;

    private Double coordX;

    private Double coordY;

    private String warehouseDescribe;

    private Integer lifecycle;

    private String formatedCreateDate;

    private String formatedLastModifiedDate;

    private HbzUserVO createUserVO;

    private List<String> titleImageList;

    private List<String> contentImageList;

    private Long publishDate;

    //区域名字
    private String originAreaName;

    //区域id
    private Long originAreaId;

    private String type;//审核状态

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

    public String getFormatedCreateDate() {
        return formatedCreateDate;
    }

    public void setFormatedCreateDate(String formatedCreateDate) {
        this.formatedCreateDate = formatedCreateDate;
    }

    public String getFormatedLastModifiedDate() {
        return formatedLastModifiedDate;
    }

    public void setFormatedLastModifiedDate(String formatedLastModifiedDate) {
        this.formatedLastModifiedDate = formatedLastModifiedDate;
    }

    public HbzUserVO getCreateUserVO() {
        return createUserVO;
    }

    public void setCreateUserVO(HbzUserVO createUserVO) {
        this.createUserVO = createUserVO;
    }

    public List<String> getTitleImageList() {
        return titleImageList;
    }

    public void setTitleImageList(List<String> titleImageList) {
        this.titleImageList = titleImageList;
    }

    public List<String> getContentImageList() {
        return contentImageList;
    }

    public void setContentImageList(List<String> contentImageList) {
        this.contentImageList = contentImageList;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Long publishDate) {
        this.publishDate = publishDate;
    }
}
