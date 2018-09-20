package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.CommodityInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
public interface CommodityInformationService {

    //分页
    public Page<CommodityInformationDTO> findByCondition(CommodityInformationDTO commodityInformationDTO, Pageable pageable);

    //新增货物信息
    public Boolean addCommodityInformation(CommodityInformationDTO commodityInformationDTO);

    //新增货物名称重复校验
    public  Boolean  commodityName(CommodityInformationDTO commodityInformationDTO);
    //新增货物条码校验
    public Boolean  barcode(CommodityInformationDTO commodityInformationDTO);


    //修改货物信息
    public Boolean updateCommodityInformation(CommodityInformationDTO commodityInformationDTO);

     //修改货物名称不能重复
    public Boolean updateCommodityName(CommodityInformationDTO commodityInformationDTO);
    //修改货物条码不能重复
    public  Boolean updateBarcode(CommodityInformationDTO  commodityInformationDTO);

   //删除货物
   public Boolean  deletaCommodityInformation(CommodityInformationDTO commodityInformationDTO);



}
