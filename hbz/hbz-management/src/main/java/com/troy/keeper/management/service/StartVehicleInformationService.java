package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;

import java.util.List;

/**
 * @author 李奥   初始装车
 * @date 2018/1/10.
 */

public interface StartVehicleInformationService {


    //查询车辆信息中所有车辆的 车辆编号
    public List<VehicleInformationDTO> findVehicleNumber();


//    //通过货物路线，车辆编号，发车时间查询  发车管理中的货物信息
//    public List<StartVehicleDTO> findAllStartVehicle(StartVehicleDTO startVehicleDTO);




    //批量添加 货物信息装车
    public Boolean  addBatchStartVehicle(StartVehicleInformationDTO startVehicleInformationDTO);

    //发车单确认
    public  Boolean startCar(StartVehicleInformationDTO startVehicleInformationDTO);

    //发车单状态为新建时  编辑
    public Boolean addNewBatchStartVehicle(StartVehicleInformationDTO startVehicleInformationDTO);

    //删除新建的车辆信息
    public Boolean deleteCar( StartVehicleInformationDTO startVehicleInformationDTO);













}
