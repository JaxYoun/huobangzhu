package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 车辆信息类
 * @Date: Created in 11:53  2018/1/30.
 * @Midified By:
 */
@Setter
@Getter
@Entity
@Table(name = "hbz_vehicle_information")
public class HbzVehicleInformation extends BaseVersionLocked {
    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '用户'")
    private HbzUser user;
    //车牌号
    @Column(name = "plate_number", columnDefinition = "varchar(32) comment '车牌号'")
    private String plateNumber;
    //车辆类型
    @Enumerated(EnumType.STRING)
    @Column(name = "trans_type", columnDefinition = "varchar(32) comment '车辆类型'")
    private TransType transType;
    //载重
    @Column(name = "max_load", columnDefinition = "double comment '车辆载重'")
    private Double maxLoad;
    //车长
    @ManyToMany
    @JoinTable(name = "hbz_vit_ts", joinColumns = {@JoinColumn(name = "vi_id")}, inverseJoinColumns = {@JoinColumn(name = "t_id")})
    private List<HbzTransSize> transSizes;
    //车辆描述
    @Column(name = "vehicle_description", columnDefinition = "varchar(200) comment '车辆描述'")
    private String vehicleDescription;
}
