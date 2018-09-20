package com.troy.keeper.management.service;

import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;
import com.troy.keeper.management.dto.LtlManagementDTO;
import com.troy.keeper.management.dto.TeakUserInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/2/5.
 */
public interface LtlManagementService {

    //零担分页查询
    public Page<LtlManagementDTO> findByCondition(LtlManagementDTO ltlManagementDTO, Pageable pageable);


    //零担id 查询零担的详细信息
    public LtlManagementDTO ltlInformation(LtlManagementDTO ltlManagementDTO);


    //查询接单人的 详细信息
    public TeakUserInformationDTO findTeakUserInformation(TeakUserInformationDTO teakUserInformationDTO);










}
