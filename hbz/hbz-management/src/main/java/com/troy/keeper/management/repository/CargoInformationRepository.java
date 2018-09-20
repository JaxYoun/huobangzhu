package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.CargoInformation;
import com.troy.keeper.hbz.po.DealManagement;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.ReceivableManagement;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.system.domain.SmUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/29.
 */
public interface CargoInformationRepository  extends BaseRepository<CargoInformation, Long> {

  //查询物流编号的最大值
    @Query(" select  max(ci.trackingNumber)  from  CargoInformation ci ")
    public String trackingNumber();


    //查询货物编号的最大值
    @Query(" select  max(ci.commodityNumber)  from  CargoInformation ci ")
    public String commodityNumber();

    //删除收货信息
    @Modifying
    @Query(" update CargoInformation ci set ci.status='0' where ci.id=?1")
    public void   deleteCargoInformation(Long id);

    //根据收货的信息的id 查询该收货的详细信息
    @Query(" select  ci   from CargoInformation ci where ci.id=?1")
    public CargoInformation findCargoInformationId(Long id);

    //查询用户的组织机构
   @Query("select   su  from SmUser su where su.id=?1")
    public SmUser findSmOrgId(Long id);

  //修改时判断运单编号的重复性
  @Query("select   count(cif.waybillNumber)   from  CargoInformation cif where cif.waybillNumber=?1 and  cif.id <>?2")
  public Long  waybillNumber(String waybillNumber,Long  id);

  //修改 收货表中的库存量
  @Modifying
  @Query(" update CargoInformation ci set ci.inventoryQuantity=?2 where ci.id=?1")
  public  void  updateInventoryQuantity(Long  id,Integer inventoryQuantity);

  @Query(" select ci.inventoryQuantity   from CargoInformation ci where ci.id=?1")
  Integer getCount(Long id);

  //批量保存时修改货物的运输状态
  @Modifying
  @Query(" update CargoInformation ci set ci.shippingStatus='OUTSOURCING' where ci.id=?1")
  public  void  updateShippingStatus(Long  id);


  //批量修改时修改货物的运输状态
  @Modifying
  @Query(" update CargoInformation ci set ci.shippingStatus=?2 where ci.id=?1")
  public  void  returnShippingStatus(Long  id, ShippingStatus str);


  //修改时通过物流编号查询 应付的信息
  @Query(" select  del  from DealManagement del where del.sourceCode=?1")
  public List<DealManagement> findAllDealManagement(String sourceCode);

  //修改时通过物流编号查询 应付的信息
  @Query(" select  rm  from ReceivableManagement rm where rm.sourceCode=?1")
  public ReceivableManagement findAllReceivableManagement(String sourceCode);


  //更新收货表中的库存量 及货物的运输状态
  @Modifying
  @Query(" update  CargoInformation  ci set ci.inventoryQuantity=?2 ,ci.shippingStatus=?3 where  ci.id=?1")
  public  void inventoryQuantityShippingStatus(Long id,Integer inventoryQuantity,ShippingStatus shippingStatus );

  //删除外包发车单更新 货物运输状态
  @Modifying
  @Query(" update  CargoInformation  ci set  ci.shippingStatus=?2 where  ci.id=?1")
  public  void updateOutShippingStatus(Long id,ShippingStatus shippingStatus );



}
