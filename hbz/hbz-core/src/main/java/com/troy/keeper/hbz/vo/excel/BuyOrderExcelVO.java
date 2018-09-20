package com.troy.keeper.hbz.vo.excel;

import com.troy.keeper.hbz.annotation.ExcelCell;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/9 14:42
 */
@Data
public class BuyOrderExcelVO extends BaseOrderExcelVO {

    @ExcelCell(title = "商品名称", sortNo = 50)
    private String commodityName;

    @ExcelCell(title = "详细需求", sortNo = 51)
    private String buyNeedInfo;

    @ExcelCell(title = "商品数量", sortNo = 52)
    private Long commodityCount;

    @ExcelCell(title = "货物金额", sortNo = 53)
    private Double commodityAmount;

    @ExcelCell(title = "运输金额", sortNo = 54)
    private Double remuneration;

    @ExcelCell(title = "配送地址", sortNo = 13)
    private String destAddress;

    @ExcelCell(title = "联系人", sortNo = 55)
    private String linker;

    @ExcelCell(title = "联系电话", sortNo = 56)
    private String linkTelephone;

    @ExcelCell(title = "配送限制", sortNo = 57)
    private String timeLimit;

    @ExcelCell(title = "配送时间", sortNo = 58)
    private String startTime;

    @ExcelCell(title = "超时时间", sortNo = 59)
    private String endTime;
}
