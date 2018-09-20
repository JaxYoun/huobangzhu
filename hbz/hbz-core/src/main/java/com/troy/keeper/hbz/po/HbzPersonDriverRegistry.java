package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ReceiveAccountType;
import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/6.
 */
//个人司机
@Getter
@Setter
@Entity
@DiscriminatorValue("PersonDriver")
public class HbzPersonDriverRegistry extends HbzUserRegistry {

    @Enumerated(EnumType.STRING)
    @Column(name = "receiveAccountType", columnDefinition = "varchar(32) comment '收款账号类型'")
    private ReceiveAccountType receiveAccountType;

    @Column(name = "receiveAccount", length = 255)
    private String receiveAccount;

    //持证照片
    @Lob
    @Column(columnDefinition = "longtext comment '持证照片'")
    private String certifiedPhoto;

    @Lob
    @Column(columnDefinition = "longtext comment '交通强制险照片'")
    private String strongInsuranceImage;

    //驾照有效期
    @Column(columnDefinition = "bigint comment '驾照有效期'")
    private Long drivingValidity;

    //车牌号
    @Column(columnDefinition = "varchar(128) comment '车牌'")
    private String licensePlateNumber;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) comment '车辆类型'")
    private TransType transType;

    //车辆长度
    @Column(columnDefinition = "double comment '车长(米)'")
    private Double transLength;

    @Column(name = "iload", columnDefinition = "double comment '负载'")
    private Double load;

    //驾驶证
    @Lob
    @Column(columnDefinition = "longtext comment '驾驶证照片'")
    private String drivingLicense;

    @Lob
    @Column(columnDefinition = "longtext comment '驾驶证背面'")
    private String drivingLicense2;

    @Lob
    @Column(columnDefinition = "longtext comment '车辆45°照'")
    private String vehicle45degreePhoto;

    //行驶证正面
    @Lob
    @Column(columnDefinition = "longtext comment '行驶证'")
    private String plyLicense;

    //行驶证另一面
    @Lob
    @Column(columnDefinition = "longtext comment '行驶背面'")
    private String plyLicense2;

}
