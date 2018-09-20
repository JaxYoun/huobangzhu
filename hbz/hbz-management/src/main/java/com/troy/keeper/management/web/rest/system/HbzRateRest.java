package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRateDTO;
import com.troy.keeper.hbz.service.HbzRateService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.dto.HbzRateMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/19.
 */
@RestController
@RequestMapping("/api/rate")
public class HbzRateRest {

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzRateService hbzRateService;

    @Label("管理端 - 评分 - 查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO rateQuery(@RequestBody HbzRateMapDTO hbzRateMapDTO) {
        HbzRateDTO rateQuery = new HbzRateDTO();
        new Bean2Bean().copyProperties(hbzRateMapDTO, rateQuery);
        rateQuery.setStatus("1");
        List<HbzRateDTO> rates = hbzRateService.query(rateQuery);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", rates.stream().map(MapSpec::mapRate).collect(Collectors.toList()));
    }

    @RequestMapping("/queryPage")
    public ResponseDTO page(@RequestBody HbzRateMapDTO hbzRateMapDTO) {
        HbzRateDTO rateQuery = new HbzRateDTO();
        new Bean2Bean().copyProperties(hbzRateMapDTO, rateQuery);
        rateQuery.setStatus("1");
        Page<HbzRateDTO> page = hbzRateService.queryPage(rateQuery, rateQuery.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(MapSpec::mapRate));
    }
}
