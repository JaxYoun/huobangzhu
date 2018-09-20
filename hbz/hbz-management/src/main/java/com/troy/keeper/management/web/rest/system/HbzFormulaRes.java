package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzFormulaDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzFormulaService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
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
 * Created by leecheng on 2018/1/18.
 */
@RestController
@RequestMapping("/api/formula")
public class HbzFormulaRes {

    @Autowired
    HbzFormulaService hbzFormulaService;

    @Label("管理端 - 公式 -添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzFormulaDTO formulaDTO) {
        String[] err = ValidationHelper.valid(formulaDTO, "formula_add");
        if (err.length > 0) return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        String formulaNo = hbzFormulaService.createNo();
        HbzFormulaDTO formula = new HbzFormulaDTO();
        new Bean2Bean().copyProperties(formulaDTO, formula);
        formula.setFormulaNo(formulaNo);
        formula.setStatus("1");
        formula = hbzFormulaService.save(formula);
        return new ResponseDTO(Const.STATUS_OK, "操作OK", MapSpec.mapFormula(formula));
    }

    @Label("管理端 - 公式 - 编辑")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO update(@RequestBody HbzFormulaDTO formulaDTO) {
        HbzFormulaDTO formula = hbzFormulaService.findById(formulaDTO.getId());
        new Bean2Bean().copyProperties(formulaDTO, formula, true);
        formula.setStatus("1");
        formula = hbzFormulaService.save(formula);
        return new ResponseDTO(Const.STATUS_OK, "操作OK", MapSpec.mapFormula(formula));
    }

    @Label("管理端 - 公式 - 删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzFormulaDTO formulaDTO) {
        HbzFormulaDTO formula = hbzFormulaService.findById(formulaDTO.getId());
        hbzFormulaService.delete(formula);
        return new ResponseDTO(Const.STATUS_OK, "操作OK");
    }

    @Label("管理端 - 公式 - 查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzFormulaDTO formulaDTO) {
        HbzFormulaDTO formula = new HbzFormulaDTO();
        formula.setStatus("1");
        new Bean2Bean().copyProperties(formulaDTO, formula, true);
        List<HbzFormulaDTO> list = hbzFormulaService.query(formula);
        return new ResponseDTO(Const.STATUS_OK, "操作OK", list.stream().map(MapSpec::mapFormula).collect(Collectors.toList()));
    }

    @Label("管理端 - 公式 - 查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO querypqaaa(@RequestBody HbzFormulaDTO formulaDTO) {
        HbzFormulaDTO formula = new HbzFormulaDTO();
        formula.setStatus("1");
        new Bean2Bean().copyProperties(formulaDTO, formula, true);
        Page<HbzFormulaDTO> page = hbzFormulaService.queryPage(formula, formula.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作OK", page.map(MapSpec::mapFormula));
    }
}
