package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzBillService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.dto.HbzBillMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/3/8.
 */
@RestController
@RequestMapping({"/api/bill"})
public class BillSrc {

    @Autowired
    HbzBillService hbzBillService;

    @Label("管理端 - 对账单 - 查询")
    @RequestMapping({"/query"})
    public ResponseDTO query(@RequestBody HbzBillMapDTO billMapDTO) {
        HbzBillDTO query = new HbzBillDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("date", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateLT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateGE", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(billMapDTO, query, true);
        query.setStatus("1");
        List<HbzBillDTO> result = hbzBillService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", result.stream().map(MapSpec::mapBill).collect(Collectors.toList()));
    }

    @Label("管理端 - 对账单 - 查询")
    @RequestMapping({"/queryPage"})
    public ResponseDTO queryPage(@RequestBody HbzBillMapDTO billMapDTO) {
        HbzBillDTO query = new HbzBillDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("date", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateLT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("dateGE", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(billMapDTO, query, true);
        query.setStatus("1");
        Page<HbzBillDTO> result = hbzBillService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", result.map(MapSpec::mapBill));
    }
}
