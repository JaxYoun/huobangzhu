package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.BannerDTO;
import com.troy.keeper.hbz.vo.BannerVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/16 15:37
 */
public interface BannerService {

    /**
     * 添加banner
     *
     * @param bannerDTO
     * @return
     */
    boolean addBanner(BannerDTO bannerDTO);

    /**
     * 获取banner详情
     *
     * @param bannerDTO
     * @return
     */
    BannerVO getBannerDetail(BannerDTO bannerDTO);

    /**
     * 修改banner
     *
     * @param bannerDTO
     * @return
     */
    boolean updateBanner(BannerDTO bannerDTO);

    /**
     * 启禁用banner
     *
     * @param bannerDTO
     * @return
     */
    boolean enableOrDisableBanner(BannerDTO bannerDTO);

    /**
     * 分页条件查询banner
     *
     * @param bannerDTO
     * @param pageable
     * @return
     */
    Page<BannerVO> getBannerListByPage(BannerDTO bannerDTO, Pageable pageable);

    /**
     * 根据类型和位置获取banner
     *
     * @param bannerDTO
     * @return
     */
    List<BannerVO> getBannerListByLocation(BannerDTO bannerDTO);

    /**
     * 删除banner
     *
     * @param bannerDTO
     * @return
     */
    boolean deleteBanner(BannerDTO bannerDTO);

}