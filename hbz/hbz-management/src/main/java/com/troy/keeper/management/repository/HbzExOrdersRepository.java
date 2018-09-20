package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzExOrder;
import com.troy.keeper.hbz.po.HbzExpressPieces;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2017/12/20.
 */
public interface HbzExOrdersRepository  extends BaseRepository<HbzExOrder, Long> {

    //通过快递的id 查询派件表及快递的 信息
    @Query("select  hep   from  HbzExOrder  hep where hep.id=?1")
    public  HbzExOrder findHbzHbzExOrder(Long id);








}
