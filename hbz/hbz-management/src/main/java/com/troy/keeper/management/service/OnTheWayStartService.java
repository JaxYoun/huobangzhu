package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/9.
 */
public interface OnTheWayStartService {


    //分页   司机确认
    public Page<StartVehicleDTO> driverConfirmed(StartVehicleDTO startVehicleDTO, Pageable pageable);

    //司机确认装货
    public  Boolean updateStatus(StartVehicleDTO startVehicleDTO);


    //分页  在途发车信息
    public Page<StartVehicleInformationDTO> findByCondition(StartVehicleInformationDTO startVehicleInformationDTO, Pageable pageable);


    //编辑时 修改装车 车的状态和备注
    public Boolean updateStartVehicleInformation(StartVehicleInformationDTO startVehicleInformationDTO);


    //通过车辆的id  查询车中的货物信息
    public StartVehicleInformationDTO  findAllStartVehicleInformation(StartVehicleInformationDTO startVehicleInformationDTO);


    //在途发车中途添加发货信息
    public Boolean inserAletelStartVehicleInformation(StartVehicleInformationDTO startVehicleInformationDTO);



    //卸货
//    public Boolean  unloadProduct(List<Long> list);
    public  Boolean unloadProduct(StartVehicleInformationDTO startVehicleInformationDTO);


   //发车单导出
    public List<StartVehicleInformationDTO> startVehicleInformationExport(StartVehicleInformationDTO startVehicleInformationDTO);



    boolean cancleStartVehicle(StartVehicleDTO startVehicleDTO);

}
