package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author 李奥   装车时装车 车的信息
 * @date 2018/1/10.
 */
@Getter
@Setter
@Entity
@Table(name = "start_vehicle_information")
public class StartVehicleInformation   extends  BaseVersionLocked {


    //车辆编号
    @Column(columnDefinition = "varchar(64) comment '车辆编号'")
    private  String vehicleNumber;

    //发车时间
    @Column(columnDefinition = "bigint comment '发车时间'")
    private  Long   receiptDate;

    //预计到达时间
    @Column(columnDefinition = "bigint comment '发车时间'")
    private  Long   receiptToDate;

    //发出站区域
    @ManyToOne
    @JoinColumn(name = "originArea", columnDefinition = "bigint comment '发出站区域'")
    private HbzArea originArea;

    //到站区域
    @ManyToOne
    @JoinColumn(name = "destArea", columnDefinition = "bigint comment '到站区域'")
    private HbzArea destArea;

    //车辆运输状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '车辆运输状态'")
    private ShippingStatus shippingStatus;


    //货物信息表
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "startVehicle", columnDefinition = "bigint comment '货物信息表'")
    private List<StartVehicle> startVehicle;


    //基础车辆信息表    此处因为添加删除发车单就取消了级联操作
    @OneToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicleInformation", columnDefinition = "bigint comment '基础车辆信息表'")
    private VehicleInformation vehicleInformation;


    //在途状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15) comment '在途状态'")
    private TransitState transitState;

    //发车编号
    @Column(columnDefinition = "varchar(64) comment '发车编号'")
    private String startNumber;

    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;

    //当前站点的 机构id
    @Column( columnDefinition = "bigint comment '机构id'")
    private Long smOrgId;










}
