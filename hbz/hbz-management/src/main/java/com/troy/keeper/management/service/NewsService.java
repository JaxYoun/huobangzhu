package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Autohor: hecj
 * @Description: 后台平台新闻Service
 * @Date: Created in 9:57  2018/1/10.
 * @Midified By:
 */
public interface NewsService {
    //新闻列表查询
    Page<NewsDTO> getNewsListByPage(NewsDTO newsDTO, Pageable pageable);
    //新增审核
    Boolean addNews(NewsDTO newsDTO);
    //根据资讯id获取新闻详情
    NewsDTO getNewsDetail(NewsDTO newsDTO);
    //根据资讯id修改新闻详情
    Boolean updateNews(NewsDTO newsDTO);
    //根据资讯id修改新闻类型
    Boolean updateNewsStatus(NewsDTO newsDTO);
    //根据资讯id删除图文
    Boolean deleteNews(NewsDTO newsDTO);
}
