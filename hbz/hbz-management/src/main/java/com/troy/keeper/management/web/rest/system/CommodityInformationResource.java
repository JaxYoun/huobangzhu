package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.CommodityInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.service.CommodityInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
@RestController
public class CommodityInformationResource {

    @Autowired
    private CommodityInformationService commodityInformationService;

    //分页查询
    @RequestMapping("/api/manager/commodityInformationTable")
    public ResponseDTO commodityInformationTable(@RequestBody CommodityInformationDTO commodityInformationDTO, Pageable pageable){
        return new ResponseDTO("200", "货物信息列表分页查询",commodityInformationService.findByCondition(commodityInformationDTO,pageable));
    }

    //新增货物信息
    @RequestMapping("/api/manager/addCommodityInformation")
    public ResponseDTO addCommodityInformation(@RequestBody CommodityInformationDTO commodityInformationDTO){
         Boolean  s= commodityInformationService.commodityName(commodityInformationDTO);
         Boolean  s1= commodityInformationService.barcode(commodityInformationDTO);
         if (s==false){
             return new ResponseDTO("401", "新增货物名称重复",s);
         }
         if (s1==false){
             return new ResponseDTO("401", "新增货物条码重复",s1);
         }

        return new ResponseDTO("200", "新增成功",commodityInformationService.addCommodityInformation(commodityInformationDTO));
    }

    //修改货物
    @RequestMapping("/api/manager/updateCommodityInformation")
    public ResponseDTO updateCommodityInformation(@RequestBody CommodityInformationDTO commodityInformationDTO){
       Boolean s=  commodityInformationService.updateCommodityName(commodityInformationDTO);
       Boolean s1=  commodityInformationService.updateBarcode(commodityInformationDTO);
       if (s==false){
           return new ResponseDTO("401", "修改货物名称重复",s);
       }
       if (s1==false){
           return new ResponseDTO("401", "修改货物条码重复",s1);
       }

        return new ResponseDTO("200", "修改成功",commodityInformationService.updateCommodityInformation(commodityInformationDTO));
    }

    //删除货物信息
    @RequestMapping("/api/manager/deletaCommodityInformation")
    public ResponseDTO deletaCommodityInformation(@RequestBody CommodityInformationDTO commodityInformationDTO){
        return new ResponseDTO("200", "删除成功",commodityInformationService.deletaCommodityInformation(commodityInformationDTO));
    }

















}
