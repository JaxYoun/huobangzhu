package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzExpressPieces;
import com.troy.keeper.hbz.po.LogisticsDetails;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/21.
 */
public interface HbzExpressPiecesRepository  extends BaseRepository<HbzExpressPieces, Long> {
  //通过本表的 id  即是实体类中的exId 查询物流详情记录中的 数据
    @Query(" select  ld   from LogisticsDetails ld where ld.hbzExpressPieces.id=?1")
    public List<LogisticsDetails> findLogisticsDetail(Long id);


    @Query("select hp from HbzExpressPieces hp where hp.hbzExOrder.id=?1")
    HbzExpressPieces findByHbzExOrderId(Long exOrderId);


}
