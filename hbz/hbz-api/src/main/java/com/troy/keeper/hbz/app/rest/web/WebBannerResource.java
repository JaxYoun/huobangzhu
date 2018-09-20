package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.BannerDTO;
import com.troy.keeper.hbz.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/5 11:07
 */
@Slf4j
@RestController
@RequestMapping("/api/web/banner")
public class WebBannerResource {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取banner列表
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/getBannerListByLocation")
    public ResponseDTO getBannerListByLocation(@RequestBody BannerDTO bannerDTO) {
        if(StringUtils.isBlank(bannerDTO.getLocation())) {
            return new ResponseDTO(Const.STATUS_ERROR, "失败，请传入banner位置", null);
        }
        bannerDTO.setIfEnable(Const.STATUS_ENABLED);
        bannerDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "成功", this.bannerService.getBannerListByLocation(bannerDTO));
    }

}
