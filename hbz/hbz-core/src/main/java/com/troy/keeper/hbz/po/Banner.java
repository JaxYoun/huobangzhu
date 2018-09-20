package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.consts.Const;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author：YangJx
 * @Description：Banner实体类
 * @DateTime：2018/1/15 17:25
 */
@Data
@Entity
@Table(name = "hbz_banner")
public class Banner extends BaseVersionLocked {

    @Column(columnDefinition = "char(10) comment '编号'")
    private String code;

    @Column(columnDefinition = "varchar(100) comment '名称'")
    private String name;

    @Column(columnDefinition = "int comment '位置'")
    private String location;

    @Column(columnDefinition = "int comment '跳转方式'")
    private String skipType;

    @Column(columnDefinition = "int comment '排序编号'")
    private Integer sortNo;

    @Column(columnDefinition = "varchar(200) comment '跳转url'")
    private String url;

    @Column(columnDefinition = "char(1) comment 'banner状态，0：禁用，1：可用'")
    private String ifEnable;

    @Column(columnDefinition = "varchar(200) comment '图片路径'")
    private String imagePath;

    @Column(columnDefinition = "text(1000) comment '备注'")
    private String remark;

}