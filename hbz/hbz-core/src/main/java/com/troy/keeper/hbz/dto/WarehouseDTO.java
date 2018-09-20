package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.po.HbzArea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 14:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO extends BaseDTO {

    @NotNull(message = "仓储名为必填项！")
    @NotBlank(message = "请填写有效的仓储名！")
    @Length(min = 1, max = 32, message = "仓储名长度必须在不能超过32位")
    private String name;

    @NotNull(message = "仓储名为必填项！")
    @Min(value = 1, message = "容量不能小于1")
    private Double capacity;
    private Double minCapacity;  //查询用
    private Double maxCapacity;  //查询用

    @NotNull(message = "仓储单价为必填项！")
    @Min(value = 0, message = "金额必须大于0")
    private Double unitPrice;
    private Double minUnitPrice;  //查询用
    private Double maxUnitPrice;  //查询用

    @NotNull(message = "起租时长为必填项！")
    @Range(min = 1, message = "租期不得短于1个月！")
    private Integer minRentTime;
    private Integer minMinRentTime;  //查询用
    private Integer maxMinRentTime;  //查询用

    @NotNull(message = "联系人为必填项！")
    @Length(min = 1, max = 32, message = "仓储名长度必须在不能超过32位")
    @NotBlank(message = "请填写有效的联系人！")
    private String ownerName;

    @NotNull(message = "联系电话为必填项！")
    @Length(min = 11, max = 11, message = "联系电话格式非法")
    @NotBlank(message = "请填写有效的联系电话！")
    private String telephone;

    @NotNull(message = "仓储名为必填项！")
    @Length(min = 1, max = 100, message = "仓储名长度必须在不能超过100位")
    @NotBlank(message = "请填写有效的地址！")
    private String address;

    @NotNull(message = "仓储名为必填项！")
    @Range(min = 1, max = 1000, message = "纬度无效")
    private Double coordX;

    @NotNull(message = "仓储名为必填项！")
    @Range(min = 1, max = 1000, message = "纬度无效")
    private Double coordY;

    private Long publishDate;

    private String warehouseDescribe;

    private Integer lifecycle;

    private String minFormatedLastModifiedDate;  // 查询用

    private String maxFormatedLastModifiedDate;  // 查询用

    private HbzUserDTO createUserDTO;

    private String titleImageList;

    private String contentImageList;

    private HbzAreaDTO originArea;

    private String originAreaId;//查询使用

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

    public Double getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(Double minCapacity) {
        this.minCapacity = minCapacity;
    }

    public Double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getMinUnitPrice() {
        return minUnitPrice;
    }

    public void setMinUnitPrice(Double minUnitPrice) {
        this.minUnitPrice = minUnitPrice;
    }

    public Double getMaxUnitPrice() {
        return maxUnitPrice;
    }

    public void setMaxUnitPrice(Double maxUnitPrice) {
        this.maxUnitPrice = maxUnitPrice;
    }

    public Integer getMinRentTime() {
        return minRentTime;
    }

    public void setMinRentTime(Integer minRentTime) {
        this.minRentTime = minRentTime;
    }

    public Integer getMinMinRentTime() {
        return minMinRentTime;
    }

    public void setMinMinRentTime(Integer minMinRentTime) {
        this.minMinRentTime = minMinRentTime;
    }

    public Integer getMaxMinRentTime() {
        return maxMinRentTime;
    }

    public void setMaxMinRentTime(Integer maxMinRentTime) {
        this.maxMinRentTime = maxMinRentTime;
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

    public String getMinFormatedLastModifiedDate() {
        return minFormatedLastModifiedDate;
    }

    public void setMinFormatedLastModifiedDate(String minFormatedLastModifiedDate) {
        this.minFormatedLastModifiedDate = minFormatedLastModifiedDate;
    }

    public String getMaxFormatedLastModifiedDate() {
        return maxFormatedLastModifiedDate;
    }

    public void setMaxFormatedLastModifiedDate(String maxFormatedLastModifiedDate) {
        this.maxFormatedLastModifiedDate = maxFormatedLastModifiedDate;
    }

    public HbzUserDTO getCreateUserDTO() {
        return createUserDTO;
    }

    public void setCreateUserDTO(HbzUserDTO createUserDTO) {
        this.createUserDTO = createUserDTO;
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

    public HbzAreaDTO getOriginArea() {
        return originArea;
    }

    public void setOriginArea(HbzAreaDTO originArea) {
        this.originArea = originArea;
    }

    public String getOriginAreaId() {
        return originAreaId;
    }

    public void setOriginAreaId(String originAreaId) {
        this.originAreaId = originAreaId;
    }
}
