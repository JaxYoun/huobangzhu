package com.troy.keeper.hbz.vo.excel;

import com.troy.keeper.hbz.annotation.ExcelCell;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/9 15:43
 */
@Data
public class ExOrderExcelVO extends BaseOrderExcelVO {

    @ExcelCell(title = "发货人", sortNo = 50)
    private String originLinker;

    @ExcelCell(title = "发货人电话", sortNo = 51)
    private String originTelephone;

    @ExcelCell(title = "收货人", sortNo = 52)
    private String linker;

    @ExcelCell(title = "收货人电话", sortNo = 53)
    private String telephone;

    @ExcelCell(title = "发货地址", sortNo = 8)
    private String originAddr;

    @ExcelCell(title = "送货地址", sortNo = 13)
    private String destAddr;

    @ExcelCell(title = "货物类型", sortNo = 54)
    private String commodityClass;

    @ExcelCell(title = "货物重量", sortNo = 55)
    private Double commodityWeight;

    @ExcelCell(title = "货物体积", sortNo = 56)
    private Double commodityVolume;

    @ExcelCell(title = "取货时间", sortNo = 57)
    private String orderTakeTime;

    @ExcelCell(title = "货物详情", sortNo = 58)
    private String commodityDesc;
}
