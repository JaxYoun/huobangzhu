package com.troy.keeper.hbz.vo.excel;

import com.troy.keeper.hbz.annotation.ExcelCell;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/9 16:03
 */
@Data
public class BaseOrderExcelVO {

    @ExcelCell(title = "订单编号")
    public String orderNo;

    @ExcelCell(title = "订单状态", sortNo = 1)
    public String orderTrans;

    @ExcelCell(title = "订单金额", sortNo = 2)
    public Double amount;

    @ExcelCell(title = "创建人", sortNo = 3)
    public String createUser;

    @ExcelCell(title = "创建时间", sortNo = 4)
    public String createTime;

    @ExcelCell(title = "发货省", sortNo = 5)
    public String originProvince;

    @ExcelCell(title = "发货市", sortNo = 6)
    public String originCity;

    @ExcelCell(title = "发货区/县", sortNo = 7)
    public String originCounty;

    @ExcelCell(title = "发货地址门牌号", sortNo = 9)
    public String originInfo;

    @ExcelCell(title = "收货省", sortNo = 10)
    public String destProvince;

    @ExcelCell(title = "收货市", sortNo = 11)
    public String destCity;

    @ExcelCell(title = "收货区/县", sortNo = 12)
    public String destCounty;

    @ExcelCell(title = "收货地址门牌号", sortNo = 14)
    public String destInfo;

    @ExcelCell(title = "支付类型", sortNo = 15)
    public String settlementType;

    /*@ExcelCell(title = "付款方式", sortNo = 16)
    public String payType;*/

    @ExcelCell(title = "是否线下处理", sortNo = 17)
    public String offlineProcess = "否";

    @ExcelCell(title = "接单用户", sortNo = 18)
    public String takeUser;

    @ExcelCell(title = "接单时间", sortNo = 19)
    public String takeTime;

    @ExcelCell(title = "配送人", sortNo = 20)
    public String dealUser;

    @ExcelCell(title = "配送时间", sortNo = 21)
    public String dealTime;

    @ExcelCell(title = "代理人", sortNo = 22)
    public String agent;

    @ExcelCell(title = "代理时间", sortNo = 23)
    public String agentTime;
}