package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.CommodityInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
public interface CommodityInformationRepository  extends BaseRepository<CommodityInformation, Long> {

    //删除货物信息
    @Modifying
    @Query(" update CommodityInformation ci set ci.status='0'  where ci.id=?1")
    public  void  deleteCommodityInformation(Long id);

    //查询货物编号的最大值
    @Query(" select  max(ci.commodityNumber)  from  CommodityInformation ci ")
    public String  commodityNumber();

   //货物名称不能重复
    @Query(" select  count(ci.commodityName) from   CommodityInformation ci where ci.commodityName=?1  ")
    public Long  commodityName(String commodityName);

   //新增条码 验证
    @Query("select   count(ci.barcode)   from CommodityInformation  ci   where ci.barcode=?1")
    public  Long  barcode(String barcode);


    //修改货物名称不能重复
    @Query("select  count(ci.commodityName) from CommodityInformation ci where ci.commodityName=?1 and ci.id <>?2")
    public  Long   updateCommodityName(String  commodityName ,Long  id);
    //修改货物条码能不能重复
    @Query(" select count(ci.barcode)  from  CommodityInformation ci where ci.barcode=?1 and ci.id <>?2 ")
    public Long  updateBarcode(String barcode,Long id);












}
