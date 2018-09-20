package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.mapper.MyHbzUserMapper;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.HbzUserMapper;
import com.troy.keeper.hbz.service.mapper.web.WebFslOrderCreateValidGroup;
import com.troy.keeper.hbz.service.mapper.web.WebFslOrderDTO;
import com.troy.keeper.hbz.service.mapper.web.WebFslOrderService;
import com.troy.keeper.hbz.service.mapper.web.WebFslOrderVO;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.vo.HbzUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/api/web/fslorder")
public class WebFslOrderResource extends BaseResource {

    @Autowired
    private WebFslOrderService webFslService;

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    /**
     * 添加专线整车货源
     *
     * @return
     */
    @PostMapping("/addWebFslOrder")
    public ResponseDTO addWebFslOrder(@RequestBody @ModelAttribute @Validated(WebFslOrderCreateValidGroup.class) WebFslOrderDTO dto) {
        ResponseDTO responseDTO = null;

        dto.setStatus(Const.STATUS_ENABLED);
        dto.setOrderTrans(OrderTrans.NEW);
        dto.setOrderType(OrderType.FSL);
        dto.setOrderNo(hbzOrderService.createNewOrderNo(OrderType.FSL));

        HbzUser hbzUser = hbzUserMapper.newEntity();
        hbzUserMapper.dto2entity(hbzUserService.currentUser(), hbzUser);
        dto.setCreateUser(hbzUser);

        WebFslOrderVO webFslOrderVO = webFslService.addWebFslOrder(dto);
        if (webFslOrderVO != null) {
            responseDTO = new ResponseDTO("200", "添加成功", webFslOrderVO);
        } else {
            responseDTO = new ResponseDTO("400", "添加失败", null);
        }
        return responseDTO;
    }

    /**
     * 删除专线整车货源
     *
     * @param dto
     * @return
     */
    @PostMapping("/deleteFslOrder")
    public ResponseDTO deleteFslOrder(@RequestBody WebFslOrderDTO dto) {
        ResponseDTO responseDTO = null;
        if (dto != null && dto.getId() != null) {
            boolean bool = webFslService.deleteFslOrder(dto);
            responseDTO = bool ? new ResponseDTO("200", "success", bool) : new ResponseDTO("201", "删除失败", bool);
        } else {
            responseDTO = new ResponseDTO("400", "参数非法", null);
        }
        return responseDTO;
    }


    /**
     * 修改专线整车货源
     *
     * @param dto
     * @return
     */
    @PostMapping("/updateFslOrder")
    public ResponseDTO updateFslOrder(@RequestBody WebFslOrderDTO dto) {
        ResponseDTO responseDTO = null;
        if (webFslService.updateFslOrder(dto)) {
            responseDTO = new ResponseDTO("200", "success", null);
        } else {
            responseDTO = new ResponseDTO("201", "failed", null);
        }
        return responseDTO;
    }

    /**
     * 查询专线整车货源详情
     *
     * @param dto
     * @return
     */
    @PostMapping("/getFslOrderById")
    public ResponseDTO getFslOrderById(@RequestBody WebFslOrderDTO dto) {
        ResponseDTO responseDTO = null;
        if (dto != null && dto.getId() != null) {
            WebFslOrderVO webFslOrderVO = webFslService.getFslOrderById(dto);
            responseDTO = new ResponseDTO("200", "success", webFslOrderVO);
        } else {
            responseDTO = new ResponseDTO("400", "参数非法", null);
        }
        return responseDTO;
    }

}

interface MyFunction extends Function<String, String> {}