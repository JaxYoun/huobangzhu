package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.service.SmOrgManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2018/1/31.  网点库存查询
 */
@RestController
public class SmOrgManagementResource {

    @Autowired
    private SmOrgManagementService smOrgManagementService;




    //查询当前登陆人下的所有机构
    @RequestMapping("/api/manager/listSmOrgInformation")
    public ResponseDTO listSmOrgInformation(@RequestBody SmOrgDTO smOrgDTO){
        return new ResponseDTO("200", "查询当前登陆人下的所有机构",smOrgManagementService.getListSmOrg1(smOrgDTO));
    }

    //查询当前机构下的所有货物信息
    @RequestMapping("/api/manager/selectAllOrgCargoInformation")
    public ResponseDTO selectAllOrgCargoInformation(@RequestBody CargoInformationDTO cargoInformationDTO, Pageable pageable){

        return new ResponseDTO("200", "查询当前机构下的所有货物信息",smOrgManagementService.selectCargoInformation(cargoInformationDTO,pageable));
    }


    //通过选择机构  查询当前所选择的机构下的 所有网店的收货信息
    @RequestMapping("/api/manager/currentSmOrgCargoInformation")
    public ResponseDTO currentSmOrgCargoInformation(@RequestBody CargoInformationDTO cargoInformationDTO, Pageable pageable){

        return new ResponseDTO("200", "选择机构下的所有收货信息",smOrgManagementService.selectSmOrgCargoInformation(cargoInformationDTO,pageable));
    }


}
