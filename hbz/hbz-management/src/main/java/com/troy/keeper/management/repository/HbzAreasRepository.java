package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzArea;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 */
public interface HbzAreasRepository extends BaseRepository<HbzArea, Long> {

    //根据市id查询所有的区的id集合
    @Query("select a.id from HbzArea a where a.parent.id = ?1 or a.id = ?1")
    List<Long> findAreaIdsByParentId(Long parentId);


    //根据省id查询所有的市id和区id的集合

    @Query("select a.id from HbzArea a where a.parent.id = ?1 or a.parent.parent.id = ?1 or a.id = ?1")
    List<Long> findAllIdsByParentId(Long id);

    //通过outCode查询省市区
    public HbzArea findByOutCode(String outCode);

}
