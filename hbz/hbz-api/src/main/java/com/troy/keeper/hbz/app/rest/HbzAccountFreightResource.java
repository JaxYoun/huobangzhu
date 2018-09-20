package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.app.constant.CommonConstants;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAccountFreightDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzAccountFreightService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.Bean2Map;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/20.
 */
@RestController
@RequestMapping("/api/user")
public class HbzAccountFreightResource {

    @Autowired
    HbzAccountFreightService hbzAccountFreightService;

    //常用司机创建
    @Label("App端 - 运输专线 - 常用司机 - 创建")
    @RequestMapping(value = {"/freight/create"}, method = RequestMethod.POST)
    public ResponseDTO freightCreate(@RequestBody HbzAccountFreightDTO accountFreightDTO) {
        accountFreightDTO.setId(null);
        HbzAccountFreightDTO accountFreight = new HbzAccountFreightDTO();
        new Bean2Bean().copyProperties(accountFreightDTO, accountFreight);
        if (accountFreight.getIndex() == null) accountFreight.setIndex(Integer.MIN_VALUE);
        accountFreight.setHostId(SecurityUtils.getCurrentUserId());
        accountFreight.setStatus(Const.STATUS_ENABLED);
        accountFreight = hbzAccountFreightService.save(accountFreight);
        if (accountFreight != null)
            return new ResponseDTO(Const.STATUS_OK, "保存成功");
        else
            return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    //常用司机更新
    @Label("App端 - 运输专线 - 常用司机 - 更新")
    @RequestMapping(value = {"/freight/update"}, method = RequestMethod.POST)
    public ResponseDTO freightUpdate(@RequestBody HbzAccountFreightDTO accountFreightDTO) {
        if (accountFreightDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", "ID非法");
        }
        HbzAccountFreightDTO accountFreight = hbzAccountFreightService.get(accountFreightDTO);
        if (accountFreight == null) return new ResponseDTO(Const.STATUS_ERROR, "错误", "ID非法");
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(accountFreightDTO, accountFreight);
        if (accountFreight.getIndex() == null) accountFreight.setIndex(Integer.MIN_VALUE);

        accountFreight.setStatus(Const.STATUS_ENABLED);
        accountFreight.setHostId(SecurityUtils.getCurrentUserId());
        accountFreight = hbzAccountFreightService.save(accountFreight);
        if (accountFreight != null)
            return new ResponseDTO(Const.STATUS_OK, "保存成功");
        else
            return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    //常用司机删除
    @Label("App端 - 运输专线 - 常用司机 - 删除")
    @RequestMapping(value = {"/freight/delete"}, method = RequestMethod.POST)
    public ResponseDTO freightd(@RequestBody HbzAccountFreightDTO accountFreightDTO) {
        if (accountFreightDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", "ID非法");
        }
        hbzAccountFreightService.delete(accountFreightDTO);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    //常用司机查询
    @Label("App端 - 运输专线 - 常用司机 - 查询")
    @RequestMapping(value = {"/freight/query"}, method = RequestMethod.POST)
    public ResponseDTO freightQuery(@RequestBody HbzAccountFreightDTO accountFreightDTO) {
        accountFreightDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzAccountFreightService.query(accountFreightDTO).stream().map(this::map).collect(Collectors.toList()));
    }

    //常用司机查询
    @Label("App端 - 运输专线 - 常用司机 - 查询 - 分页")
    @RequestMapping(value = {"/freight/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO freightp(@RequestBody HbzAccountFreightDTO accountFreightDTO) {
        accountFreightDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzAccountFreightService.queryPage(accountFreightDTO, accountFreightDTO.getPageRequest()).map(this::map));
    }

    private Map<String, Object> map(HbzAccountFreightDTO dto) {
        return new Bean2Map(new PropertyMapper<>("user", new Bean2Map().addIgnores("activated", "password").addIgnores(CommonConstants.commonIgnores)::map), new PropertyMapper<>("host", new Bean2Map().addIgnores(CommonConstants.commonIgnores).addIgnores("activated", "password")::map)).addIgnores(CommonConstants.commonIgnores).map(dto);
    }
}
