package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.VehicleInformation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.xml.ws.soap.MTOM;
import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
public interface VehicleInformationRepository  extends BaseRepository<VehicleInformation, Long> {

  //删除车辆信息
    @Modifying
    @Query("update  VehicleInformation vi set  vi.status=0  where vi.id=?1")
    public void deleteVehicleInformation(Long id);

   //新增车辆信息时判断 车牌号, 不能重复
  @Query("select   count(vi)  from   VehicleInformation  vi where vi.numberPlate=?1")
   public Long numberPlate(String numberPlate);

//  //新增车辆编号校验重复
//  @Query(" select   count(vi)     from  VehicleInformation vi  where vi.vehicleNumber=?1")
//  public Long vehicleNumber(Integer vehicleNumber);

  //车主的电话号码重复校验
  @Query(" select   count(vi)     from  VehicleInformation vi  where vi.ownersTelephone=?1")
  public  Long ownersTelephone(String ownersTelephone);
  //车主证件号
  @Query(" select   count(vi)     from  VehicleInformation vi  where vi.ownerNumber=?1")
  public  Long ownerNumber(String ownerNumber);
  //司机电话号
  @Query(" select   count(vi)     from  VehicleInformation vi  where vi.driverTelephone=?1")
  public  Long driverTelephone(String driverTelephone);
  //司机证件号
  @Query(" select   count(vi)     from  VehicleInformation vi  where vi.driverNumber=?1")
  public  Long driverNumber(String driverNumber);



  //修改车牌号重复校验
  @Query("select   count(vi.numberPlate)  from   VehicleInformation  vi where vi.numberPlate=?1 and vi.id <>?2")
  public Long updateNumberPlate(String numberPlate,Long  id);
//  //修改编号重复
//  @Query("select   count(vi.vehicleNumber)  from   VehicleInformation  vi where vi.vehicleNumber=?1 and vi.id <>?2")
//  public Long updateVehicleNumber(Integer vehicleNumber,Long  id);
  //修改车主电话
  @Query("select   count(vi.ownersTelephone)  from   VehicleInformation  vi where vi.ownersTelephone=?1 and vi.id <>?2")
  public Long updateOwnersTelephone(String ownersTelephone,Long  id);
  //修改车主证件号
  @Query("select   count(vi.ownerNumber)  from   VehicleInformation  vi where vi.ownerNumber=?1 and vi.id <>?2")
  public Long updateOwnerNumber(String ownerNumber,Long  id);
  //修改司机电话
  @Query("select   count(vi.driverTelephone)  from   VehicleInformation  vi where vi.driverTelephone=?1 and vi.id <>?2")
  public Long updateDriverTelephone(String driverTelephone,Long  id);
  //修改司机证件号
  @Query("select   count(vi.driverNumber)  from   VehicleInformation  vi where vi.driverNumber=?1 and vi.id <>?2")
  public Long updateDriverNumber(String driverNumber,Long  id);



  //查询车辆编号的最大值
  @Query(" select  max(vi.vehicleNumber)  from  VehicleInformation vi ")
  public String  vehicleNumber();


}
