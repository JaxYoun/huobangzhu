package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.NewsDTO;
import com.troy.keeper.hbz.vo.NewsVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：YangJx
 * @Description：资讯服务层接口
 * @DateTime：2017/12/26 9:23
 */
public interface NewsService {

    /**
     * 根据资讯id获取资讯详情
     *
     * @param newsDTO
     * @return
     */
    NewsVO getNewsDetail(NewsDTO newsDTO);

    /**
     * 分页条件查询资讯列表
     *
     * @param newsDTO
     * @return
     */
    Page<NewsVO> getNewsListByPage(NewsDTO newsDTO, Pageable pageable);

    /**
     * 通过Mapper的方式获取详情
     * @param newsDTO
     * @return
     */
    NewsVO getNewsDetailViaMapper(NewsDTO newsDTO);

}
