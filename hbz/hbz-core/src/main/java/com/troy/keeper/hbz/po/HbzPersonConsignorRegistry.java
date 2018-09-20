package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created by leecheng on 2017/11/6.
 */
//私人货
@Getter
@Setter
@Entity
@DiscriminatorValue(value = "PersonConsignor")
public class HbzPersonConsignorRegistry extends HbzUserRegistry {

    //持证照片
    @Lob
    @Column
    private String certifiedPhoto;

}
