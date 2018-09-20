package com.troy.keeper.hbz.rest.debug;

import com.troy.keeper.hbz.service.CommonService;
import com.troy.keeper.hbz.sys.SysCore;
import com.troy.keeper.hbz.sys.annotation.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;

/**
 * Created by leecheng on 2017/10/16.
 */
@Controller
public class DebugResource {

    @Autowired
    private SysCore sysCore;

    @Autowired
    private CommonService commonService;

    @Event
    @ResponseBody
    @RequestMapping("/debug")
    public String debug(@RequestBody String debug) throws Exception {
        StringWriter out = new StringWriter();

        ScriptEngine script = new ScriptEngineManager().getEngineFactories().get(0).getScriptEngine();
        script.put("sysCore", sysCore);
        script.put("out", out);

        commonService.txDo((obj) -> {
            try {
                script.eval(debug);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }, null);

        return out.toString();
    }
}
