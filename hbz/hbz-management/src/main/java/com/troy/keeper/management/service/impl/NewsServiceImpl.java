package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.JsonArrToListMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.News;
import com.troy.keeper.hbz.repository.NewsRepository;
import com.troy.keeper.management.dto.NewsDTO;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 平台新闻ServiceImpl
 * @Date: Created in 9:58  2018/1/10.
 * @Midified By:
 */
@Service("managerNewsService")
@Transactional
public class NewsServiceImpl implements NewsService {

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Override
    public Page<NewsDTO> getNewsListByPage(NewsDTO newsDTO, Pageable pageable) {
        Page<News> pageList = newsRepository.findAll(new Specification<News>(){
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                //信息编号
                if (null != newsDTO.getNewsNo() && !"".equals(newsDTO.getNewsNo())) {
                    predicateList.add(criteriaBuilder.equal(root.get("newsNo"), newsDTO.getNewsNo()));
                }
                // 标题模糊查询
                if (StringUtils.isNotBlank(newsDTO.getTitle())) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + newsDTO.getTitle() + "%"));
                }
                // 发布时间
                if (null != newsDTO.getPublishDateStart() && !"".equals(newsDTO.getPublishDateStart())) {
                    predicateList.add(criteriaBuilder.ge(root.get("publishDate"), newsDTO.getPublishDateStart()));
                }
                if (null != newsDTO.getPublishDateEnd() && !"".equals(newsDTO.getPublishDateEnd())) {
                    //查收时分钟值需增加59999计算
                    predicateList.add(criteriaBuilder.le(root.get("publishDate"), newsDTO.getPublishDateEnd()+59999l));
                }
                // 信息类型
                if (StringUtils.isNotBlank(newsDTO.getNewsType())) {
                    predicateList.add(criteriaBuilder.equal(root.get("newsType"), newsDTO.getNewsType()));
                }
                //信息状态
                if (StringUtils.isNotBlank(newsDTO.getNewsStatus())) {
                    predicateList.add(criteriaBuilder.equal(root.get("newsStatus"), newsDTO.getNewsStatus()));
                }
                // 是否显示创建时间
                if (null != newsDTO.getDisplayPublishDate() && !"".equals(newsDTO.getDisplayPublishDate())) {
                    predicateList.add(criteriaBuilder.equal(root.get("displayPublishDate"), newsDTO.getDisplayPublishDate()));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), "1"));//显示使用数据
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("publishDate")));//按照发布时间倒序排序
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        },pageable);
        Page<NewsDTO> WarehouseAuditDTOPage = pageList.map(new Converter<News, NewsDTO>() {
            @Override
            public NewsDTO convert(News news) {
                NewsDTO newsDTO = new NewsDTO();
                BeanUtils.copyProperties(news,newsDTO,"bond, activated, starLevel, langKey, imageUrl, activationKey, sex, roleName, org, orgId");
                String newsName = userInformationRepository.findUserClassification("NewsType",news.getNewsType());
                newsDTO.setNewsTypeValue(newsName);
                newsDTO.setNewsType(news.getNewsType());
                newsDTO.setPublishDate(news.getPublishDate());
                //图片地址拼接
                String title = news.getTitleImageList();
                newsDTO.setTitleImageList(staticImagePrefix+title);//设置输出图片
                return newsDTO;
            }
        });
        return WarehouseAuditDTOPage;
    }

    @Override
    public Boolean addNews(NewsDTO newsDTO) {
        News news = new News();
        BeanUtils.copyProperties(newsDTO,news);
        news.setNewsNo(createNewsNo());
        Long userId = SecurityUtils.getCurrentUserId();
        news.setCreateSmUserId(userId);
        news.setStatus("1");//设置删除标识 1 正常 0 停用
        news.setPublishDate(newsDTO.getPublishDate());
        return newsRepository.save(news) != null;
    }

    @Override
    public NewsDTO getNewsDetail(NewsDTO newsDTO) {
        News news = newsRepository.findOne(newsDTO.getId());
        NewsDTO dto = new NewsDTO();
        BeanUtils.copyProperties(news,dto,"bond, activated, starLevel, langKey, imageUrl, activationKey, sex, roleName, org, orgId");
        dto.setPublishDate(news.getPublishDate());
        //图片地址拼接
        String title = news.getTitleImageList();
        dto.setTitleImageList(staticImagePrefix+title);//设置输出图片
        return dto;
    }

    @Override
    public Boolean updateNews(NewsDTO newsDTO) {
        News news = newsRepository.findOne(newsDTO.getId());
        BeanUtils.copyProperties(newsDTO,news);
        if(newsDTO.getTitleImageList().contains("http://")){
            String imageList = newsDTO.getTitleImageList();
            news.setTitleImageList(imageList.substring(staticImagePrefix.length()));
        }
        Long userId = SecurityUtils.getCurrentUserId();
        news.setCreateSmUserId(userId);
        news.setPublishDate(newsDTO.getPublishDate());
        news.setLastUpdatedBy(userId);
        news.setLastUpdatedDate(Instant.now().getEpochSecond());
        return newsRepository.save(news) != null;
    }

    @Override
    public Boolean updateNewsStatus(NewsDTO newsDTO) {
        News news = newsRepository.findOne(newsDTO.getId());
        news.setNewsStatus(newsDTO.getNewsStatus());
        return newsRepository.save(news) != null;
    }

    @Override
    public Boolean deleteNews(NewsDTO newsDTO) {
        News news = newsRepository.findOne(newsDTO.getId());
        news.setStatus("0");
        return newsRepository.save(news) != null;
    }


    /**
     * 创建信息单号
     * @return
     */
    public synchronized String createNewsNo() {
        String newsNo = null;
        int init = 0;
        while (true) {
            newsNo = "I"+ StringHelper.frontCompWithZore(++init, 7);
            News news = newsRepository.findByNewsNo(newsNo);
            if (news == null) {
                return newsNo;
            }
        }
    }

}
