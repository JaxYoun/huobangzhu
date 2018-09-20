package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.ReceivableManagement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/1/23.
 */
public interface ReceivableManagementRepository extends BaseRepository<ReceivableManagement, Long> {

    //查询货物编号的最大值
    @Query(" select  max(rm.sourceCode)  from  ReceivableManagement rm ")
    public String sourceCode();


    @Modifying
    @Query(" update  ReceivableManagement rm set rm.recordStatus='0' where rm.id=?1  ")
    public void  updateRecordStatus(Long  id);

    //查询货物编号的最大值
    @Query(" select  max(dm.coding)  from  ReceivableManagement dm  where dm.orderSource='0'")
    public String coding();




}
