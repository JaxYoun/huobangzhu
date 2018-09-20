package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzURLDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzAuthService;
import com.troy.keeper.hbz.service.HbzURlService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.utils.MapSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/8.
 */
@Slf4j
@RestController
@RequestMapping("/api/URL")
public class HbzURLRec {

    @Autowired
    HbzURlService hbzURlService;

    @Autowired
    HbzAuthService hbzAuthService;

    /**
     * URL资源添加
     *
     * @param hbzURLDTO
     * @return
     */
    @RequestMapping("/add")
    public ResponseDTO add(@RequestBody HbzURLDTO hbzURLDTO) {
        String[] err = ValidationHelper.valid(hbzURLDTO, "u_c");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzURLDTO url = new HbzURLDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS)
                .copyProperties(hbzURLDTO, url);
        url.setStatus(Const.STATUS_ENABLED);
        if (url.getState() == null) {
            url.setState("1");
        }
        hbzURlService.save(url);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * URL资源管理编辑
     *
     * @param hbzURLDTO
     * @return
     */
    @RequestMapping("/update")
    public ResponseDTO update(@RequestBody HbzURLDTO hbzURLDTO) {
        HbzURLDTO url = hbzURlService.get(hbzURLDTO);
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS)
                .copyProperties(hbzURLDTO, url, true);
        url.setStatus(Const.STATUS_ENABLED);
        if (url.getState() == null) {
            url.setState("1");
        }
        hbzURlService.save(url);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * 删除资源
     *
     * @param hbzURLDTO
     * @return
     */
    @RequestMapping("/delete")
    public ResponseDTO delete(@RequestBody HbzURLDTO hbzURLDTO) {
        String[] err = ValidationHelper.valid(hbzURLDTO, "u_r");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzURLDTO url = hbzURlService.get(hbzURLDTO);
        hbzURlService.delete(url);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * 查询资源
     *
     * @param hbzURLDTO
     * @return
     */
    @RequestMapping("/query")
    public ResponseDTO query(@RequestBody HbzURLDTO hbzURLDTO) {
        hbzURLDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzURlService.query(hbzURLDTO).stream().map(MapSpec::mapResource).collect(Collectors.toList()));
    }

    @RequestMapping("/queryPage")
    public ResponseDTO pageQuery(@RequestBody HbzURLDTO hbzURLDTO) {
        hbzURLDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzURlService.queryPage(hbzURLDTO, hbzURLDTO.getPageRequest()).map(MapSpec::mapResource));
    }

    @RequestMapping("/makeAuth")
    public ResponseDTO auth(@RequestBody HbzURLDTO hbzURLDTO) {
        String[] err = ValidationHelper.valid(hbzURLDTO, "u_a");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzURLDTO url = hbzURlService.findById(hbzURLDTO.getId());
        if (url == null)
            return new ResponseDTO(Const.STATUS_ERROR, "url id非法");
        List<Long> longIds = hbzURLDTO.getAuthIds().stream().filter(a -> hbzAuthService.findById(a) == null).collect(Collectors.toList());
        if (longIds != null && longIds.size() > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "包含非法authid", longIds);
        List<HbzAuthDTO> auths = hbzURLDTO.getAuthIds().stream().map(hbzAuthService::findById).collect(Collectors.toList());
        hbzURlService.setAuths(url, auths);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

}
