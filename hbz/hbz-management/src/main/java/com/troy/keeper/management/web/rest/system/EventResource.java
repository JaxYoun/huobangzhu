package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.config.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.service.HbzEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：YangJx
 * @Description：Applog
 * @DateTime：2018/1/22 10:42
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/appLog")
public class EventResource {

    @Autowired
    private HbzEventService hbzEventService;

    /**
     * 分页条件查询app日志详情
     *
     * @param hbzEventDTO
     * @return
     */
    @PostMapping("/getAppLogListByPage")
    public ResponseDTO getAppLogListByPage(@RequestBody HbzEventDTO hbzEventDTO) {
        return new ResponseDTO(Const.CODE_200, "成功", this.hbzEventService.queryPage(hbzEventDTO, hbzEventDTO.getPageRequest()));
    }

    /**
     * 获取app日志详情
     *
     * @param hbzEventDTO
     * @return
     */
    @PostMapping("/getAppLogDetail")
    public ResponseDTO getAppLogDetail(@RequestBody HbzEventDTO hbzEventDTO) {
        if (hbzEventDTO.getId() == null) {
            return new ResponseDTO(Const.CODE_500, "请传入id", null);
        }
        return new ResponseDTO(Const.CODE_200, "成功", this.hbzEventService.findById(hbzEventDTO.getId()));
    }

}