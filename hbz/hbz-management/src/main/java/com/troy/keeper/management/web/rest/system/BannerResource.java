package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.BannerDTO;
import com.troy.keeper.hbz.service.BannerService;
import com.troy.keeper.hbz.vo.BannerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：Banner相关业务控制层
 * @DateTime：2018/1/16 9:43
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/banner")
public class BannerResource {

    @Autowired
    private BannerService bannerService;

    /**
     * 添加banner
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/addBanner")
    public ResponseDTO addBanner(@RequestBody @Valid BannerDTO bannerDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        bannerDTO.setCreatedBy(SecurityUtils.getCurrentUserId());
        bannerDTO.setCreatedDate(Instant.now().getEpochSecond());
        bannerDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        bannerDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        bannerDTO.setStatus(Const.STATUS_ENABLED);

        if (this.bannerService.addBanner(bannerDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "添加成功！", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "添加失败！", null);
    }

    /**
     * 获取banner详情
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/getBannerDetail")
    public ResponseDTO getBannerDetail(@RequestBody BannerDTO bannerDTO) {
        if (bannerDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id！", null);
        }
        bannerDTO.setStatus(Const.STATUS_ENABLED);
        BannerVO bannerVO = this.bannerService.getBannerDetail(bannerDTO);
        return new ResponseDTO(Const.STATUS_OK, "成功！", bannerVO);
    }

    /**
     * 修改banner
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/updateBanner")
    public ResponseDTO updateBanner(@RequestBody @Valid BannerDTO bannerDTO, BindingResult validResult) {
        if (bannerDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id！", null);
        }
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        bannerDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        bannerDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.bannerService.updateBanner(bannerDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "修改成功！", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "修改失败！", null);
    }

    /**
     * 启用或禁用banner
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/enableOrDisableBanner")
    public ResponseDTO enableOrDisableBanner(@RequestBody BannerDTO bannerDTO) {
        if (bannerDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id！", null);
        }

        bannerDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        bannerDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.bannerService.enableOrDisableBanner(bannerDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "操作成功！", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "操作失败！", null);
    }

    /**
     * 分页条件查询banner
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/getBannerListByPage")
    public ResponseDTO getBannerListByPage(@RequestBody BannerDTO bannerDTO) {
        bannerDTO.setStatus(Const.STATUS_ENABLED);
        List<Sort.Order> orderList = new ArrayList<>(3);
        orderList.add(new Sort.Order(Sort.Direction.ASC, "sortNo"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(bannerDTO.getPage(), bannerDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", this.bannerService.getBannerListByPage(bannerDTO, pageable));
    }

    /**
     * 删除banner
     * @param bannerDTO
     * @return
     */
    @PostMapping("/deleteBanner")
    public ResponseDTO deleteBanner(@RequestBody BannerDTO bannerDTO) {
        if (bannerDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id！", null);
        }
        bannerDTO.setStatus(Const.STATUS_DISABLED);
        bannerDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        bannerDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.bannerService.deleteBanner(bannerDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "操作成功！", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "操作失败！", null);
    }

}