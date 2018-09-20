package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.dto.DealManagementDTO;
import com.troy.keeper.management.dto.SubjectManagementDTO;
import com.troy.keeper.management.service.DealManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2018/1/22.
 */
@RestController
public class DealManagementResource {

    @Autowired
    private DealManagementService dealManagementService;

    //应付时  查询科目管理中的应付数据 并分页
    @RequestMapping("/api/manager/selectDealPage")
    public ResponseDTO selectDealPage(@RequestBody SubjectManagementDTO subjectManagementDTO, Pageable pageable){
        return new ResponseDTO("200", "科目管理应付分页查询",dealManagementService.findByCondition(subjectManagementDTO,pageable));
    }


  //应付表分页查询
  @RequestMapping("/api/manager/selectAllDealPage")
  public ResponseDTO selectAllDealPage(@RequestBody DealManagementDTO dealManagementDTO, Pageable pageable){
      return new ResponseDTO("200", "应付表分页查询",dealManagementService.dealPage(dealManagementDTO,pageable));
  }

  //保存新建的应付信息
  @RequestMapping("/api/manager/addDealInformation")
  public ResponseDTO addDealInformation(@RequestBody DealManagementDTO dealManagementDTO){
      return new ResponseDTO("200", "保存新建的应付信息",dealManagementService.savedealManagement(dealManagementDTO));
  }

    //编辑某一个应付信息
    @RequestMapping("/api/manager/updateDealInformation")
    public ResponseDTO updateDealInformation(@RequestBody DealManagementDTO dealManagementDTO){
        return new ResponseDTO("200", "编辑应付信息",dealManagementService.updateDealManagement(dealManagementDTO));
    }

    //修改记录状态
    @RequestMapping("/api/manager/updateDealRecordStatus")
    public ResponseDTO updateDealRecordStatus(@RequestBody DealManagementDTO dealManagementDTO){
        return new ResponseDTO("200", "修改记录状态",dealManagementService.updateRecordStatus(dealManagementDTO));
    }















}
