package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.VehicleInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
public interface VehicleInformationService {


    //分页
    public Page<VehicleInformationDTO> findByCondition(VehicleInformationDTO vehicleInformationDTO, Pageable pageable);

  //新增车辆信息
    public Boolean addVehicleInformation(VehicleInformationDTO vehicleInformationDTO);

    //新增时判断车牌号是否重复
    public  Boolean  numberPlate(VehicleInformationDTO vehicleInformationDTO);

    //新增车牌号校验重复
//    public Boolean vehicleNumber(VehicleInformationDTO vehicleInformationDTO);
    //车主的电话号码重复校验
    public Boolean ownersTelephone(VehicleInformationDTO vehicleInformationDTO);
    //车主证件号
    public Boolean ownerNumber(VehicleInformationDTO vehicleInformationDTO);
    //司机电话号
    public Boolean driverTelephone(VehicleInformationDTO vehicleInformationDTO);
    ////司机证件号
    public Boolean driverNumber(VehicleInformationDTO vehicleInformationDTO);
    //修改车牌号重复校验
    public Boolean updateNumberPlate(VehicleInformationDTO vehicleInformationDTO);
    //修改编号重复
//    public Boolean updateVehicleNumber(VehicleInformationDTO vehicleInformationDTO);
    //修改车主电话
    public Boolean updateOwnersTelephone(VehicleInformationDTO vehicleInformationDTO);
    //修改车主证件号
    public Boolean updateOwnerNumber(VehicleInformationDTO vehicleInformationDTO);
    //修改司机电话
    public Boolean updateDriverTelephone(VehicleInformationDTO vehicleInformationDTO);
    //修改司机证件号
    public Boolean updateDriverNumber(VehicleInformationDTO vehicleInformationDTO);





    //修改车辆信息
    public  Boolean updateVehicleInformation(VehicleInformationDTO vehicleInformationDTO);

    //删除车辆信息
    public  Boolean  deleteVehicleInformation(VehicleInformationDTO vehicleInformationDTO);

}
