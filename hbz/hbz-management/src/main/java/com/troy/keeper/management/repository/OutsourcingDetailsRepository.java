package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.OutsourcingDetails;
import com.troy.keeper.hbz.po.OutsourcingGoods;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/2/3.
 */
public interface OutsourcingDetailsRepository  extends BaseRepository<OutsourcingDetails, Long> {

    //点击收货确认
    @Modifying
    @Query("update  OutsourcingDetails od set od.shippingStatus='SIGN' where  od.id=?1")
    public void outsourcingDetailsShippingStatus(Long id);






}
