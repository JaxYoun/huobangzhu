package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzArea;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leecheng on 2017/11/8.
 */
public interface HbzAreaRepository extends BaseRepository<HbzArea, Long> {

    HbzArea findByOutCode(String outCode);

    /**
     * 根据父区域获取子区域列表
     *
     * @param parentHbzAreaId
     * @return
     */
    @Query("select t from HbzArea t where t.parent.id = ?1 and t.status = '1'")
    List<HbzArea> getAreaByParentId(Long parentHbzAreaId);

}
