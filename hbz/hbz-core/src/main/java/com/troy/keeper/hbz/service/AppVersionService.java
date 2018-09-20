package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.AppVersionDTO;
import com.troy.keeper.hbz.vo.AppVersionVO;
import org.springframework.data.domain.Pageable;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/11 14:23
 */
public interface AppVersionService {

    /**
     * 根据系统类型获取app
     *
     * @param appVersionDTO
     * @return
     */
    AppVersionVO getRecentAppByPlatformType(AppVersionDTO appVersionDTO);

}
