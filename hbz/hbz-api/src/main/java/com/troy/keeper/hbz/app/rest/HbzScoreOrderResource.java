package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzScoreOrderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzProduct;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.HbzProductMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreOrderMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
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
    HbzProductMapper hbzProductMapper;

    @Autowired
    HbzCreditRecordService hbzCreditRecordService;

    @Autowired
    HbzAreaService hbzAreaService;

    @Autowired
    HbzScoreChangeService hbzScoreChangeService;

    /**
     * 新增商品类型
     *
     * @param scoreOrderMapDTO
     * @return
     */
    @Label("App端 - 积分商城 - 用户 - 创建积分兑换订单")
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseDTO createScoreOrder(@RequestBody HbzScoreOrderMapDTO scoreOrderMapDTO) {
        String[] err = ValidationHelper.valid(scoreOrderMapDTO, "h_score_order_c");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", err);
        }
        String orderNo = hbzScoreOrderService.createOrderNo();
        HbzProductDTO product = entityService.get(HbzProduct.class, productMapper, scoreOrderMapDTO.getProductId());

        if (product == null) return new ResponseDTO(Const.STATUS_ERROR, "找不产品id");
        if (product.getLeave().equals(Long.valueOf(0))) return new ResponseDTO(Const.STATUS_ERROR, "没有库存了");

        HbzScoreDTO userScore = hbzScoreService.attach(hbzUserService.currentUser().getId());
        if (userScore.getScore() < product.getScore()) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，积分不足");
        } else if (product.getLeave() < 1L) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，库存量不足");
        } else {
            HbzScoreOrderDTO order = new HbzScoreOrderDTO();
            new Bean2Bean().copyProperties(scoreOrderMapDTO, order);
            if (order.getAreaCode() != null && !order.getAreaCode().equals("")) {
                HbzAreaDTO area = hbzAreaService.findByOutCode(order.getAreaCode());
                if (area != null) {
                    order.setAreaId(area.getId());
                    order.setArea(area);
                }
            }
            order.setOrderNo(orderNo);
            order.setState(1);
            order.setProductId(product.getId());
            order.setScoreTime(Clock.systemUTC().millis());
            order.setProduct(product);
            order.setStatus(Const.STATUS_ENABLED);
            order.setUser(hbzUserService.currentUser());
            order.setUserId(hbzUserService.currentUser().getId());
            order = entityService.save(order, hbzScoreOrderMapper);
            if (order != null) {

                userScore.setScore(userScore.getScore() - product.getScore());
                product.setLeave(product.getLeave() - 1);

                entityService.save(product, hbzProductMapper);
                hbzScoreService.save(userScore);

                HbzScoreChangeDTO scoreChange = new HbzScoreChangeDTO();
                scoreChange.setUser(hbzUserService.currentUser());
                scoreChange.setAction(Const.SCORE_CHG_ACTION_ALL);
                scoreChange.setAdjustType(Const.SCORE_CHG_ADJUEST_TYPE_CHG);
                scoreChange.setDelta((int) product.getScore().longValue() * (-1));
                scoreChange.setMsg("兑换商品[" + product.getProductName() + "]");
                scoreChange.setTime(System.currentTimeMillis());
                scoreChange.setRecNo(hbzCreditRecordService.createNo());
                scoreChange.setType(Const.SCORE_CHG_TYPE_CHG);
                scoreChange.setStatus("1");
                hbzScoreChangeService.save(scoreChange);

                return new ResponseDTO(Const.STATUS_OK, "成功");
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "失败");
            }
        }
    }

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

    /**
     * 查询自己的积分兑换订单
     *
     * @param scoreOrderMapDTO
     * @return
     */
    @RequestMapping(value = {"/my/query"}, method = RequestMethod.POST)
    public ResponseDTO queryMyScoreOrder(@RequestBody HbzScoreOrderMapDTO scoreOrderMapDTO) {
        HbzScoreOrderDTO query = new HbzScoreOrderDTO();
        new Bean2Bean().copyProperties(scoreOrderMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(hbzUserService.currentUser().getId());
        List<HbzScoreOrderDTO> list = hbzScoreOrderService.query(query);
        return new ResponseDTO(Const.STATUS_ERROR, "成功", list.stream().map(MapSpec::mapScoreOrder).collect(Collectors.toList()));
    }

    @RequestMapping(value = {"/my/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPMyScoreOrder(@RequestBody HbzScoreOrderMapDTO scoreOrderMapDTO) {
        HbzScoreOrderDTO query = new HbzScoreOrderDTO();
        new Bean2Bean().copyProperties(scoreOrderMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(hbzUserService.currentUser().getId());
        Page<HbzScoreOrderDTO> page = hbzScoreOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_ERROR, "成功", page.map(MapSpec::mapScoreOrder));
    }
}
