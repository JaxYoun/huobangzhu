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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/8.
 */
@RestController
@RequestMapping("/api/authority")
public class HbzAuthRes {

    @Autowired
    HbzAuthService hbzAuthService;

    @Autowired
    HbzURlService hbzURlService;

    /**
     * @param authDTO
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzAuthDTO authDTO) {
        String[] err = ValidationHelper.valid(authDTO, "auth_a");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzAuthDTO auth = new HbzAuthDTO();
        new Bean2Bean().copyProperties(authDTO, auth);
        auth.setStatus("1");
        hbzAuthService.save(auth);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * @param authDTO
     * @return
     */
    @RequestMapping(value = "/merge", method = RequestMethod.POST)
    public ResponseDTO merge(@RequestBody HbzAuthDTO authDTO) {
        HbzAuthDTO auth = hbzAuthService.get(authDTO);
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(authDTO, auth, true);
        auth.setStatus("1");
        hbzAuthService.save(auth);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    /**
     * @param authDTO
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzAuthDTO authDTO) {
        String[] err = ValidationHelper.valid(authDTO, "auth_d");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzAuthDTO auth = hbzAuthService.get(authDTO);
        hbzAuthService.delete(auth);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    @RequestMapping("/makeUrls")
    public ResponseDTO makeUrls(@RequestBody HbzAuthDTO authDTO) {
        HbzAuthDTO auth = hbzAuthService.findById(authDTO.getId());
        if (auth == null)
            return new ResponseDTO(Const.STATUS_ERROR, "auth id非法");
        List<HbzURLDTO> urls = authDTO.getUrlIds().stream().map(hbzURlService::findById).collect(Collectors.toList());
        hbzAuthService.setUrls(auth, urls);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    /**
     * @param queryD
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzAuthDTO queryD) {
        queryD.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzAuthService.query(queryD).stream().map(MapSpec::mapAuth).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzAuthDTO queryD) {
        queryD.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzAuthService.queryPage(queryD, queryD.getPageRequest()).map(MapSpec::mapAuth));
    }
}
