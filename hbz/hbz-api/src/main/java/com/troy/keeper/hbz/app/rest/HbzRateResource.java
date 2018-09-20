package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzRateMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCreditRecordDTO;
import com.troy.keeper.hbz.dto.HbzRateDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzCreditRecordService;
import com.troy.keeper.hbz.service.HbzFormulaService;
import com.troy.keeper.hbz.service.HbzRateService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/28.
 */
@RestController
@RequestMapping("/api/rate")
public class HbzRateResource {

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzRateService hbzRateService;

    @Autowired
    HbzFormulaService hbzFormulaService;

    @Autowired
    HbzCreditRecordService hbzCreditRecordService;

    //TODO 评分任务需要处理

    /**
     * 查询评分
     *
     * @param hbzRateMapDTO
     * @return
     */
    @RequestMapping("/query")
    public ResponseDTO list(@RequestBody HbzRateMapDTO hbzRateMapDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzRateDTO rateQuery = new HbzRateDTO();
        new Bean2Bean().copyProperties(hbzRateMapDTO, rateQuery);
        rateQuery.setUserId(user.getId());
        rateQuery.setStatus("1");
        List<HbzRateDTO> rates = hbzRateService.query(rateQuery);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", rates.stream().map(MapSpec::mapRate).collect(Collectors.toList()));
    }

    @RequestMapping("/queryPage")
    public ResponseDTO page(@RequestBody HbzRateMapDTO hbzRateMapDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzRateDTO rateQuery = new HbzRateDTO();
        new Bean2Bean().copyProperties(hbzRateMapDTO, rateQuery);
        rateQuery.setUserId(user.getId());
        rateQuery.setStatus("1");
        Page<HbzRateDTO> page = hbzRateService.queryPage(rateQuery, rateQuery.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(MapSpec::mapRate));
    }

    @RequestMapping("/count")
    public ResponseDTO countX(@RequestBody HbzRateMapDTO hbzRateMapDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzRateDTO rateQuery = new HbzRateDTO();
        new Bean2Bean().copyProperties(hbzRateMapDTO, rateQuery);
        rateQuery.setUserId(user.getId());
        rateQuery.setStatus("1");
        Long count = hbzRateService.count(rateQuery);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", count);
    }

    private final static String PROV_FORMULA = "CALC_PROV";
    private final static String CONS_FORMULA = "CALC_CONS";

    @RequestMapping("/rateSign")
    public ResponseDTO sign(@RequestBody HbzRateMapDTO hbzRateMapDTO) {

        HbzUserDTO user = hbzUserService.currentUser();

        if (hbzRateMapDTO.getId() == null)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "错误", Arrays.asList("ID为空"));
        if (hbzRateMapDTO.getStar() == null || hbzRateMapDTO.getStar() < 1)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, " 错误", Arrays.asList("评星为空"));

        HbzRateDTO rate = hbzRateService.findById(hbzRateMapDTO.getId());
        if (rate != null) {
            rate.setComment(hbzRateMapDTO.getComment());
            rate.setStar(hbzRateMapDTO.getStar());
            hbzRateService.save(rate);
            Double star = Double.valueOf(rate.getStar());
            Map<String, Double> var = new LinkedMap();
            var.put("s", star);
            if (rate.getUser().getId().equals(user.getId())) {
                switch (rate.getType()) {
                    case PROVIDER: {
                        //我的身份是货运方
                        HbzUserDTO tar = rate.getOrder().getCreateUser();
                        if (tar.getUserScore() == null) tar.setUserScore(0);
                        try {
                            int score = (int) hbzFormulaService.calculate(PROV_FORMULA, var).doubleValue();
                            tar.setUserScore(tar.getUserScore() + score);
                            hbzUserService.save(tar);

                            //增加货主端积分变化记录
                            HbzCreditRecordDTO creditRecordDTO = new HbzCreditRecordDTO();
                            creditRecordDTO.setUser(rate.getOrder().getCreateUser());
                            creditRecordDTO.setAction(Const.CREDIT_SCORE_ADJUEST_ACTION_ALL);
                            creditRecordDTO.setAdjustType(Const.CREDIT_SCORE_ADJUEST_TYPE_ALL);
                            creditRecordDTO.setDelta(score);
                            creditRecordDTO.setMsg("评论订单[" + rate.getOrder().getOrderNo() + "]");
                            creditRecordDTO.setTime(System.currentTimeMillis());
                            creditRecordDTO.setRecNo(hbzCreditRecordService.createNo());
                            creditRecordDTO.setType(Const.CREDIT_SCORE_TYPE_CONS);
                            creditRecordDTO.setTime(System.currentTimeMillis());
                            creditRecordDTO.setStatus("1");
                            hbzCreditRecordService.save(creditRecordDTO);

                        } catch (Exception e) {
                            return new ResponseDTO(Const.STATUS_ERROR, "计算错误", e.getMessage());
                        }
                        return new ResponseDTO(Const.STATUS_OK, "操作成功");
                    }
                    case CONSIGNOR: {
                        //我的身份是货主
                        HbzUserDTO tar = rate.getOrder().getTakeUser();
                        if (tar.getScore() == null) tar.setScore(0);
                        try {
                            int score = (int) hbzFormulaService.calculate(CONS_FORMULA, var).doubleValue();
                            tar.setScore(tar.getScore() + score);
                            hbzUserService.save(tar);

                            //增加车主端积分记录
                            HbzCreditRecordDTO creditRecordDTO = new HbzCreditRecordDTO();
                            creditRecordDTO.setUser(rate.getOrder().getTakeUser());
                            creditRecordDTO.setAction(Const.CREDIT_SCORE_ADJUEST_ACTION_ALL);
                            creditRecordDTO.setAdjustType(Const.CREDIT_SCORE_ADJUEST_TYPE_ALL);
                            creditRecordDTO.setDelta(score);
                            creditRecordDTO.setMsg("评论订单[" + rate.getOrder().getOrderNo() + "]");
                            creditRecordDTO.setTime(System.currentTimeMillis());
                            creditRecordDTO.setRecNo(hbzCreditRecordService.createNo());
                            creditRecordDTO.setType(Const.CREDIT_SCORE_TYPE_PROV);
                            creditRecordDTO.setTime(System.currentTimeMillis());
                            creditRecordDTO.setStatus("1");
                            hbzCreditRecordService.save(creditRecordDTO);

                        } catch (Exception e) {
                            return new ResponseDTO(Const.STATUS_ERROR, "计算错误", e.getMessage());
                        }
                        return new ResponseDTO(Const.STATUS_OK, "操作成功");
                    }
                }
            }
            return new ResponseDTO(Const.STATUS_ERROR, "错误的状态");
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "非法的状态");
        }
    }
}
