package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.NewsDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.JsonArrToListMapper;
import com.troy.keeper.hbz.mapper.NewsMapper;
import com.troy.keeper.hbz.po.News;
import com.troy.keeper.hbz.repository.NewsRepository;
import com.troy.keeper.hbz.service.NewsService;
import com.troy.keeper.hbz.vo.HbzUserVO;
import com.troy.keeper.hbz.vo.NewsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：资讯服务层实现类
 * @DateTime：2017/12/26 9:25
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsRepository newsRepository;

    /**
     * 根据资讯id获取资讯详情
     *
     * @param newsDTO
     * @return
     */
    @Override
    public NewsVO getNewsDetail(NewsDTO newsDTO) {
        News newsFromDB = this.newsRepository.findNewsByIdAndStatus(newsDTO.getId(), newsDTO.getStatus());
        NewsVO newsVO = BeanUtils.instantiate(NewsVO.class);

        BeanUtils.copyProperties(newsFromDB, newsVO);
        newsVO.setCreateDate(DateUtils.longToString(newsFromDB.getCreatedDate(), DateUtils.yyyy_MM_dd_HH_mm));
        newsVO.setLastUpdateDate(DateUtils.longToString(newsFromDB.getLastUpdatedDate(), DateUtils.yyyy_MM_dd_HH_mm));

        //HbzUserVO hbzUserVO = new HbzUserVO();
        //BeanUtils.copyProperties(newsFromDB.getCreateUser(), hbzUserVO, "bond, activated, starLevel, langKey, imageUrl, activationKey, sex, roleName, org, orgId");
        //newsVO.setCreateUser(hbzUserVO);

        //图片地址拼接
        String title = newsFromDB.getTitleImageList();
        newsVO.setTitleImageList(this.staticImagePrefix + title);
//        newsVO.setTitleImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, title));
        //String content = newsFromDB.getContentImageList();
        //newsVO.setContentImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, content));

        return newsVO;
    }

    /**
     * 分页条件查询资讯列表
     *
     * @param newsDTO
     * @return
     */
    @Override
    public Page<NewsVO> getNewsListByPage(NewsDTO newsDTO, Pageable pageable) {
        // 构造查询条件
        Specification<News> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            // 创建人
            if (newsDTO.getCreatedBy() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("createdBy"), newsDTO.getCreatedBy()));
            }

            // 标题模糊查询
            if (StringUtils.isNotBlank(newsDTO.getTitle())) {
                predicateList.add(criteriaBuilder.like(root.get("title"), "%" + newsDTO.getTitle() + "%"));
            }

            // 创建时间
            if (StringUtils.isNotBlank(newsDTO.getCreateDateStart())) {
                predicateList.add(criteriaBuilder.ge(root.get("createdDate"), DateUtils.noSecondStringToLong(newsDTO.getCreateDateStart())));
            }
            if (StringUtils.isNotBlank(newsDTO.getCreateDateEnd())) {
                predicateList.add(criteriaBuilder.le(root.get("createdDate"), DateUtils.noSecondStringToLong(newsDTO.getCreateDateEnd())));
            }

            // 更新时间
            if (StringUtils.isNotBlank(newsDTO.getUpdateDateStart())) {
                predicateList.add(criteriaBuilder.ge(root.get("lastUpdatedDate"), DateUtils.noSecondStringToLong(newsDTO.getUpdateDateStart())));
            }
            if (StringUtils.isNotBlank(newsDTO.getUpdateDateStart())) {
                predicateList.add(criteriaBuilder.le(root.get("lastUpdatedDate"), DateUtils.noSecondStringToLong(newsDTO.getUpdateDateStart())));
            }

            // 可用状态
            if (StringUtils.isNotBlank(newsDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), newsDTO.getStatus()));
            }

            //咨询类型
            if(StringUtils.isNotBlank(newsDTO.getNewsType())) {
                predicateList.add(criteriaBuilder.equal(root.get("newsType"), newsDTO.getNewsType()));
            }

            predicateList.add(criteriaBuilder.equal(root.get("newsStatus"), "1"));//显示可用状态数据
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);

            return criteriaBuilder.and(predicateArr);
        };

        // 执行分页查询
        Page<News> newsPage = newsRepository.findAll(specification, pageable);

        // PO封装VO
        Page<NewsVO> newsVOPage = newsPage.map(news -> {
            NewsVO newsVO = this.newsMapper.entityToVo(news);

            //图片地址拼接
            String title = news.getTitleImageList();
            newsVO.setTitleImageList(this.staticImagePrefix + title);
//            newsVO.setTitleImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, title));
            //String content = news.getContentImageList();
            //newsVO.setContentImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, content));

            return newsVO;
        });

        return newsVOPage;
    }

    /**
     * 通过Mapper的方式获取详情
     *
     * @param newsDTO
     * @return
     */
    @Override
    public NewsVO getNewsDetailViaMapper(NewsDTO newsDTO) {
        News newsFromDB = this.newsRepository.findOne(newsDTO.getId());
        return this.newsMapper.entityToVo(newsFromDB);
    }


}
