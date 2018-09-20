package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.CargoInformation;
import com.troy.keeper.hbz.po.OutsourcingGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/1/15.
 */
public interface OutsourcingGoodsRepository  extends BaseRepository<OutsourcingGoods, Long> {

    //点击发车确认修改分包用户的运输状态
    @Modifying
    @Query("update  OutsourcingGoods og set og.shippingStatus='DURING_SHIPPING' where  og.id=?1")
    public void uopdateShippingStatus(Long id);


    //点击发车确认修改分包用户的运输状态
    @Modifying
    @Query("update  OutsourcingGoods og set og.remarks=?2 where  og.id=?1")
    public void saveRemarks(Long id,String remarks);


    //点击收货确认 修改分包单的状态
    @Modifying
    @Query("update  OutsourcingGoods od set od.shippingStatus='ISOK' where  od.id=?1")
    public void outShippingStatus(Long id);












}
