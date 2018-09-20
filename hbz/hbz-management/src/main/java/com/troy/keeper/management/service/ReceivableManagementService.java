package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.ReceivableManagementDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2018/1/23.
 */
public interface ReceivableManagementService {

    //应收分页查询
    public Page<ReceivableManagementDTO> findConfirmed(ReceivableManagementDTO receivableManagementDTO, Pageable pageable);

    //新增应收的信息
    public  Boolean addReceivableManagement(ReceivableManagementDTO receivableManagementDTO);

    //编辑应收信息
    public Boolean updateRmInformation(ReceivableManagementDTO receivableManagementDTO);

    //修改应收信息的记录状态
    public Boolean updateRecordStatus(ReceivableManagementDTO receivableManagementDTO);





}
