package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.BondDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzBondGradeDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzBondGradeService;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/25.
 */
@CommonsLog
@RestController
@RequestMapping("/api/bond")
public class HbzBondResource {

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzBondGradeService hbzBondGradeService;

    //用户充值 - 保证金
    @Label("App端 - 保证金 - 创建")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseDTO pay(@RequestBody BondDTO bondDTO) {

        HbzUserDTO user = hbzUserService.currentUser();
        String[] er = ValidationHelper.valid(bondDTO, "bond_x");
        if (er.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", er);

        HbzBondGradeDTO gradeQuery = new HbzBondGradeDTO();
        gradeQuery.setBondType(bondDTO.getBondType());
        gradeQuery.setGrade(bondDTO.getGrade());
        gradeQuery.setStatus("1");
        List<HbzBondGradeDTO> grades = hbzBondGradeService.query(gradeQuery);
        if (grades == null || grades.size() != 1) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误的保证金项目!");
        }
        HbzBondGradeDTO grade = grades.get(0);
        HbzBondDTO bond = new HbzBondDTO();
        bond.setBondStatus(0);
        bond.setStatus("1");
        bond.setBondNo(hbzBondService.createBondNo());
        bond.setAmount(grade.getTotal());
        bond.setUser(user);
        bond.setUserId(user.getId());
        bond.setBondGrade(grade);
        bond.setBondGradeId(grade.getId());
        bond = hbzBondService.save(bond);
        return new ResponseDTO(Const.STATUS_OK, "创建订单号成功", bond.getBondNo());
    }

    @Label("App端 - 保证金级别 - 列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseDTO list() {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzBondGradeDTO gradeQuery = new HbzBondGradeDTO();
        gradeQuery.setStatus("1");
        List<HbzBondGradeDTO> grades = hbzBondGradeService.query(gradeQuery);
        List<Map<String, Object>> list = new LinkedList<>();
        for (HbzBondGradeDTO grade : grades) {
            Map<String, Object> queryBond = new LinkedHashMap<>();
            queryBond.put("bondGrade.bondType", grade.getBondType());
            queryBond.put("bondGrade.grade", grade.getGrade());
            queryBond.put("status", "1");
            queryBond.put("user.id", user.getId());
            queryBond.put("bondStatus in", Arrays.asList(1, 2));
            Long cnt = hbzBondService.count(queryBond);
            Map<String, Object> g = MapSpec.mapBondGrade(grade);
            g.put("totalCount", cnt);
            list.add(g);
        }
        return new ResponseDTO(Const.STATUS_OK, "", list);
    }

    @Label("App端 - 保证金 - 详情")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseDTO get(@RequestBody HbzBondDTO bond) {
        HbzBondDTO query = new HbzBondDTO();
        query.setId(bond.getId());
        HbzBondDTO dto = hbzBondService.get(query);
        return new ResponseDTO(Const.STATUS_OK, "", MapSpec.mapBond(dto));
    }

    @Label("App端 - 保证金 - 查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzBondDTO bond) {
        HbzBondDTO query = new HbzBondDTO();
        new Bean2Bean().copyProperties(bond, query);
        query.setUserId(hbzUserService.currentUser().getId());
        query.setStatus("1");
        List<HbzBondDTO> list = hbzBondService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "", list.stream().map(MapSpec::mapBond).collect(Collectors.toList()));
    }

    @Label("App端 - 保证金 - 分页 - 查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzBondDTO bond) {
        HbzBondDTO query = new HbzBondDTO();
        new Bean2Bean().copyProperties(bond, query);
        query.setUserId(SecurityUtils.getCurrentUserId());
        query.setStatus("1");
        Page<HbzBondDTO> page = hbzBondService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "", page.map(MapSpec::mapBond));
    }

}
