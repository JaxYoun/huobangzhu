package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.Warehouse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 16:19
 */
public interface WarehouseRepository extends BaseRepository<Warehouse, Long> {
    @Modifying
    @Query(" update Warehouse w set w.lifecycle=?1  where w.id=?2")
    public void updateLifecycle(Integer type,Long id);

    @Modifying
    @Query(" update Warehouse w set w.publishDate=?1  where w.id=?2")
    public void updatePublishDate(Long publishDate,Long id);

    @Modifying
    @Query(" update Warehouse w set w.warehouseDescribe=?1  where w.id=?2")
    public void updateDescribe(String desc,Long id);

    /**
     * 定时任务仓储发布一个月之后设置为过期
     */
    @Modifying
    @Query(nativeQuery=true,value = "update hbz_warehouse hw set hw.lifecycle='2' where hw.publish_date-unix_timestamp(date_sub(NOW(), interval 1 MONTH))*1000 < 0")
    void updateLifecycleTimer();

    /**
     *  当前时间-(发布时间+租用时间) > 0 修改为过期
     */
    //@Modifying
    //@Query(nativeQuery=true,value = "update hbz_warehouse hw set hw.lifecycle='2' where NOW()- DATE_ADD(FROM_UNIXTIME(hw.publish_date/1000,'%Y-%m-%d %H:%i:%s'),INTERVAL hw.min_rent_time MONTH) > 0")
    //void updateLifecycleTimer();

    @Modifying
    @Query(" update Warehouse w set w.lifecycle='2'  where w.id=?1")
    public void updateLifecycleOverdue(Long id);
}
