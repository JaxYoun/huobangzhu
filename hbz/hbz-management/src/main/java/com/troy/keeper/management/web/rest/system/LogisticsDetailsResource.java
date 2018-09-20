package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.ExLogisticsDetailsDTO;
import com.troy.keeper.management.dto.ManagementHbzExOrderDTO;
import com.troy.keeper.management.service.LogisticsDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2017/12/21.
 */
@RestController
public class LogisticsDetailsResource {
    @Autowired
    private LogisticsDetailsService logisticsDetailsService;





    //分页查询
    @RequestMapping("/api/manager/logisticsDetailsPage")
    public ResponseDTO pageQureyDemo(@RequestBody ManagementHbzExOrderDTO managementHbzExOrderDTO, Pageable pageable){
        return new ResponseDTO("200", "快递记录分页查询",logisticsDetailsService.findByCondition(managementHbzExOrderDTO,pageable));
    }


    //保存快递物流详情
    @RequestMapping("/api/manager/addLogisticsDetail")
    public ResponseDTO addLogisticsDetail(@RequestBody ExLogisticsDetailsDTO exLogisticsDetailsDTO){
        return new ResponseDTO("200", "保存物流详情",logisticsDetailsService.saveLogisticsDetails(exLogisticsDetailsDTO));
    }

   //删除物流详情
   @RequestMapping("/api/manager/deleteLogisticsDetail")
   public ResponseDTO deleteLogisticsDetail(@RequestBody ExLogisticsDetailsDTO exLogisticsDetailsDTO){
       return new ResponseDTO("200", "删除物流详情",logisticsDetailsService.deletaLogisticsDetails(exLogisticsDetailsDTO));
   }






}
