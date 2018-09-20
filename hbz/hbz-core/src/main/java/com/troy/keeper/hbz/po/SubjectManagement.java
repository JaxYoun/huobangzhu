package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author 李奥
 * @date 2018/1/21.
 */
@Getter
@Setter
@Entity
@Table(name = "subject_management")
public class SubjectManagement   extends  BaseVersionLocked {

    //科目编码
    @Column(columnDefinition = "varchar(64) comment '科目编码'")
    private  String  subjectCode;


    //科目名称
    @Column(columnDefinition = "varchar(20) comment '科目名称'")
    private String  subjectName;


    //科目类型  1---应收  0--应付
    @Column(columnDefinition = "varchar(10) comment '科目类型'")
    private String  subjectType;


    //科目状态  1--可用  0--停用
    @Column(columnDefinition = "varchar(5) comment '科目状态'")
    private String  subjectStatus;


    //备注
    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private  String  remarks;










}
