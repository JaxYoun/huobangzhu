package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.dto.HbzPayMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/3/6.
 */
@RestController
@RequestMapping({"/api/pay"})
public class PayResource {

    @Autowired
    HbzPayService hbzPayService;

    @Label("管理端 - 支付 - 支付记录查询")
    @RequestMapping("/query")
    public ResponseDTO query(@RequestBody HbzPayMapDTO queryMapDTO) {
        HbzPayDTO query = new HbzPayDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateLT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateGE", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(queryMapDTO, query);

        if (query.getCreatedDateLE() != null)
            query.setCreatedDateLE(query.getCreatedDateLE() + 24L * 3600L * 1000L);
        if (query.getCreatedDateLT() != null)
            query.setCreatedDateLT(query.getCreatedDateLT() + 24L * 3600L * 1000L);

        query.setStatus("1");
        List<HbzPayDTO> pays = hbzPayService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "Success", pays.stream().map(MapSpec::mapPay).collect(Collectors.toList()));
    }

    @Label("管理端 - 支付 - 支付记录查询 - 分页")
    @RequestMapping("/queryPage")
    public ResponseDTO queryPage(@RequestBody HbzPayMapDTO queryMapDTO) {
        HbzPayDTO query = new HbzPayDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateLT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createdDateGE", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(queryMapDTO, query);

        if (query.getCreatedDateLE() != null)
            query.setCreatedDateLE(query.getCreatedDateLE() + 24L * 3600L * 1000L);
        if (query.getCreatedDateLT() != null)
            query.setCreatedDateLT(query.getCreatedDateLT() + 24L * 3600L * 1000L);

        query.setStatus("1");
        Page<HbzPayDTO> pays = hbzPayService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "Success", pays.map(MapSpec::mapPay));
    }
}
