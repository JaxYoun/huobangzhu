package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.AppVersionDTO;
import com.troy.keeper.hbz.service.AppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/11 14:12
 */
@Slf4j
@RestController
@RequestMapping("/api/web/appVersion")
public class AppVersionResource {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * 根据系统类型获取app
     *
     * @param appVersionDTO
     * @return
     */
    @PostMapping("/getRecentAppByPlatformType")
    public ResponseDTO getRecentAppByPlatformType(@RequestBody AppVersionDTO appVersionDTO) {
        if (StringUtils.isBlank(appVersionDTO.getType())) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入系统类型", null);
        }
        appVersionDTO.setStatus(Const.STATUS_ENABLED);
        appVersionDTO.setIsDisable(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "成功", appVersionService.getRecentAppByPlatformType(appVersionDTO));
    }

}
