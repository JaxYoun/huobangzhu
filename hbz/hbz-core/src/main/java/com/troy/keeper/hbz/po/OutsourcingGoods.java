package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author 李奥   收货信息分包
 * @date 2018/1/15.
 */
@Getter
@Setter
@Entity
@Table(name = "outsourcing_goods")
public class OutsourcingGoods  extends  BaseVersionLocked{




    //单位名称
    @Column(columnDefinition = "varchar(64) comment '单位名称'")
    private  String  companyName;

    //司机姓名
    @Column(columnDefinition = "varchar(15) comment '司机姓名'")
    private String  driverName;

    //司机电话
    @Column(columnDefinition = "varchar(11) comment '司机电话'")
    private String  driverTelephone;

    //车牌号
    @Column(columnDefinition = "varchar(7) comment '车牌号'")
    private  String numberPlate;

    //分包时间
    @Column(columnDefinition = "bigint comment '分包时间'")
    private  Long   receiptDate;

    //分包预计到达时间
    @Column(columnDefinition = "bigint comment '分包时间'")
    private  Long   receiptToDate;


    //发车编号
    @Column(columnDefinition = "varchar(64) comment '发车编号'")
    private String startNumber;

    //发出站区域
    @ManyToOne
    @JoinColumn(name = "originArea", columnDefinition = "bigint comment '发出站区域'")
    private HbzArea originArea;

//    //到站区域
//    @ManyToOne
//    @JoinColumn(name = "destArea", columnDefinition = "bigint comment '到站区域'")
//    private HbzArea destArea;

    //外包方运单号
    @Column(columnDefinition = "varchar(128) comment '外包方运单号'")
    private  String  outNumber;



    //分包的客户信息
    @OneToOne
    @JoinColumn(name = "userInformation", columnDefinition = "bigint comment '分包的客户信息'")
    private UserInformation userInformation;



    //分包详情
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "outsourcingDetails", columnDefinition = "bigint comment '分包详情'")
    private List<OutsourcingDetails> outsourcingDetails;

    //收货运输状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '收货运输状态'")
    private ShippingStatus shippingStatus;

    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;

    //该收货的信息属于那个站点 新建的收货信息
    @Column( columnDefinition = "bigint comment '机构id'")
    private Long smOrgId;











}
