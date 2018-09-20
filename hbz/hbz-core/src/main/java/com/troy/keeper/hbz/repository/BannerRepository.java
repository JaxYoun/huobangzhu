package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.Banner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/16 15:35
 */
public interface BannerRepository extends BaseRepository<Banner, Long> {

    /**
     * 获取banner最大id
     *
     * @return
     */
    @Query("SELECT MAX(t.id) AS id FROM Banner AS t")
    Long getMaxIdBanner();

    /**
     * 启/禁用banner
     *
     * @param ifEnable
     * @param updateUserId
     * @param lastUpdateDate
     * @param id
     * @return
     */
    @Modifying
    @Query("update Banner as t set t.ifEnable = ?1, t.lastUpdatedBy = ?2, t.lastUpdatedDate = ?3 where t.id = ?4")
    int enableOrDisableBanner(String ifEnable, Long updateUserId, Long lastUpdateDate, Long id);

    /**
     * 根据是否删除、是否可用、位置获取banner列表
     *
     * @param status
     * @param ifEnable
     * @param location
     * @return
     */
    List<Banner> getDistinctByStatusAndIfEnableAndLocation(String status, String ifEnable, String location);

}
