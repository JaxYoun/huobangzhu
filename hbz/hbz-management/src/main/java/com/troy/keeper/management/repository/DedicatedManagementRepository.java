package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzTakerInfoDTO;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.type.PayProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥   专线管理
 * @date 2017/11/28.
 */
public interface DedicatedManagementRepository extends BaseRepository<HbzFslOrder, Long> {
  //城市联动 查询
   @Query("select h  from HbzArea h where h.parent.id=?1 and h.status ='1' ")
   public  List<HbzArea> findById(Long id);

   //根据接单人用户的id 查询用户的车辆信息
    @Query("select  hpdr  from  HbzPersonDriverRegistry hpdr where hpdr.id=?1")
    public  HbzPersonDriverRegistry findHbzPersonDriverRegistry(Long id);

    //根据订单编号查询  订单的支付信息
    @Query("select  hp  from  HbzPay hp where hp.businessNo=?1 and hp.payProgress in(?2)")
    public  HbzPay findHbzPay(String businessNo,List<PayProgress> payProgresses);

    //根据订单id 查询车辆征集条件
    @Query("select  ht  from  HbzTender  ht where ht.order.id=?1")
    public HbzTender findHbzTender(Long id);

    //根据订单id 查询参与征集司机的信息
    @Query("select  hti   from  HbzTakerInfo hti  where hti.order.id=?1")
    public List<HbzTakerInfo> findHbzTakerInfo(Long orderId);

  //查询客户分类的 name 值
  @Query(" select  htv.name   from   HbzTypeVal htv  where htv.type=?1 and  htv.val=?2 ")
  public String   findUserClassification(String type,String  val);

//  //查询客户分类的 name 值
//  @Query(" select  htv.name   from   HbzTypeVal htv  where htv.type=?1 and  htv.val=?2 ")
//  public String   findHbzTendName(String type,Integer  val);




/////////////////////////////////////////////////////////////////////////////////
   //通过订单Id 查询整车订单信息
    @Query("select  hf  from  HbzFslOrder  hf where hf.id=?1")
    public  HbzFslOrder findHbzFslOrder(Long id);







}
