package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ServiceMethodType;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.WeightUnit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author 李奥    运输子系统  收货管理中的货物信息
 * @date 2017/12/29.
 */
@Getter
@Setter
@Entity
@Table(name = "cargo_information")
public class CargoInformation  extends  BaseVersionLocked {

    //物流编号
    @Column(columnDefinition = "varchar(4000) comment '物流编号'")
    private String  trackingNumber;

    //运单编号
    @Column(columnDefinition = "varchar(4000) comment '运单编号'")
    private String waybillNumber;

    //收货日期
    @Column(columnDefinition = "bigint comment '收货日期'")
    private  Long   receiptDate;

    //发出站区域
    @ManyToOne
    @JoinColumn(name = "originArea", columnDefinition = "bigint comment '发出站区域'")
    private HbzArea originArea;

    //到站区域
    @ManyToOne
    @JoinColumn(name = "destArea", columnDefinition = "bigint comment '到站区域'")
    private HbzArea destArea;

    //货物编码
    @Column(columnDefinition = "varchar(32) comment '货物编号'")
    private  String  commodityNumber;

    //货物名称
    @Column(columnDefinition = "varchar(32) comment '货物名称'")
    private String commodityName;

    //包装单位
    @Column(columnDefinition = "varchar(32) comment '包装单位'")
    private  String packageUnit;

    //数量
    @Column(columnDefinition = "int comment '数量'")
    private  Integer  amount;

    //实际重量
    @Column(columnDefinition = "long comment '实际重量'")
    private Double   weight;


    //计费重量
    @Column(columnDefinition = "long comment '计费重量'")
    private Double  billingWeight;


    //体积
    @Column(columnDefinition = "long comment '体积'")
    private Double  volume;

    //单价
    @Column(columnDefinition = "double comment '单价'")
    private Double price;


    //声明价值
    @Column(columnDefinition = "double comment '声明价值'")
    private  Double declareValue;


    //保率
    @Column(columnDefinition = "double comment '保率'")
    private Double ratio;

    //包装状态  数据字典
    @Column(columnDefinition = "varchar(32) comment '包装状态'")
    private  String packagingStatus;

    //开票员     数据字典
    @Column(columnDefinition = "varchar(32) comment '开票员'")
    private  String   billingUser;

    //是否回单
    @Column(columnDefinition = "varchar(2) comment '是否回单'")
    private  String   isReceipt;

    //等话放货
    @Column(columnDefinition = "varchar(2) comment '等话放货'")
    private String isDelivery;

    //服务方式
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '服务方式'")
    private ServiceMethodType serviceMethodType;

    //中转战
    @Column(columnDefinition = "varchar(64) comment '中转战'")
    private  String inWar;

    //回单数
    @Column(columnDefinition = "double comment '回单数'")
    private Double  receiptNumber;

    //代收款
    @Column(columnDefinition = "double comment '代收款'")
    private  Double onCollection;

    //中转费
    @Column(columnDefinition = "double comment '中转费'")
    private Double transferFee;

    //垫付货款
    @Column(columnDefinition = "double comment '垫付货款'")
    private Double advancePayment;

    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;

    //托运方
    @ManyToOne
    @JoinColumn(name = "shipperUser", columnDefinition = "bigint comment '托运方'")
    private ShipperUser shipperUser;

    //收货方
    @ManyToOne
    @JoinColumn(name = "receiverUser", columnDefinition = "bigint comment '收货方'")
    private ReceiverUser receiverUser;

    //费用表
    @ManyToOne
    @JoinColumn(name = "feeSchedule", columnDefinition = "bigint comment '费用表'")
    private FeeSchedule feeSchedule;



    //库存数量
    @Column(columnDefinition = "int comment '库存数量'")
    private Integer inventoryQuantity;

//    //运单数量
//    @Column(columnDefinition = "int comment '运单数量'")
//    private Integer waybillQuantity;

    //单个重量
    @Column(columnDefinition = "varchar(32) comment '单个重量'")
    private Double singleWeight;


    //单个体积
    @Column(columnDefinition = "double comment '单个体积'")
    private Double singleVolume;

    //收货运输状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '收货运输状态'")
    private ShippingStatus shippingStatus;

    //运送货物的类型  1--代表整车  0---代表零担
    @Column(columnDefinition = "varchar(4) comment '运送货物的类型'")
    private String goodsType;


    //该收货的信息属于那个站点 新建的收货信息
    @Column( columnDefinition = "bigint comment '机构id'")
    private Long smOrgId;


    //收运来源  1--网点自由   0---平台指定
    @Column(columnDefinition = "varchar(4) comment '收运来源'")
    private  String  come;

    //计费方式
    @Column(columnDefinition = "varchar(20) comment '计费方式'")
    private  String billingMethod;











}
