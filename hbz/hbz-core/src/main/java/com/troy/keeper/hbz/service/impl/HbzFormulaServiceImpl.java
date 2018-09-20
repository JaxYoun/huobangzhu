package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzFormulaDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzFormula;
import com.troy.keeper.hbz.repository.HbzFormulaRepository;
import com.troy.keeper.hbz.service.HbzFormulaService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzFormulaMapper;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.transaction.Transactional;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by leecheng on 2017/12/4.
 */
@Service
@Transactional
public class HbzFormulaServiceImpl extends BaseEntityServiceImpl<HbzFormula, HbzFormulaDTO> implements HbzFormulaService {

    private ScriptEngine scriptEngine;

    @Autowired
    private HbzFormulaRepository r;

    @Autowired
    private HbzFormulaMapper m;

    @SneakyThrows
    public ScriptEngine getScriptEngine() {
        if (scriptEngine != null) return scriptEngine;
        @Cleanup InputStreamReader isr = new InputStreamReader(HbzFormulaService.class.getResourceAsStream("/jsfiles/hbzFormula.js"));
        ScriptEngine js = new ScriptEngineManager().getEngineByName("js");
        js.eval(isr);
        scriptEngine = js;
        return scriptEngine;
    }

    @Override
    public BaseMapper getMapper() {
        return m;
    }

    @Override
    public BaseRepository getRepository() {
        return r;
    }

    @SneakyThrows
    public Double calculate(String formulaKey, Map<String, Double> variables) {
        ScriptEngine js = getScriptEngine();
        if (variables != null) {
            variables.entrySet().forEach(entry -> {
                js.put(entry.getKey(), entry.getValue());
            });
        }
        HbzFormulaDTO formula = m.map(r.findByFormulaKey(formulaKey));
        if (formula == null) throw new IllegalStateException("公式[" + formulaKey + "]不可用");
        String fx = formula.getFormula();
        Object r = js.eval(fx);
        if (r instanceof Double)
            return (Double) r;
        if (r instanceof Integer)
            return Double.valueOf((Integer) r);
        if (r instanceof Float)
            return Double.valueOf((Float) r);
        if (r instanceof Long)
            return Double.valueOf((Long) r);
        throw new IllegalStateException("无法计算");
    }

    @Override
    public String createNo() {
        String no;
        int idx = 0;
        while (true) {
            no = StringHelper.frontCompWithZero(++idx, 6);
            HbzFormulaDTO q = new HbzFormulaDTO();
            q.setFormulaNo(no);
            if (count(q) < 1) return no;
        }
    }

}
