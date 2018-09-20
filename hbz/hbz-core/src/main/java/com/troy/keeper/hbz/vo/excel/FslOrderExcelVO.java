package com.troy.keeper.hbz.vo.excel;

import com.troy.keeper.hbz.annotation.ExcelCell;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/9 10:25
 */
@Data
public class FslOrderExcelVO extends BaseOrderExcelVO {

    @ExcelCell(title = "货物名称", sortNo = 50)
    private String commodityName;

    @ExcelCell(title = "货物类型", sortNo = 51)
    private String commodityType;

    @ExcelCell(title = "预估重量", sortNo = 52)
    private Double commodityWeight;

    @ExcelCell(title = "重量单位", sortNo = 53)
    private String weightUnit;

    @ExcelCell(title = "预估体积", sortNo = 54)
    private Double commodityVolume;

    @ExcelCell(title = "体积单位", sortNo = 55)
    private String volumeUnit;

    @ExcelCell(title = "车辆类型", sortNo = 56)
    private String transType;

    @ExcelCell(title = "最低载重", sortNo = 57)
    private Double maxLoad;

    @ExcelCell(title = "单价", sortNo = 58)
    private Double unitPrice;

    @ExcelCell(title = "发货地址", sortNo = 8)
    private String originAddress;

    @ExcelCell(title = "收货地址", sortNo = 13)
    private String destAddress;

    @ExcelCell(title = "发货人", sortNo = 69)
    private String linkMan;

    @ExcelCell(title = "发货人电话", sortNo = 60)
    private String linkTelephone;

    @ExcelCell(title = "接运时间", sortNo = 61)
    private String orderTakeStart;

    @ExcelCell(title = "收货人", sortNo = 62)
    private String destLinker;

    @ExcelCell(title = "收货人电话", sortNo = 63)
    private String destTelephone;

    @ExcelCell(title = "送达时间限制", sortNo = 64)
    private String destlimit;

    @ExcelCell(title = "货物描述", sortNo = 65)
    private String commodityDescribe;

    @ExcelCell(title = "补充说明", sortNo = 66)
    private String linkRemark;
}
