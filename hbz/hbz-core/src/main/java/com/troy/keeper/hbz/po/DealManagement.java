package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.CopingStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author 李奥
 * @date 2018/1/22. 应付管理
 */
@Getter
@Setter
@Entity
@Table(name = "deal_management")
public class DealManagement  extends  BaseVersionLocked  {

    //应付编码
    @Column(columnDefinition = "varchar(128) comment '应付编码'")
    private String  coding;

    //订单来源  1--代表收运订单 0--代表杂项
    @Column(columnDefinition = "varchar(5) comment '订单来源'")
    private  String  orderSource;

    //来源编码
    @Column(columnDefinition = "varchar(128) comment '来源编码'")
    private String sourceCode;

    //科目名称
    @Column(columnDefinition = "varchar(32) comment '科目名称'")
    private String  subjectName;


    //应付金额
    @Column(columnDefinition = "double comment '应付金额'")
    private  Double  amountsPayable;

    //已付款金额
    @Column(columnDefinition = "double comment '已付款金额'")
    private Double  amountPaid;

    //应付状态
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '应付状态'")
    private CopingStatusType copingStatus;

    //收款方公司名称
    @Column(columnDefinition = "varchar(64) comment '应付状态'")
    private String  companyName;


    //联系人
    @Column(columnDefinition = "varchar(20) comment '联系人'")
    private String  contact;

    //联系方式
    @Column(columnDefinition = "varchar(11) comment '联系方式'")
    private  String contactPhone;

    //收款方银行
    @Column(columnDefinition = "varchar(64) comment '收款方银行'")
    private  String  bank;

    //收款方账号
    @Column(columnDefinition = "varchar(64) comment '收款方账号'")
    private  String  payeeAccount;


    //记录状态  1--代表正常   0---代表作废
    @Column(columnDefinition = "varchar(5) comment '记录状态'")
    private  String  recordStatus;


    //科目管理的一对多关系
    @ManyToOne
    @JoinColumn(name = "subjectManagement", columnDefinition = "bigint comment '科目管理'")
    private SubjectManagement subjectManagement;


    //收货信息表中的
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cargoInformation", columnDefinition = "bigint comment '收货信息'")
    private  CargoInformation cargoInformation;


    //该收货的信息属于那个站点 新建的收货信息
    @Column( columnDefinition = "bigint comment '机构id'")
    private Long smOrgId;



}
