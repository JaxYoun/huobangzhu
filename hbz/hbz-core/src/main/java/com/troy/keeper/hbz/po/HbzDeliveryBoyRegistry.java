package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ReceiveAccountType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/17.
 */
//配送员资质
@Getter
@Setter
@Entity
@DiscriminatorValue("DeliveryBoy")
public class HbzDeliveryBoyRegistry extends HbzUserRegistry {

    @Enumerated(EnumType.STRING)
    @Column(name = "receiveAccountType", columnDefinition = "varchar(32) comment '收款账号类型'")
    private ReceiveAccountType receiveAccountType;

    @Column(name = "receiveAccount", length = 255)
    private String receiveAccount;

    //持证照片
    @Lob
    @Column(columnDefinition = "longtext comment '手拿证照片'")
    private String certifiedPhoto;

}
