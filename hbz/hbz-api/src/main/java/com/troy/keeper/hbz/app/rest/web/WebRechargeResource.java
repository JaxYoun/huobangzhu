package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.HbzRechargeMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRechargeDTO;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.HbzRechargeService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.WebPayService;
import com.troy.keeper.hbz.service.mapper.HbzRechargeMapper;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * @Author：YangJx
 * @Description：保证金相关
 * @DateTime：2017/12/20 15:05
 */
@Slf4j
@RestController
@RequestMapping("/api/web/deposit")
public class WebRechargeResource {

    @Autowired
    private HbzRechargeMapper hbzRechargeMapper;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private HbzPayService hbzPayService;

    @Autowired
    private HbzRechargeService hbzRechargeService;

    @Autowired
    private WebPayService webPayService;

    /**
     * 创建保证金缴纳订单
     *
     * @param hbzRechargeMapDTO
     * @return
     */
    @PostMapping("/createDepositOrder")
    public ResponseDTO createDepositOrder(@RequestBody HbzRechargeMapDTO hbzRechargeMapDTO) {
        if (hbzRechargeMapDTO.getMoney() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "保证金金额不能为空！", null);
        }
        if (!(hbzRechargeMapDTO.getRole() instanceof Role)) {
            return new ResponseDTO(Const.STATUS_ERROR, "保证金类型非法！", null);
        }

        HbzRechargeDTO hbzRechargeDTO = new HbzRechargeDTO();
        hbzRechargeDTO.setMoney(hbzRechargeMapDTO.getMoney());
        hbzRechargeDTO.setRole(hbzRechargeMapDTO.getRole());
        hbzRechargeDTO.setUser(hbzUserService.currentUser());
        hbzRechargeDTO.setState(HbzRechargeService.NEW);
        hbzRechargeDTO.setUserId(hbzUserService.currentUser().getId());
        hbzRechargeDTO.setExecuteDate(Instant.now().toEpochMilli());
        hbzRechargeDTO.setStatus(Const.STATUS_ENABLED);

        String chargeNo = hbzPayService.createTradeNo(BusinessType.BOND);
        hbzRechargeDTO.setChargeNo(chargeNo);

        HbzRechargeDTO hbzRechargeDtoFromDb = hbzRechargeService.save(hbzRechargeDTO);
        ResponseDTO responseDTO = null;
        if (hbzRechargeDtoFromDb != null) {
//            String code_url = webPayService.getPreparePayCodeAndUrl(hbzRechargeDtoFromDb.getChargeNo());
            //TODO 用生code_url成支付二维码
            responseDTO = new ResponseDTO(Const.STATUS_OK, "保存成功！", hbzRechargeDtoFromDb);
        } else {
            responseDTO = new ResponseDTO(Const.STATUS_ERROR, "保存失败！", null);
        }
        return responseDTO;
    }

    /**
     * 支付回调接口，用于支付成功后更新订单状态
     */
    public void payCallBack() {
        //TODO
    }

    /*public static void main(String[] args) {
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(new Date().getTime());
    }*/

}
