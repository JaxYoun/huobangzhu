package com.troy.keeper.management.web.rest.system;

import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzRefundDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzRefund;
import com.troy.keeper.hbz.service.AlipayService;
import com.troy.keeper.hbz.service.HbzPayService;
import com.troy.keeper.hbz.service.HbzRefundService;
import com.troy.keeper.hbz.service.WechatService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.RefundStatus;
import com.troy.keeper.management.dto.HbzRefundMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/3/6.
 */
@RestController
@RequestMapping({"/api/refund"})
public class RefundResource {

    @Autowired
    HbzRefundService hbzRefundService;

    @Autowired
    AlipayService alipayService;

    @Autowired
    WechatService wechatService;

    @Autowired
    HbzPayService hbzpayService;

    @Label("管理端 - 退款 - 创建")
    @RequestMapping({"/create"})
    public ResponseDTO create(@RequestBody HbzRefundMapDTO hbzRefundDTO) {
        String[] errs = ValidationHelper.valid(hbzRefundDTO, "create");
        if (errs.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "参数错误", errs);

        HbzPayDTO pay = hbzpayService.findById(hbzRefundDTO.getPayId());

        HbzRefundDTO refundQuery = new HbzRefundDTO();
        refundQuery.setStatus("1");
        refundQuery.setRefundStatuses(Arrays.asList(RefundStatus.SUCCESS, RefundStatus.SUBMIT, RefundStatus.REFUNDING, RefundStatus.NEW));
        refundQuery.setPayId(hbzRefundDTO.getPayId());
        refundQuery.setTradeNo(pay.getTradeNo());
        Long count = hbzRefundService.count(refundQuery);
        if (count > 0L) {
            return new ResponseDTO(Const.STATUS_ERROR, "已经存在该退款单");
        }

        HbzRefundDTO hbzRefund = new HbzRefundDTO();
        new Bean2Bean(
                new PropertyMapper<>("createTime", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLT", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzRefundDTO, hbzRefund);
        String refundNo = hbzRefundService.createRefundNo();
        String requestNo = hbzRefundService.createReqNo();
        hbzRefund.setCreateTime(Clock.systemUTC().millis());
        hbzRefund.setStatus("1");
        hbzRefund.setBill(0);
        hbzRefund.setRefundNo(refundNo);
        hbzRefund.setRequestNo(requestNo);
        hbzRefund = hbzRefundService.save(hbzRefund);
        return new ResponseDTO(Const.STATUS_OK, "", MapSpec.mapRefund(hbzRefund));
    }

    @Label("管理端 - 退款 - 向支付机构发起退款")
    @RequestMapping({"/post"})
    public ResponseDTO submit(@RequestBody HbzRefundMapDTO hbzRefundDTO) {
        String[] errs = ValidationHelper.valid(hbzRefundDTO, "post");
        if (errs.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "参数错误", errs);
        HbzRefundDTO hbzRefund = hbzRefundService.findById(hbzRefundDTO.getId());
        if (Arrays.asList(RefundStatus.REFUNDING, RefundStatus.SUCCESS, RefundStatus.FAILURE).contains(hbzRefund.getRefundStatus())) {
            return new ResponseDTO(Const.STATUS_ERROR, "只能对新建的退款单发起退款");
        }
        int tx = 0;
        hbzRefundService.refundWith(hbzRefund);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", tx);
    }

    @Label("管理端 - 退款 - 查询")
    @RequestMapping({"/query"})
    public ResponseDTO query(@RequestBody HbzRefundMapDTO hbzRefundDTO) {
        HbzRefundDTO query = new HbzRefundDTO();
        new Bean2Bean(
                new PropertyMapper<>("createTime", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLT", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzRefundDTO, query);
        if (query.getCreateTimeLE() != null) query.setCreateTimeLE(query.getCreateTimeLE() + 24L * 60L * 60L * 1000L);
        if (query.getCreateTimeLT() != null) query.setCreateTimeLT(query.getCreateTimeLT() + 24L * 3600L * 1000L);
        query.setStatus("1");
        List<HbzRefundDTO> result = hbzRefundService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", result.stream().map(MapSpec::mapRefund).collect(Collectors.toList()));
    }

    @Label("管理端 - 退款 - 查询 - 分布")
    @RequestMapping({"/queryPage"})
    public ResponseDTO pageQuery(@RequestBody HbzRefundMapDTO hbzRefundDTO) {
        HbzRefundDTO query = new HbzRefundDTO();
        new Bean2Bean(
                new PropertyMapper<>("createTime", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGT", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeGE", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("createTimeLT", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzRefundDTO, query);
        if (query.getCreateTimeLE() != null) query.setCreateTimeLE(query.getCreateTimeLE() + 24L * 60L * 60L * 1000L);
        if (query.getCreateTimeLT() != null) query.setCreateTimeLT(query.getCreateTimeLT() + 24L * 3600L * 1000L);
        query.setStatus("1");
        Page<HbzRefundDTO> result = hbzRefundService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", result.map(MapSpec::mapRefund));
    }
}
