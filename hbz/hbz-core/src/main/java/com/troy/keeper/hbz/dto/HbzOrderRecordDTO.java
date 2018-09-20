package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.OrderTrans;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by leecheng on 2017/11/3.
 */

/**
 * 生成Getter、Setter、无参数构造器、类声明参数构造器
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HbzOrderRecordDTO extends BaseDTO {

    private HbzOrderDTO order;

    @QueryColumn(propName = "order.id", queryOper = "equal")
    private Long orderId;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id", queryOper = "equal")
    private Long userId;

    @QueryColumn
    private OrderTrans orderTrans;

    @QueryColumn
    private Long timeMillis;

}
