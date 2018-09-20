package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.IsUnload;
import com.troy.keeper.hbz.type.ServiceMethodType;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author 李奥   发车管理
 * @date 2018/1/3.
 */

@Getter
@Setter
@Entity
@Table(name = "start_vehicle")
public class StartVehicle   extends  BaseVersionLocked  {

    //车辆编号  装车编号
    @Column(columnDefinition = "varchar(64) comment '装车编号'")
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

//    //收货信息
//    @ManyToOne
//    @JoinColumn(name = "cargoInformation", columnDefinition = "bigint comment '收货信息'")
//    private CargoInformation cargoInformation;

    //货物状态  代表收货   代表卸货
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15) comment '货物状态'")
    private IsUnload isUnload;


    //货物名称
    @Column(columnDefinition = "varchar(32) comment '货物名称'")
    private String commodityName;

    //运单数量---件数
    @Column(columnDefinition = "int comment '运单数量'")
    private Integer waybillQuantity;

    //单位名称
    @Column(columnDefinition = "varchar(64) comment '单位名称'")
    private  String  receiverUserCompanyName;

    //电话
    @Column(columnDefinition = "varchar(15) comment '联系人'")
    private  String  receiverUserTelephone;

    //包装状态  数据字典
    @Column(columnDefinition = "varchar(32) comment '包装状态'")
    private  String packagingStatus;

    //重量
    @Column(columnDefinition = "double comment '重量'")
    private Double singleWeight;

    //体积
    @Column(columnDefinition = "double comment '体积'")
    private Double singleVolume;

    //已装重量
    @Column(columnDefinition = "double comment '体积'")
    private Double installedWeight;

    //已装体积
    @Column(columnDefinition = "double comment '体积'")
    private Double installedVolume;



    //付款方式   数据字典
    @Column(columnDefinition = "varchar(10) comment '付款方式'")
    private String  paymentMethod;

    //代收款
    @Column(columnDefinition = "double comment '代收款'")
    private  Double onCollection;

    //垫付货款
    @Column(columnDefinition = "double comment '垫付货款'")
    private Double advancePayment;

    //中转费
    @Column(columnDefinition = "double comment '中转费'")
    private Double transferFee;

    //服务方式
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '服务方式'")
    private ServiceMethodType serviceMethodType;

    //运单编号
    @Column(columnDefinition = "varchar(4000) comment '运单编号'")
    private String waybillNumber;

    //总运费
    @Column(columnDefinition = "double comment '总运费'")
    private Double fotalFee;

    //数量
    @Column(columnDefinition = "int comment '数量'")
    private  Integer  amount;

    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;

    //收货运输状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '收货运输状态'")
    private ShippingStatus shippingStatus;


    //物流编号
    @Column(columnDefinition = "varchar(4000) comment '物流编号'")
    private String  trackingNumber;


    //运送货物的类型  1--代表整车  0---代表零担
    @Column(columnDefinition = "varchar(4) comment '运送货物的类型'")
    private String goodsType;


    //这批货物所属车俩
    @ManyToOne
    @JoinColumn(name = "vehicleInformation", columnDefinition = "bigint comment '这批货物所属车俩'")
   private  VehicleInformation vehicleInformation;


    //货物信息
    @ManyToOne
    @JoinColumn(name = "CargoInformation", columnDefinition = "bigint comment '货物信息'")
    private  CargoInformation CargoInformation;

    //货物装车信息
    @ManyToOne
    @JoinColumn(name = "startVehicle", columnDefinition = "bigint comment '货物信息表'")
    private StartVehicleInformation startVehicleInformation;

    //发车编号
    @Column(columnDefinition = "varchar(64) comment '发车编号'")
    private String startNumber;

    //在途状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) comment '在途状态'")
    private TransitState transitState;

    //货物编码
    @Column(columnDefinition = "varchar(32) comment '货物编号'")
    private  String  commodityNumber;


    //当前站点的 机构id
    @Column( columnDefinition = "bigint comment '机构id'")
    private Long smOrgId;

    //包装单位
    @Column(columnDefinition = "varchar(32) comment '包装单位'")
    private  String packageUnit;
















}
