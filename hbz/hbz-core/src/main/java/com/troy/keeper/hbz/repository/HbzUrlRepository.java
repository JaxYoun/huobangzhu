package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzUrl;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leecheng on 2017/10/11.
 */
public interface HbzUrlRepository extends BaseRepository<HbzUrl, Long> {

    Long countByUrlPattern(String urlPat);

    @Query("select u from HbzUrl u where u.status = '1' and u.state = '1'")
    List<HbzUrl> findAvilable();

}
