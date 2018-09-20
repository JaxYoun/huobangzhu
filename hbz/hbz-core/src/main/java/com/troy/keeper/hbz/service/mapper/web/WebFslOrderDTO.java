package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.type.*;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebFslOrderDTO extends BaseDTO {

    private Long id;

    private String orderNo;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private String commodityName;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private CommodityClass commodityClass;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private Double commodityWeight;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private Double commodityVolume;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private WeightUnit weightUnit;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private VolumeUnit volumeUnit;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private TransType transType;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private Double maxLoad;

    //订单状态
    private OrderTrans orderTrans;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private String originAddress;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private String destAddress;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private String orderTakeStart;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private String linkTelephone;

    private Double originX;

    private Double originY;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private Double amount;

    //目标经度
    private Double destX;

    //目标纬度
    private Double destY;

    //结算方式
    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private SettlementType settlementType;

    //发货区域
    private String originAreaCode;

    //收货区域
    private String destAreaCode;

    //用户状态信息
    private Double locationX;
    private Double locationY;

    //分页排序参数
    private Integer page = 0;

    private Integer size = 11;

    private List<String[]> sorts;

    @NotEmpty(groups = {WebFslOrderCreateValidGroup.class})
    private String status;

    private OrderType orderType;

    private HbzUser createUser;

    private HbzUser takeUser;

    private HbzUser dealUser;

    public String getOrderTakeStart() {
        return orderTakeStart;
    }
    public void setOrderTakeStart(String orderTakeStart) {
        this.orderTakeStart = orderTakeStart;
    }
}
