package com.troy.keeper.hbz.app.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.service.HbzFormulaService;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/12/4.
 */
@RestController
@RequestMapping(value = {"/api/math"})
public class HbzFormulaResource {

    @Autowired
    private HbzFormulaService hbzFormulaService;

    @Label("App端 - 公式 - 使用公式计算")
    @RequestMapping(value = "/calc", method = RequestMethod.POST)
    public ResponseDTO calcByFormula(@RequestBody String request) {
        JsonObject object = new Gson().fromJson(request, JsonObject.class);
        String formulaKey = object.get("formulaKey").getAsString();
        JsonObject variables = object.getAsJsonObject("variables");
        Map<String, Double> vartable = new LinkedHashMap<>();
        variables.entrySet().forEach(stringJsonElementEntry -> {
            vartable.put(stringJsonElementEntry.getKey(), variables.get(stringJsonElementEntry.getKey()).getAsDouble());
        });
        Double v = hbzFormulaService.calculate(formulaKey, vartable);
        return new ResponseDTO(Const.STATUS_OK, "计算完成", v);
    }

}
