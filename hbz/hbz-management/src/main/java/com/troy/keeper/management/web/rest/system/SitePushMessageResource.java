package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.SitePushMessageDTO;
import com.troy.keeper.hbz.dto.SitePushMessageRecordDTO;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.SitePushMessageRecordService;
import com.troy.keeper.hbz.service.SitePushMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：站内推送消息相关业务
 * @DateTime：2018/1/16 11:06
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/sitePushMessage")
public class SitePushMessageResource {

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private SitePushMessageService sitePushMessageService;

    @Autowired
    private SitePushMessageRecordService sitePushMessageRecordService;

    /**
     * 添加站内推送消息
     *
     * @param sitePushMessageDTO
     * @param validResult
     * @return
     */
    @PostMapping("/addSitePushMessage")
    public ResponseDTO addSitePushMessage(@RequestBody @Valid SitePushMessageDTO sitePushMessageDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        //定时发送时必须填写发送时间
        if ("2".equals(sitePushMessageDTO.getPushType()) && sitePushMessageDTO.getSendTime() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "定时发送必须填写发送时间", null);
        }
        //向单个用户发送时，手机号必填，且必须验证在系统内的可用性
        if ("4".equals(sitePushMessageDTO.getConsumerType())) {
            if (StringUtils.isNotBlank(sitePushMessageDTO.getConsumerPhoneNo())) {
                if (!hbzUserService.checkActiveUserPhone(sitePushMessageDTO.getConsumerPhoneNo())) {
                    return new ResponseDTO(Const.STATUS_ERROR, "手机号码无效", null);
                }
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "手机号码必填", null);
            }
        }
        sitePushMessageDTO.setStatus(Const.STATUS_ENABLED);
        sitePushMessageDTO.setCreatedBy(SecurityUtils.getCurrentUserId());
        sitePushMessageDTO.setCreatedDate(Instant.now().getEpochSecond());
        sitePushMessageDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        sitePushMessageDTO.setLastUpdatedBy(Instant.now().getEpochSecond());
        sitePushMessageDTO.setIfSend(Const.STATUS_DISABLED);
        if (this.sitePushMessageService.addSitePushMessage(sitePushMessageDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "添加成功！", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "添加失败！", null);
    }

    /**
     * 删除站内推送信息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @PostMapping("/deleteSitePushMessage")
    public ResponseDTO deleteSitePushMessage(@RequestBody SitePushMessageDTO sitePushMessageDTO) {
        if (sitePushMessageDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        sitePushMessageDTO.setStatus(Const.STATUS_DISABLED);
        sitePushMessageDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        sitePushMessageDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if(this.sitePushMessageService.deleteSitePushMessage(sitePushMessageDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
        }else{
            return new ResponseDTO(Const.STATUS_OK, "操作失败", null);
        }
    }

    /**
     * 获取站内推送信息详情
     *
     * @param sitePushMessageDTO
     * @return
     */
    @PostMapping("/getSitePushMessageDetail")
    public ResponseDTO getSitePushMessageDetail(@RequestBody SitePushMessageDTO sitePushMessageDTO) {
        if (sitePushMessageDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        sitePushMessageDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", this.sitePushMessageService.getSitePushMessageDetail(sitePushMessageDTO));
    }

    /**
     * 修改站内推送消息
     *
     * @param sitePushMessageDTO
     * @param validResult
     * @return
     */
    @PostMapping("/updateSitePushMessage")
    public ResponseDTO updateSitePushMessage(@RequestBody @Valid SitePushMessageDTO sitePushMessageDTO, BindingResult validResult) {
        if (sitePushMessageDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id！", null);
        }
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        sitePushMessageDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        sitePushMessageDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.sitePushMessageService.updateSitePushMessage(sitePushMessageDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "修改成功！", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "修改失败！", null);
    }

    /**
     * 分页条件查询站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @PostMapping("/getSitePushMessageListByPage")
    public ResponseDTO getSitePushMessageListByPage(@RequestBody SitePushMessageDTO sitePushMessageDTO) {
        sitePushMessageDTO.setStatus(Const.STATUS_ENABLED);
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(sitePushMessageDTO.getPage(), sitePushMessageDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", this.sitePushMessageService.getSitePushMessageListByPage(sitePushMessageDTO, pageable));
    }

    /**
     * 推送站内消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @PostMapping("/sendSitePushMessage")
    public ResponseDTO sendSitePushMessage(@RequestBody SitePushMessageDTO sitePushMessageDTO) {
        if (sitePushMessageDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }

        sitePushMessageDTO.setStatus(Const.STATUS_ENABLED);
        sitePushMessageDTO.setIfSend("0");
        if (!this.sitePushMessageService.getSitePushMessageById(sitePushMessageDTO)) {
            return new ResponseDTO(Const.STATUS_ERROR, "消息状态无效", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "推送成功", this.sitePushMessageService.sendSitePushMessage(sitePushMessageDTO));
    }

    /**
     * 分页条件查询推送记录
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    @PostMapping("/getSitePushMessageRecordListByPage")
    public ResponseDTO getSitePushMessageRecordListByPage(@RequestBody SitePushMessageRecordDTO sitePushMessageRecordDTO) {
        sitePushMessageRecordDTO.setStatus(Const.STATUS_ENABLED);
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(sitePushMessageRecordDTO.getPage(), sitePushMessageRecordDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "成功", this.sitePushMessageRecordService.getSitePushMessageRecordListByPage(sitePushMessageRecordDTO, pageable, false));
    }

    /**
     * 获取发送记录详情
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    @PostMapping("/getSitePushMessageRecordDetail")
    public ResponseDTO getSitePushMessageRecordDetail(@RequestBody SitePushMessageRecordDTO sitePushMessageRecordDTO) {
        if (sitePushMessageRecordDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", this.sitePushMessageRecordService.getSitePushMessageRecordDetail(sitePushMessageRecordDTO.getId(), false));
    }

    /**
     * 验证电话号码是否有效
     *
     * @param paramMap
     * @return
     */
    @PostMapping("/checkActiveUserPhone")
    public ResponseDTO checkActiveUserPhone(@RequestBody Map<String, String> paramMap) {
        if (StringUtils.isBlank(paramMap.get("phoneNo"))) {
            return new ResponseDTO(Const.STATUS_ERROR, "电话号码不能为空！", null);
        } else {
            if (this.hbzUserService.checkActiveUserPhone(paramMap.get("phoneNo"))) {
                return new ResponseDTO(Const.STATUS_OK, "电话号码有效！", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "电话号码无效！", null);
            }
        }
    }

}