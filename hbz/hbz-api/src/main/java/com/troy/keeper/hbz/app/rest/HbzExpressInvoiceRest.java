package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzExpressInvoiceMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzExpressInvoiceDTO;
import com.troy.keeper.hbz.service.HbzExpressInvoiceService;
import com.troy.keeper.hbz.sys.Bean2Bean;
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
 * Created by leecheng on 2018/1/5.
 */
@RestController
public class HbzExpressInvoiceRest {

    @Autowired
    HbzExpressInvoiceService hbzExpressInvoiceService;

    /**
     * 查询
     *
     * @param expressInvoiceMapDTO
     * @return
     */
    @Label("App端 - 积分商城 - 快递 - 查询")
    @RequestMapping(value = "/api/invoice/query", method = RequestMethod.POST)
    public ResponseDTO queryExpressInvoice(@RequestBody HbzExpressInvoiceMapDTO expressInvoiceMapDTO) {
        HbzExpressInvoiceDTO hbzExpressInvoice = new HbzExpressInvoiceDTO();
        new Bean2Bean().copyProperties(expressInvoiceMapDTO, hbzExpressInvoice);
        hbzExpressInvoice.setStatus("1");
        List<HbzExpressInvoiceDTO> list = hbzExpressInvoiceService.query(hbzExpressInvoice);
        return new ResponseDTO(Const.STATUS_OK, "", list.stream().map(MapSpec::mapExpressInvoice).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/api/invoice/queryPage", method = RequestMethod.POST)
    @Label("App端 - 积分商城 - 快递 - 分页查询")
    public ResponseDTO queryapmacExpressInvoice(@RequestBody HbzExpressInvoiceMapDTO expressInvoiceMapDTO) {
        HbzExpressInvoiceDTO hbzExpressInvoice = new HbzExpressInvoiceDTO();
        new Bean2Bean().copyProperties(expressInvoiceMapDTO, hbzExpressInvoice);
        hbzExpressInvoice.setStatus("1");
        Page<HbzExpressInvoiceDTO> page = hbzExpressInvoiceService.queryPage(hbzExpressInvoice, hbzExpressInvoice.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "", page.map(MapSpec::mapExpressInvoice));
    }
}
