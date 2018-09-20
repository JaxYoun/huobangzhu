package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.annotation.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/14 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HbzDriverLineVO {

    @ExcelCell(name = "originArea", title = "始发地")
    private String originArea;

    @ExcelCell(name = "destArea", title = "目的地")
    private String destArea;

    @ExcelCell(name = "truckType", title = "车辆类型")
    private String truckType;

    @ExcelCell(name = "minLoad", title = "最低载重")
    private String minLoad;

    @ExcelCell(name = "truckLength", title = "车长")
    private String truckLength;

    @ExcelCell(name = "unitPrice", title = "单价")
    private String unitPrice;

}
