package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzCreditRecordMaPDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCreditRecordDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzCreditRecordService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/19.
 */
@RestController
@RequestMapping("/api/credit")
public class HbzCreditRest {

    @Autowired
    private HbzCreditRecordService hbzCreditRecordService;

    @Autowired
    HbzUserService hbzUserService;

    @Label("App端 - 信誉积分列表 - 查询")
    @RequestMapping("/query")
    public ResponseDTO query(@RequestBody HbzCreditRecordMaPDTO queryDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzCreditRecordDTO query = new HbzCreditRecordDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("time", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLE", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGE", new TimeMillisFormater("yyyy-MM-dd")::parse)
        ).copyProperties(queryDTO, query);
        query.setUserId(user.getId());
        query.setStatus("1");
        List<HbzCreditRecordDTO> list = hbzCreditRecordService.query(query);
        return new ResponseDTO(Const.STATUS_OK, null, list.stream().map(MapSpec::mapCreditRecord).collect(Collectors.toList()));
    }

    @Label("App端 - 信誉积分列表 - 分页查询")
    @RequestMapping("/queryPage")
    public ResponseDTO queryPage(@RequestBody HbzCreditRecordMaPDTO queryDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzCreditRecordDTO query = new HbzCreditRecordDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("time", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeLE", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGT", new TimeMillisFormater("yyyy-MM-dd")::parse),
                new PropertyMapper<>("timeGE", new TimeMillisFormater("yyyy-MM-dd")::parse)
        ).copyProperties(queryDTO, query);
        query.setUserId(user.getId());
        query.setStatus("1");
        Page<HbzCreditRecordDTO> page = hbzCreditRecordService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, null, page.map(MapSpec::mapCreditRecord));
    }
}
