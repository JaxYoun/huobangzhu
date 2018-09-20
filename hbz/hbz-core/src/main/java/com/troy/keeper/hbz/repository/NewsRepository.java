package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.News;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/26 9:26
 */
public interface NewsRepository extends BaseRepository<News, Long> {

    /**
     * 根据id和状态获取资讯
     * @param id
     * @param status
     * @return
     */
    News findNewsByIdAndStatus(Long id, String status);

    News findByNewsNo(String newsNo);

}
