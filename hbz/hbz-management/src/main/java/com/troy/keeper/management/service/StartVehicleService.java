package com.troy.keeper.management.service;

import com.troy.keeper.hbz.po.StartVehicle;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/3.
 */
public interface StartVehicleService {

    //查询车辆信息中所有车辆的 车辆编号
    public List<StartVehicleDTO> findVehicleNumber();

//    // //通过货物路线，车辆编号，发车时间查询  发车管理中的货物信息
//    public List<StartVehicleDTO> findAllStartVehicle(StartVehicleDTO startVehicleDTO);


//     public Boolean  addBatchStartVehicle(List<StartVehicleDTO> startVehicleDTO);

//    //部分货物信息 装车
//    public  Boolean addAleteStartVehicle(StartVehicleDTO startVehicleDTO);
//
   //根据车辆编号查询车辆信息
    public VehicleInformationDTO finddVehicleInformation(VehicleInformationDTO vehicleInformationDTO);



}
