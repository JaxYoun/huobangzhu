package com.troy.keeper.hbz.po;

import com.troy.keeper.system.domain.SmOrg;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leecheng on 2018/1/15.
 */


@Data
@Entity
@Table(name = "hbz_platform_organization")
@DiscriminatorValue("platform_organization_a")
public class HbzPlatformOrganization extends SmOrg {

    @Column(columnDefinition = "varchar(255) comment '组织机构编号'")
    private String organizationNo;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '网点所在区域'")
    private HbzArea addressArea;

    @Column(columnDefinition = "double comment '纬度'")
    private Double lat;

    @Column(columnDefinition = "double comment '经度'")
    private Double lng;

    @Column(columnDefinition = "varchar(128) comment '地址'")
    private String address;

    @Column(name = "platformOrganizationState", columnDefinition = "varchar(32) comment '网点状态'")
    private String state;

    @Column(columnDefinition = "int comment '类型'")
    private Integer orgType;

    @ManyToMany
    @JoinTable(name = "hbz_serviceOrg_area", joinColumns = {@JoinColumn(name = "poid")}, inverseJoinColumns = {@JoinColumn(name = "aeaaCode")})
    private List<HbzArea> serviceAreas;

}
