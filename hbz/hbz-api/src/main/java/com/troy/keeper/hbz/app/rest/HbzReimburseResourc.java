package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.PayDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzReimburseDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.HbzReimburseService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.ReimburseProgress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/3.
 */
@RestController
@RequestMapping("/api/reimburse")
public class HbzReimburseResourc {

    @Autowired
    HbzReimburseService hbzReimburseService;

    @Autowired
    HbzPayService hbzPayService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    //TODO 统一退款单提交接口
    //生成退款单
    @RequestMapping(value = "/reimburse", method = RequestMethod.POST)
    public ResponseDTO reimburse(@RequestBody PayDTO payDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String businessNo = null;
        switch (payDTO.getBusinessType()) {
            case ORDER: {
                HbzOrderDTO order = hbzOrderService.findByOrderNo(payDTO.getOrderNo());
                businessNo = order.getOrderNo();
            }
            break;
            case BOND: {

            }
            break;
            default: {
                return new ResponseDTO(Const.STATUS_ERROR, "不支付业务");
            }
        }


        HbzReimburseDTO reimburse = new HbzReimburseDTO();
        reimburse.setBusinessNo(businessNo);
        reimburse.setBusinessType(payDTO.getBusinessType());

        HbzPayDTO query = new HbzPayDTO();
        query.setStatus("1");
        query.setPayProgresses(Arrays.asList(PayProgress.SUCCESS));
        query.setBusinessNo(businessNo);
        query.setBusinessType(BusinessType.ORDER);

        List<HbzPayDTO> paies = hbzPayService.query(query);
        if (paies == null)
            return new ResponseDTO(Const.STATUS_ERROR, "未找到支付信息");
        else if (paies.size() != 1)
            return new ResponseDTO(Const.STATUS_OK, "找到了多条支付订单");
        else {
            HbzPayDTO pay = paies.get(0);
            reimburse.setFee(pay.getFee());
            reimburse.setPayType(pay.getPayType());
            reimburse.setReProgress(ReimburseProgress.NotBeginning);
            reimburse.setStatus(Const.STATUS_ENABLED);
            reimburse.setTradeNo(pay.getTradeNo());
            if (hbzReimburseService.save(reimburse) != null)
                return new ResponseDTO(Const.STATUS_OK, null, null);
            else
                return new ResponseDTO(Const.STATUS_ERROR, "创建退款单失败");
        }
    }

    /**
     * @param hbzReimburseDTO
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseDTO list(@RequestBody HbzReimburseDTO hbzReimburseDTO) {
        HbzReimburseDTO q = new HbzReimburseDTO();
        BeanUtils.copyProperties(hbzReimburseDTO, q);
        q.setStatus(Const.STATUS_ENABLED);
        List<HbzReimburseDTO> list = hbzReimburseService.query(q);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapReimburse).collect(Collectors.toList()));
    }

}
