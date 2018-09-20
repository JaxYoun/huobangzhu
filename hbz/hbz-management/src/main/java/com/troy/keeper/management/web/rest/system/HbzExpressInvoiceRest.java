package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzExpressInvoiceDTO;
import com.troy.keeper.hbz.dto.HbzScoreOrderDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzExpressInvoiceService;
import com.troy.keeper.hbz.service.HbzScoreOrderService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.management.dto.HbzExpressInvoiceMapDTO;
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
 * Created by leecheng on 2018/1/5.
 */
@RestController
public class HbzExpressInvoiceRest {

    @Autowired
    HbzExpressInvoiceService hbzExpressInvoiceService;
    @Autowired
    HbzScoreOrderService hbzScoreOrderService;

    /**
     * 保存订单
     *
     * @param expressInvoiceMapDTO
     * @return
     */
    @RequestMapping(value = "/api/orderScore/invoice", method = RequestMethod.POST)
    public ResponseDTO createScoreOrderExpressInvoice(@RequestBody HbzExpressInvoiceMapDTO expressInvoiceMapDTO) {
        String[] err = ValidationHelper.valid(expressInvoiceMapDTO, "cr_order_e");
        if (err.length > 0) return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        HbzExpressInvoiceDTO hbzExpressInvoice = new HbzExpressInvoiceDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("sendTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(expressInvoiceMapDTO, hbzExpressInvoice);
        hbzExpressInvoice.setOrderType(Const.SCORE_ORDER);
        hbzExpressInvoice.setStatus("1");

        HbzScoreOrderDTO queryScoreOrder = new HbzScoreOrderDTO();
        queryScoreOrder.setOrderNo(expressInvoiceMapDTO.getOrderNo());
        queryScoreOrder.setStatus("1");
        hbzScoreOrderService.query(queryScoreOrder).stream().map(o -> {
            o.setState(2);
            return o;
        }).forEach(hbzScoreOrderService::save);

        hbzExpressInvoiceService.save(hbzExpressInvoice);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }


    /**
     * 查询
     *
     * @param expressInvoiceMapDTO
     * @return
     */
    @RequestMapping(value = "/api/invoice/query", method = RequestMethod.POST)
    public ResponseDTO queryExpressInvoice(@RequestBody HbzExpressInvoiceMapDTO expressInvoiceMapDTO) {
        HbzExpressInvoiceDTO hbzExpressInvoice = new HbzExpressInvoiceDTO();
        new Bean2Bean().copyProperties(expressInvoiceMapDTO, hbzExpressInvoice);
        hbzExpressInvoice.setStatus("1");
        List<HbzExpressInvoiceDTO> list = hbzExpressInvoiceService.query(hbzExpressInvoice);
        return new ResponseDTO(Const.STATUS_OK, "", list.stream().map(MapSpec::mapExpressInvoice).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/api/invoice/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryapmacExpressInvoice(@RequestBody HbzExpressInvoiceMapDTO expressInvoiceMapDTO) {
        HbzExpressInvoiceDTO hbzExpressInvoice = new HbzExpressInvoiceDTO();
        new Bean2Bean().copyProperties(expressInvoiceMapDTO, hbzExpressInvoice);
        hbzExpressInvoice.setStatus("1");
        Page<HbzExpressInvoiceDTO> page = hbzExpressInvoiceService.queryPage(hbzExpressInvoice, hbzExpressInvoice.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "", page.map(MapSpec::mapExpressInvoice));
    }
}
