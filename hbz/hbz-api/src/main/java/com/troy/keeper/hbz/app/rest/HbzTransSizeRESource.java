package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.service.HbzTransSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/6.
 */
@RestController
@RequestMapping("/api/transSize")
public class HbzTransSizeRESource {

    @Autowired
    HbzTransSizeService hbzTransSizeService;

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ResponseDTO updateDriverLi(@RequestBody HbzTransSizeDTO DTO) {
        HbzTransSizeDTO q = new HbzTransSizeDTO();
        q.setStatus(Const.STATUS_ENABLED);
        List<String[]> sort = new ArrayList<>();
        sort.add(new String[]{"transSize", "asc"});
        q.setSorts(sort);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", hbzTransSizeService.query(q).stream().map(MapSpec::mapTransSize).collect(Collectors.toList()));
    }
}
