package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzRefundMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzRefundDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.HbzRefundService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.RefundStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/3/14.
 */
@RestController
@RequestMapping("/api/refund")
public class HbzRefundResource {

    @Autowired
    HbzPayService hbzPayService;

    @Autowired
    HbzRefundService hbzRefundService;

    @Autowired
    HbzUserService hbzUserService;

    @Label("App、Web - 退款请求")
    @RequestMapping("/submit")
    public ResponseDTO submit(@RequestBody RefundRequestWrapper refundRequestWrapper) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzPayDTO query = new HbzPayDTO();
        query.setStatus("1");
        query.setBusinessNo(refundRequestWrapper.getBusinessNo());
        query.setBusinessType(refundRequestWrapper.getBusinessType());
        query.setPayProgresses(Arrays.asList(PayProgress.SUCCESS));
        List<HbzPayDTO> pays = hbzPayService.query(query);

        if (pays != null && pays.size() == 1) {
            HbzPayDTO pay = pays.get(0);
            HbzRefundDTO refundQuery = new HbzRefundDTO();
            refundQuery.setStatus("1");
            refundQuery.setRefundStatuses(Arrays.asList(RefundStatus.SUCCESS, RefundStatus.SUBMIT, RefundStatus.REFUNDING, RefundStatus.NEW));
            refundQuery.setPayId(pay.getId());
            refundQuery.setTradeNo(pay.getTradeNo());
            Long count = hbzRefundService.count(refundQuery);
            if (count > 0L) {
                return new ResponseDTO(Const.STATUS_ERROR, "已经存在该退款单");
            }

            HbzRefundDTO hbzRefund = new HbzRefundDTO();
            hbzRefund.setUserId(user.getId());
            String refundNo = hbzRefundService.createRefundNo();
            String requestNo = hbzRefundService.createReqNo();
            hbzRefund.setCreateTime(Clock.systemUTC().millis());
            hbzRefund.setPayId(pay.getId());
            hbzRefund.setRefundStatus(RefundStatus.SUBMIT);
            hbzRefund.setCreateTime(System.currentTimeMillis());
            hbzRefund.setStatus("1");
            hbzRefund.setBill(0);
            hbzRefund.setRefundNo(refundNo);
            hbzRefund.setRequestNo(requestNo);
            hbzRefund = hbzRefundService.save(hbzRefund);
            hbzRefundService.refundWith(hbzRefund);
            return new ResponseDTO(Const.STATUS_OK, "", MapSpec.mapRefund(hbzRefund));
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "异常订单，请联系客服进行处理");
        }

    }


    @Label("App、Web - 退款 - 查询")
    @RequestMapping({"/query"})
    public ResponseDTO query(@RequestBody HbzRefundMapDTO hbzRefundDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzRefundDTO query = new HbzRefundDTO();
        new Bean2Bean(
                new PropertyMapper<>("createTime", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLT", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzRefundDTO, query);
        query.setUserId(user.getId());
        if (query.getCreateTimeLE() != null) query.setCreateTimeLE(query.getCreateTimeLE() + 24L * 60L * 60L * 1000L);
        if (query.getCreateTimeLT() != null) query.setCreateTimeLT(query.getCreateTimeLT() + 24L * 3600L * 1000L);
        query.setStatus("1");
        List<HbzRefundDTO> result = hbzRefundService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", result.stream().map(MapSpec::mapRefund).collect(Collectors.toList()));
    }

    @Label("管理端 - 退款 - 查询 - 分布")
    @RequestMapping({"/queryPage"})
    public ResponseDTO pageQuery(@RequestBody HbzRefundMapDTO hbzRefundDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzRefundDTO query = new HbzRefundDTO();
        new Bean2Bean(
                new PropertyMapper<>("createTime", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLT", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzRefundDTO, query);
        query.setUserId(user.getId());
        if (query.getCreateTimeLE() != null) query.setCreateTimeLE(query.getCreateTimeLE() + 24L * 60L * 60L * 1000L);
        if (query.getCreateTimeLT() != null) query.setCreateTimeLT(query.getCreateTimeLT() + 24L * 3600L * 1000L);
        query.setStatus("1");
        Page<HbzRefundDTO> result = hbzRefundService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", result.map(MapSpec::mapRefund));
    }
}

@Data
class RefundRequestWrapper {

    private String businessNo;
    private BusinessType businessType;

}