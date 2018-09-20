package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 车辆管理DTO
 * @Date: Created in 14:38  2018/1/30.
 * @Midified By:
 */
@Getter
@Setter
public class HbzVehicleInformationDTO {
    //车辆管理id
    private Long id;
    //用户
    //private HbzUserDTO userDTO;
    //车牌号
    private String plateNumber;
    //车辆类型
    private TransType transType;
    //车辆类型名字
    private String transTypeName;
    //载重
    private Double maxLoad;
    //车长
    private List<Double> transSizes;
    //车辆描述
    private String vehicleDescription;
    //角色类型
    //private List<Role> roleTypes;
}
