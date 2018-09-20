package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.StartVehicle;
import com.troy.keeper.hbz.po.VehicleInformation;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/3.
 */
public interface StartVehicleRepository   extends BaseRepository<StartVehicle, Long> {


    //查询车辆信息中所有车辆的 车辆编号
    @Query(" select  vi.vehicleNumber  from VehicleInformation vi ")
    public List<String> findVehicleNumber();


//   //通过货物路线，车辆编号，发车时间查询  发车管理中的货物信息
//    @Query("select  sv    from  StartVehicle sv  where sv.originArea.id=?1 and sv.destArea.id=?2 and sv.vehicleNumber=?3 and (sv.receiptDate=?4 or sv.receiptToDate=?5)")
//    public List<StartVehicle> findAllStartVehicle(Long originArea,Long  destArea,String vehicleNumber,Long  receiptDate,Long receiptToDate);


    //装车成功后  更新货物的物流状态
    @Modifying
    @Query("update  CargoInformation ci   set ci.shippingStatus=?2 where ci.id=?1 ")
    public  void  updateshippingStatus(Long id,ShippingStatus shippingStatus);


   //更新收货信息的库存量
    @Modifying
    @Query("update  CargoInformation ci set ci.inventoryQuantity=?2 where ci.id=?1")
    public  void  updateInventoryQuantity(Long id,Integer inventoryQuantity);

    //通过车辆编号 查询车辆信息
    @Query("select  vi  from VehicleInformation vi  where vi.vehicleNumber=?1 ")
    public VehicleInformation findVehicleInformation(String vehicleNumber);

   //通过车辆id 查询 车辆信息
   @Query("select  vi  from VehicleInformation vi  where vi.id=?1 ")
    public  VehicleInformation findVehicleInformationDate(Long id);


   //当司机确认后  修改状态为0
    @Modifying
    @Query("update  StartVehicle sv set sv.status='0' where sv.id=?1 ")
    public void updateStatus(Long id);



    //卸货时修改货物状态
    @Modifying
    @Query("update  StartVehicle sv set sv.isUnload='ALLUNLOADING' where sv.id=?1 ")
    public void updateIsUnload(Long id);

//    //根据货物id  运输状态  查询货物
//    @Query(" select  sv from StartVehicle  sv  where sv.isUnload='ALLUNLOADING' and sv.id=?1")
//    public  List<StartVehicle>  findListStartVehicle(Long  id);







}
