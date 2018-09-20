package com.troy.keeper.management.service;

import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.system.domain.SmOrg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/31.
 */
public interface SmOrgManagementService {

    //通过当前登陆人的id 查询当前登陆人的机构 及其子机构
    public List<SmOrgDTO> getListSmOrg(SmOrgDTO smOrgDTO);

    //分页查询当前机构及子机构下所有的货物信息
    public Page<CargoInformationDTO> selectCargoInformation(CargoInformationDTO cargoInformationDTO, Pageable pageable);

    //通过选择机构  查询当前所选择的机构下的 所有网店的收货信息
    public Page<CargoInformationDTO> selectSmOrgCargoInformation(CargoInformationDTO cargoInformationDTO, Pageable pageable);


    List<SmOrgDTO> getListSmOrg1(SmOrgDTO s);




}
