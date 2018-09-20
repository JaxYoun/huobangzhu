package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.config.Const;
import com.troy.keeper.hbz.dto.SmLoginLogDTO;
import com.troy.keeper.hbz.service.SmLoginLogServeic;
import lombok.extern.slf4j.Slf4j;
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
 * @Description：后台登录日志
 * @DateTime：2018/1/22 12:43
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/smLog")
public class SmLoginLogResource {

    @Autowired
    private SmLoginLogServeic smLoginLogServeic;

    /**
     * 获取日志详情
     *
     * @param smLoginLogDTO
     * @return
     */
    @PostMapping("/getSmLogDetail")
    public ResponseDTO getSmLogDetail(@RequestBody SmLoginLogDTO smLoginLogDTO) {
        if (smLoginLogDTO.getId() == null) {
            return new ResponseDTO(Const.CODE_500, "请传入id", null);
        }
        return new ResponseDTO(Const.CODE_200, "成功", smLoginLogServeic.getSmLogDetail(smLoginLogDTO));
    }

    /**
     * 分页条件查询登录日志
     *
     * @param smLoginLogDTO
     * @return
     */
    @PostMapping("/getSmLogListByPage")
    public ResponseDTO getSmLogListByPage(@RequestBody SmLoginLogDTO smLoginLogDTO) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createDt"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(smLoginLogDTO.getPage(), smLoginLogDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.CODE_200, "成功", this.smLoginLogServeic.getSmLogListByPage(smLoginLogDTO, pageable));
    }

}
