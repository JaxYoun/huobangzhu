package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzScoreChangeMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzScoreChangeDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzScoreChangeService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/22.
 */
@RestController
@RequestMapping(value = "/api/scoreChange", method = RequestMethod.POST)
public class HbzScoreChangeRest {

    @Autowired
    HbzScoreChangeService hbzScoreChangeService;

    @Autowired
    HbzUserService hbzUserService;

    @Label("App端 - 综合积分记录 - 查询")
    @RequestMapping("/query")
    public ResponseDTO query(@RequestBody HbzScoreChangeMapDTO queryDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzScoreChangeDTO query = new HbzScoreChangeDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("time", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLE", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGE", new TimeMillisFormater("yyyy-MM-dd")::parse)
        ).copyProperties(queryDTO, query);
        query.setUserId(user.getId());
        query.setStatus("1");
        List<HbzScoreChangeDTO> list = hbzScoreChangeService.query(query);
        return new ResponseDTO(Const.STATUS_OK, null, list.stream().map(MapSpec::mapScoreChange).collect(Collectors.toList()));
    }

    @Label("管理端 - 综合积分记录 - 分页查询")
    @RequestMapping("/queryPage")
    public ResponseDTO queryPage(@RequestBody HbzScoreChangeMapDTO queryDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzScoreChangeDTO query = new HbzScoreChangeDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("time", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLE", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGE", new TimeMillisFormater("yyyy-MM-dd")::parse)
        ).copyProperties(queryDTO, query);
        query.setUserId(user.getId());
        query.setStatus("1");
        Page<HbzScoreChangeDTO> page = hbzScoreChangeService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, null, page.map(MapSpec::mapScoreChange));
    }
}
