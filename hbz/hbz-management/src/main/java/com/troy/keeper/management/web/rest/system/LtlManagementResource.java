package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.LtlManagementDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.TeakUserInformationDTO;
import com.troy.keeper.management.service.LtlManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2018/2/5.
 */
@RestController
public class LtlManagementResource {

    @Autowired
    private LtlManagementService ltlManagementService;

    //零担分页查询
    @RequestMapping("/api/manager/ltlOrderTable")
    public ResponseDTO driverConfirmedPage(@RequestBody LtlManagementDTO ltlManagementDTO, Pageable pageable){
        return new ResponseDTO("200", "零担分页查询",ltlManagementService.findByCondition(ltlManagementDTO,pageable));
    }


    //零担id 查询零担的详细信息
    @RequestMapping("/api/manager/oneInforMation")
    public ResponseDTO oneInforMation(@RequestBody LtlManagementDTO ltlManagementDTO){
        return new ResponseDTO("200", "零担id 查询零担的详细信息",ltlManagementService.ltlInformation(ltlManagementDTO));
    }


    //接单人  详情
    @RequestMapping("/api/manager/teakUserLtl")
    public ResponseDTO teakUserInformation(@RequestBody TeakUserInformationDTO teakUserInformationDTO){
        return new ResponseDTO("200", "接单人详情",ltlManagementService.findTeakUserInformation(teakUserInformationDTO));
    }







}
