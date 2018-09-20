package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzSourceDTO;
import com.troy.keeper.hbz.service.HbzSourceService;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/12.
 */
@Controller
@RequestMapping("/api/src")
public class HbzSourceRest {

    @Autowired
    HbzSourceService hbzSourceService;

    @ResponseBody
    @RequestMapping(value = "/live", method = RequestMethod.POST)
    public ResponseDTO live(@RequestBody HbzSourceDTO hbzSourceDTO) {
        hbzSourceDTO.setStatus("1");
        List<HbzSourceDTO> list = hbzSourceService.query(hbzSourceDTO);
        return new ResponseDTO(Const.STATUS_OK, "成功", list.stream().map(MapSpec::mapSource).collect(Collectors.toList()));
    }

    @ResponseBody
    @RequestMapping(value = "/livePage", method = RequestMethod.POST)
    public ResponseDTO livep(@RequestBody HbzSourceDTO hbzSourceDTO) {
        hbzSourceDTO.setStatus("1");
        Page<HbzSourceDTO> page = hbzSourceService.queryPage(hbzSourceDTO, hbzSourceDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "成功", page.map(MapSpec::mapSource));
    }

}
