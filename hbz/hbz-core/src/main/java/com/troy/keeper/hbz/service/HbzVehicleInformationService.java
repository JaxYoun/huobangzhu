package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzVehicleInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Autohor: hecj
 * @Description: 车辆信息管理Service
 * @Date: Created in 14:47  2018/1/30.
 * @Midified By:
 */
public interface HbzVehicleInformationService {
    //车辆信息分页查询
    Page<HbzVehicleInformationDTO> queryVehicleInformations(HbzVehicleInformationDTO hbzVehicleInformationDTO, Pageable pageable);
    //新增车辆信息
    boolean addVehicleInformation(HbzVehicleInformationDTO vehicleInformationDTO);
    //编辑车辆信息
    boolean updateVehicleInformation(HbzVehicleInformationDTO vehicleInformationDTO);
    //删除车辆信息
    boolean deleteVehicleInformation(HbzVehicleInformationDTO vehicleInformationDTO);
}
