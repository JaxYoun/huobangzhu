package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.dto.HbzScoreDTO;
import com.troy.keeper.hbz.dto.HbzScoreOrderDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzProduct;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.HbzProductMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreOrderMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.dto.HbzScoreOrderMapDTO;
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
 * Created by leecheng on 2017/12/19.
 */
@RestController
@RequestMapping("/api/scoreOrder")
public class HbzScoreOrderResource {

    @Autowired
    HbzProductMapper productMapper;

    @Autowired
    HbzScoreMapper hbzScoreMapper;

    @Autowired
    private EntityService entityService;

    @Autowired
    HbzScoreOrderMapper hbzScoreOrderMapper;

    @Autowired
    HbzScoreService hbzScoreService;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzScoreOrderService hbzScoreOrderService;

    @Autowired
    HbzProductService hbzProductService;

    /**
     * 查询所有积分兑换订单
     *
     * @param scoreOrderMapDTO
     * @return
     */
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO queryScoreOrder(@RequestBody HbzScoreOrderMapDTO scoreOrderMapDTO) {
        HbzScoreOrderDTO query = new HbzScoreOrderDTO();
        new Bean2Bean().copyProperties(scoreOrderMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzScoreOrderDTO> list = hbzScoreOrderService.query(query);
        return new ResponseDTO(Const.STATUS_ERROR, "成功", list.stream().map(MapSpec::mapScoreOrder).collect(Collectors.toList()));
    }

    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPScoreOrder(@RequestBody HbzScoreOrderMapDTO scoreOrderMapDTO) {
        HbzScoreOrderDTO query = new HbzScoreOrderDTO();
        new Bean2Bean().copyProperties(scoreOrderMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzScoreOrderDTO> page = hbzScoreOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_ERROR, "成功", page.map(MapSpec::mapScoreOrder));
    }

}
