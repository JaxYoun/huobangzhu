package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.BannerDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.Banner;
import com.troy.keeper.hbz.po.HbzTypeVal;
import com.troy.keeper.hbz.repository.BannerRepository;
import com.troy.keeper.hbz.repository.HbzTypeValRepo;
import com.troy.keeper.hbz.service.BannerService;
import com.troy.keeper.hbz.vo.BannerVO;
import com.troy.keeper.hbz.vo.DictionaryVO;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/16 15:39
 */
@Slf4j
@Service
public class BannerServiceImpl implements BannerService {

    private static final char[] CODE_PREFIX_CHAR_ARR = {'B', '0', '0', '0', '0', '0', '0', '0', '0'};

    private Object lock = new Object();

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private HbzTypeValRepo hbzTypeValRepo;

    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 添加banner
     *
     * @param bannerDTO
     * @return
     */
    @Override
    public boolean addBanner(BannerDTO bannerDTO) {
        bannerDTO.setCode(getBannerCode());
        Banner banner = new Banner();
        BeanUtils.copyProperties(bannerDTO, banner);
        return this.bannerRepository.save(banner) != null;
    }

    /**
     * 获取banner详情
     *
     * @param bannerDTO
     * @return
     */
    @Override
    public BannerVO getBannerDetail(BannerDTO bannerDTO) {
        Banner banner = this.bannerRepository.findOne(bannerDTO.getId());
        return entityToVo(banner);
    }

    /**
     * 修改banner
     *
     * @param bannerDTO
     * @return
     */
    @Override
    public boolean updateBanner(BannerDTO bannerDTO) {
        String imagePath = bannerDTO.getImagePath();
        bannerDTO.setImagePath(StringHelper.getTailFromFullImagePath(staticImagePrefix, imagePath));
        Banner oldBanner = this.bannerRepository.findOne(bannerDTO.getId());
        BeanUtils.copyProperties(bannerDTO, oldBanner, "id", "createdBy", "createdDate", "status", "code");
        return this.bannerRepository.save(oldBanner) != null;
    }

    /**
     * 启禁用banner
     *
     * @param bannerDTO
     * @return
     */
    @Override
    @Transactional
    public boolean enableOrDisableBanner(BannerDTO bannerDTO) {
        return this.bannerRepository.enableOrDisableBanner(bannerDTO.getIfEnable(), bannerDTO.getLastUpdatedBy(), bannerDTO.getLastUpdatedDate(), bannerDTO.getId()) > 0;
    }

    /**
     * 分页条件查询banner
     *
     * @param bannerDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<BannerVO> getBannerListByPage(BannerDTO bannerDTO, Pageable pageable) {
        Specification<Banner> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(bannerDTO.getName())) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + bannerDTO.getName() + "%"));
            }
            if (StringUtils.isNotBlank(bannerDTO.getLocation())) {
                predicateList.add(criteriaBuilder.equal(root.get("location"), bannerDTO.getLocation()));
            }
            if (StringUtils.isNotBlank(bannerDTO.getSkipType())) {
                predicateList.add(criteriaBuilder.equal(root.get("skipType"), bannerDTO.getSkipType()));
            }
            if (bannerDTO.getIfEnable() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("ifEnable"), bannerDTO.getIfEnable()));
            }
            if (StringUtils.isNotBlank(bannerDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), bannerDTO.getStatus()));
            }
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });

        Page<Banner> bannerPage = this.bannerRepository.findAll(specification, pageable);
        Page<BannerVO> bannerVOPage = bannerPage.map(this::entityToVo);
        return bannerVOPage;
    }

    /**
     * 根据类型和位置获取banner
     *
     * @param bannerDTO
     * @return
     */
    @Override
    public List<BannerVO> getBannerListByLocation(BannerDTO bannerDTO) {
        return this.bannerRepository.getDistinctByStatusAndIfEnableAndLocation(bannerDTO.getStatus(), bannerDTO.getIfEnable(), bannerDTO.getLocation()).stream().map(this::entityToVo).sorted(Comparator.comparing(it -> it.getSortNo())).collect(Collectors.toList());
    }

    /**
     * 删除banner
     *
     * @param bannerDTO
     * @return
     */
    @Override
    public boolean deleteBanner(BannerDTO bannerDTO) {
        Banner banner = this.bannerRepository.findOne(bannerDTO.getId());
        if(banner != null) {
            banner.setStatus(bannerDTO.getStatus());
            banner.setLastUpdatedBy(bannerDTO.getLastUpdatedBy());
            banner.setLastUpdatedDate(bannerDTO.getLastUpdatedDate());
            return this.bannerRepository.save(banner) != null;
        }else {
            return false;
        }
    }

    /**
     * 根据当前数据库最大id生成编号
     *
     * @return
     */
    private String getBannerCode() {
        synchronized (this.lock) {
            Long maxId = this.bannerRepository.getMaxIdBanner();
            return maxId == null ? StringHelper.contractCode(0L, this.CODE_PREFIX_CHAR_ARR) : StringHelper.contractCode(maxId, this.CODE_PREFIX_CHAR_ARR);
        }
    }

    /**
     * entityToVo
     *
     * @param banner
     * @return
     */
    private BannerVO entityToVo(Banner banner) {
        if (banner == null) {
            return null;
        }
        HbzTypeVal location = this.hbzTypeValRepo.getByTypeAndVal("BannerLocation", banner.getLocation());
        DictionaryVO locationVo = new DictionaryVO();
        BeanUtils.copyProperties(location, locationVo);
        HbzTypeVal skipType = this.hbzTypeValRepo.getByTypeAndVal("BannerSkipType", banner.getSkipType());
        DictionaryVO skipTypeVo = new DictionaryVO();
        BeanUtils.copyProperties(skipType, skipTypeVo);
        String fullIamgePath = this.staticImagePrefix + banner.getImagePath();
        BannerVO bannerVO = new BannerVO();
        bannerVO.setLocation(locationVo);
        bannerVO.setSkipType(skipTypeVo);
        bannerVO.setImagePath(fullIamgePath);
        BeanUtils.copyProperties(banner, bannerVO, "location", "skipType", "imagePath");
        return bannerVO;
    }

}