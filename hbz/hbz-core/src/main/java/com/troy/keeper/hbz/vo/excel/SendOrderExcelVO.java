package com.troy.keeper.hbz.vo.excel;

import com.troy.keeper.hbz.annotation.ExcelCell;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/9 15:04
 */
@Data
public class SendOrderExcelVO extends BaseOrderExcelVO {

    @ExcelCell(title = "发货人", sortNo = 50)
    private String originLinker;

    @ExcelCell(title = "发货人电话", sortNo = 51)
    private String originLinkTelephone;

    @ExcelCell(title = "取货人", sortNo = 52)
    private String linker;

    @ExcelCell(title = "取货人电话", sortNo = 53)
    private String linkTelephone;

    @ExcelCell(title = "货物名称", sortNo = 54)
    private String commodityName;

    @ExcelCell(title = "货物描述", sortNo = 55)
    private String commodityDesc;

    @ExcelCell(title = "货物重量", sortNo = 56)
    private Double commodityWeight;

    @ExcelCell(title = "货物体积", sortNo = 57)
    private Double commodityVolume;

    @ExcelCell(title = "发货地址", sortNo = 8)
    private String originAddress;

    @ExcelCell(title = "送货地址", sortNo = 13)
    private String destAddress;

    //1：不限，2：在指定时间之前，3：在指定时间之后
    @ExcelCell(title = "收获限制", sortNo = 58)
    private String takeLimit;

    @ExcelCell(title = "取货时间", sortNo = 59)
    private String orderTakeTime;

    @ExcelCell(title = "配送限制", sortNo = 60)
    private String timeLimit;

    @ExcelCell(title = "配送开始时间", sortNo = 61)
    private String startTime;

    @ExcelCell(title = "配送结束时间", sortNo = 62)
    private String endTime;
}