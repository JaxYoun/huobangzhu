package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.DealManagement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/1/22.
 */
public interface DealManagementRepository extends BaseRepository<DealManagement, Long> {

    //查询货物编号的最大值
    @Query(" select  max(dm.coding)  from  DealManagement dm  where dm.orderSource='0'")
    public String coding();

    //查询货物编号的最大值
    @Query(" select  max(dm.sourceCode)  from  DealManagement dm ")
    public String sourceCode();

    @Modifying
    @Query(" update  DealManagement dm set dm.recordStatus='0' where dm.id=?1")
    public void  updateRecordStatus(Long id);









}
