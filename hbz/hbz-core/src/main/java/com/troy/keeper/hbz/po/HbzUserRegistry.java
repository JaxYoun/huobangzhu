package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.CertificateType;
import com.troy.keeper.hbz.type.RegistryCode;
import com.troy.keeper.hbz.type.RegistryProgress;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/10/25.
 */
//注册信息表
@Getter
@Setter
@Entity
@Table(name = "hbz_user_registry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "registryCode")
public class HbzUserRegistry extends BaseVersionLocked {

    //关联用户
    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    //证件照片Base64编码
    @Column(columnDefinition = "longtext comment '证件编号'")
    @Lob
    private String certificates;

    //注册类型
    @Column(name = "registryCode", insertable = false, updatable = false, columnDefinition = "varchar(32) comment '资质注册'")
    @Enumerated(EnumType.STRING)
    private RegistryCode registryCode;

    //证件类型
    @Column(columnDefinition = "varchar(8) comment '证件类型'")
    @Enumerated(EnumType.STRING)
    private CertificateType certificateType;

    //证件编号
    @Column(columnDefinition = "varchar(64) comment '证件编号'")
    private String certificateNo;

    //名称
    @Column(columnDefinition = "varchar(1000) comment '姓名'")
    private String owerName;

    //时间
    @Column(columnDefinition = "bigint comment '创建时间'")
    private Long owerCreateTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(1000) comment '状态'")
    private RegistryProgress registryProgress;

}
