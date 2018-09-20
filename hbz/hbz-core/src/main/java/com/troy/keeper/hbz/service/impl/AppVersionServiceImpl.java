package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.hbz.dto.AppVersionDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.HbzVersionManagement;
import com.troy.keeper.hbz.service.AppVersionService;
import com.troy.keeper.hbz.vo.AppVersionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/11 14:24
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private CommonRepository commonRepository;

    /**
     * 根据系统类型获取app
     *
     * @param appVersionDTO
     * @return
     */
    @Override
    public AppVersionVO getRecentAppByPlatformType(AppVersionDTO appVersionDTO) {
        List<HbzVersionManagement> appVersionList = commonRepository.findAll(HbzVersionManagement.class, (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(appVersionDTO.getIsDisable())) {
                predicateList.add(criteriaBuilder.equal(root.get("isDisable"), appVersionDTO.getIsDisable()));
            }
            if (StringUtils.isNoneBlank(appVersionDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), appVersionDTO.getStatus()));
            }
            if (StringUtils.isNoneBlank(appVersionDTO.getType())) {
                predicateList.add(criteriaBuilder.equal(root.get("type"), appVersionDTO.getType()));
            }
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });

        if (appVersionList.size() > 0) {
            List<HbzVersionManagement> tmpList = appVersionList.stream().sorted((a, b) -> this.versionCodeCompare(a.getVersionNo(), b.getVersionNo(), "\\.")).collect(Collectors.toList());
            Collections.reverse(tmpList);
            return this.entityToVo(tmpList.get(0));
        } else {
            return null;
        }
    }

    private AppVersionVO entityToVo(HbzVersionManagement hbzVersionManagement) {
        if (hbzVersionManagement == null) {
            return null;
        }
        String[] ignoreArr = {"fileUrl", "createdDate", "lastUpdatedDate", "type"};
        AppVersionVO appVersionVO = new AppVersionVO();
        BeanUtils.copyProperties(hbzVersionManagement, appVersionVO, ignoreArr);
        appVersionVO.setType(getPlatformType(hbzVersionManagement.getType()));
        appVersionVO.setFormatedCreatedDate(DateUtils.longToString(hbzVersionManagement.getCreatedDate(), DateUtils.yyyy_MM_dd));
        appVersionVO.setFormatedLastUpdatedDate(DateUtils.longToString(hbzVersionManagement.getLastUpdatedDate(), DateUtils.yyyy_MM_dd));
        appVersionVO.setFileUrl(this.staticImagePrefix + hbzVersionManagement.getFileUrl());
        return appVersionVO;
    }

    /**
     * 手机系统类型转换
     *
     * @param code
     * @return
     */
    private String getPlatformType(String code) {
        String name = null;
        switch (code) {
            case "0": {
                name = "IOS";
                break;
            }
            case "1": {
                name = "Android";
                break;
            }
            case "2": {
                name = "Other";
                break;
            }
            default: {
                break;
            }
        }
        return name;
    }

    public static int versionCodeCompare(String host, String guest, String regular) {
        String[] hostArr = host.split(regular);
        String[] guestArr = guest.split(regular);
        int isGreaterThan = 0;
        for (int i = 0; i < hostArr.length; i++) {
            int ho = Integer.parseInt(hostArr[i]);
            int gu = Integer.parseInt(guestArr[i]);
            if (ho < gu) {
                isGreaterThan = -1;
                break;
            } else if (ho > gu) {
                isGreaterThan = 1;
                break;
            } else {
                continue;
            }
        }
        return isGreaterThan;
    }
}
