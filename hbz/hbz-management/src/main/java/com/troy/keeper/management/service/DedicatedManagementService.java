package com.troy.keeper.management.service;



import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.management.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author 李奥
 * @date 2017/11/28.
 */
public interface DedicatedManagementService  {

    public Page<DedicatedLineManagementDTO> findByCondition(DedicatedLineManagementDTO dedicatedLineManagementDTO, Pageable pageable);

    //根据父id 查询子
    public List<HbzAreaDTO> findCity(HbzAreaDTO hbzAreaDTO);

    //查询接单人的 详细信息
    public TeakUserInformationDTO findTeakUserInformation(TeakUserInformationDTO teakUserInformationDTO);

    //根据订单编号 查询支付信息
    public HbzPayChildDTO findHbzPay(HbzPayChildDTO hbzPayChildDTO);

    //根据订单 id 查询车辆征集条件
    public  HbzTendersDTO findHbzTender(HbzTendersDTO hbzTendersDTO);

    //根据订单id 查询参与征集司机的信息
    public List<HbzTakerInfoDTO> findfindHbzTakerInfo(HbzTakerInfoDTO hbzTakerInfoDTO);

    //查看物流详情
    public List<LogisticsDetailsDTO> findLogisticsDetails(LogisticsDetailsDTO logisticsDetailsDTO);

    //根据订单详情查询 订单信息
    public DedicatedLineManagementDTO findHbzFslOrder(DedicatedLineManagementDTO dedicatedLineManagementDTO);

    //根据订单的id 查询车辆征集信息的  自己封装的 DTO
    public  List<HbzTakerInfoCallDTO> findCall(HbzTakerInfoCallDTO hbzTakerInfoCallDTO);






}
