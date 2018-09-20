package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.StartVehicle;
import com.troy.keeper.hbz.po.StartVehicleInformation;
import com.troy.keeper.hbz.po.VehicleInformation;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/10.
 */
public interface StartVehicleInformationRepository  extends BaseRepository<StartVehicleInformation, Long> {


    //查询车辆信息中所有车辆的 车辆编号
    @Query(" select  vi  from VehicleInformation vi ")
    public List<VehicleInformation> findVehicleNumber();

    //装车成功后  更新货物的物流状态
    @Modifying
    @Query("update  CargoInformation ci   set ci.shippingStatus=?2 where ci.id=?1 ")
    public  void  updateshippingStatus(Long id,ShippingStatus shippingStatus);



    //装车成功后  更新货物的数量
    @Modifying
    @Query("update  CargoInformation ci   set ci.inventoryQuantity=?2 where ci.id=?1 ")
    public  void  updateInventoryQuantity(Long id,Integer inventoryQuantity);

//    //通过货物路线，车辆编号，发车时间查询  发车管理中的货物信息
//    @Query("select  sv    from  StartVehicle sv  where sv.originArea.id=?1 and sv.destArea.id=?2 and sv.vehicleNumber=?3 and (sv.receiptDate=?4 or sv.receiptToDate=?5)")
//    public List<StartVehicle> findAllStartVehicle(Long originArea, Long  destArea, String vehicleNumber, Long  receiptDate, Long receiptToDate);
//


    //编辑时 修改装车 车的状态和备注
    @Modifying
    @Query(" update   StartVehicleInformation  svi set svi.transitState=?2,svi.remarks=?3  where svi.id=?1")
    public void updateStartVehicleInformation(Long id, TransitState transitState,String  remarks);


    //通过车辆id  查询所有的车辆信息
    @Query("select   svi  from  StartVehicleInformation  svi where svi.id=?1")
    public StartVehicleInformation selectAllStartVehicleInformation(Long id);



    //查询没有卸货的 货物信息
    @Query(" select    sv  from StartVehicle   sv where  sv.startVehicleInformation.id=?1  and sv.isUnload='NOTUNLOADED' ")
    public  List<StartVehicle> selctStartVehicleIn(Long id);

    ////发车单确认
    @Modifying
    @Query(" update StartVehicleInformation svi set  svi.shippingStatus='DURING_SHIPPING'  where svi.id=?1")
    public  void  startCar(Long  id);


    //卸货的时候修改 装货车辆的状态ISOK("已完成")
    @Modifying
    @Query(" update StartVehicleInformation svi set  svi.shippingStatus='ISOK'  where svi.id=?1")
    public  void  updateShippingStatus(Long id);

    //卸货的时候修改 装货车辆的状态PARTUNLOADING("部分卸货")
    @Modifying
    @Query(" update StartVehicleInformation svi set  svi.shippingStatus='PARTUNLOADING'  where svi.id=?1")
    public  void  updatepartunloading(Long id);



}
