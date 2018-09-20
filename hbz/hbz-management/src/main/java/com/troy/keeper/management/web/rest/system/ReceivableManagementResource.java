package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.ReceivableManagementDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.service.ReceivableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2018/1/23.
 */
@RestController
public class ReceivableManagementResource {

    @Autowired
    private ReceivableManagementService receivableManagementService;


    //应收分页查询
    @RequestMapping("/api/manager/selectReceivablePage")
    public ResponseDTO selectReceivablePage(@RequestBody ReceivableManagementDTO receivableManagementDTO, Pageable pageable){
        return new ResponseDTO("200", "应收分页查询",receivableManagementService.findConfirmed(receivableManagementDTO,pageable));
    }


    //新增应收的信息
    @RequestMapping("/api/manager/addRmInformation")
    public ResponseDTO addRmInformation(@RequestBody ReceivableManagementDTO receivableManagementDTO){
        return new ResponseDTO("200", "新增应收的信息",receivableManagementService.addReceivableManagement(receivableManagementDTO));
    }

    //编辑应收的信息
    @RequestMapping("/api/manager/updateRmInformation")
    public ResponseDTO updateRmInformation(@RequestBody ReceivableManagementDTO receivableManagementDTO){
        return new ResponseDTO("200", "编辑应收的信息",receivableManagementService.updateRmInformation(receivableManagementDTO));
    }


    //修改应收记录状态
    @RequestMapping("/api/manager/updateRmuRecordStatus")
    public ResponseDTO updateRmuRecordStatus(@RequestBody ReceivableManagementDTO receivableManagementDTO){
        return new ResponseDTO("200", "修改应收记录状态",receivableManagementService.updateRecordStatus(receivableManagementDTO));
    }























}
