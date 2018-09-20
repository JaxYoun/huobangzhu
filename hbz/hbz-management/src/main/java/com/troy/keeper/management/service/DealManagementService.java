package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.dto.CommodityInformationDTO;
import com.troy.keeper.management.dto.DealManagementDTO;
import com.troy.keeper.management.dto.SubjectManagementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2018/1/22.
 */
public interface DealManagementService {


  //分页查询出科目管理中应付的所有信息。
  public Page<SubjectManagementDTO> findByCondition(SubjectManagementDTO subjectManagementDTO, Pageable pageable);

  //应付管理中的货物信息的 分页查询
//  public Page<CargoInformationDTO> findCCargoInformationPage(CargoInformationDTO cargoInformationDTO, Pageable pageable);

    //应付表分页查询
    public Page<DealManagementDTO> dealPage(DealManagementDTO dealManagementDTO, Pageable pageable);

    //新建应付信息
    public Boolean savedealManagement(DealManagementDTO dealManagementDTO);

    //编辑某一个应付信息
    public Boolean  updateDealManagement(DealManagementDTO dealManagementDTO);


    //作废功能
    public  Boolean updateRecordStatus(DealManagementDTO dealManagementDTO);





}
