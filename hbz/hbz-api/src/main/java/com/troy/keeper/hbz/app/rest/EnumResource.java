package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.CommonConstants;
import com.troy.keeper.hbz.app.dto.EnumQueryDTO;
import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTypeValDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzTypeValService;
import com.troy.keeper.hbz.sys.Bean2Map;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/26.
 */
@CrossOrigin
@RestController
public class EnumResource {

    @Autowired
    HbzTypeValService hbzTypeValService;

    @Label("App端 - 公共 - 枚举")
    @RequestMapping(value = "/api/public/enums", method = RequestMethod.POST)
    public ResponseDTO queryEnum(@RequestBody EnumQueryDTO enumQuery) {
        Map<String, Object> kv = new LinkedHashMap<>();
        try {
            Class<?> enumClass = Class.forName("com.troy.keeper.hbz.type." + enumQuery.getEnumname());
            if (!enumClass.isEnum()) {
                return new ResponseDTO(Const.STATUS_ERROR, "传入的类型根无效");
            } else {
                Method valuesMethod = enumClass.getMethod("values");
                Object[] values = (Object[]) valuesMethod.invoke(null);
                for (Object value : values) {
                    String key = value.toString();
                    if(enumQuery.getExcludeType()==null || !enumQuery.getExcludeType().contains(key)){
                        Method getNameMethod = enumClass.getMethod("getName");
                        String name = (String) getNameMethod.invoke(value);
                        kv.put(key, name);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseDTO(Const.STATUS_ERROR, "传入的类型根无效");
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", kv);
    }

    /**
     * @param tv
     * @return
     */
    @Label("App端 - 数据字典查询")
    @RequestMapping(value = "/api/public/typeval", method = RequestMethod.POST)
    public ResponseDTO typeVal(@RequestBody HbzTypeValDTO tv) {
        String language = WebThreadHolder.getLanguage();
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzTypeValService.getByTypeAndLanguage(tv.getType(), language).stream().filter(t->tv.getExcludeValue() == null || !tv.getExcludeValue().contains(t.getVal())).map(this::mapType).collect(Collectors.toList()));
    }

    /**
     * @param tv
     * @return
     */
    @Label("App端 - 数据字典")
    @RequestMapping(value = "/api/public/gettype", method = RequestMethod.POST)
    public ResponseDTO typeVals(@RequestBody HbzTypeValDTO tv) {
        return new ResponseDTO(Const.STATUS_OK, "成功", mapType(hbzTypeValService.getByTypeAndValAndLanguage(tv.getType(), tv.getVal(), tv.getLanguage())));
    }

    private Map<String, Object> mapType(HbzTypeValDTO tv) {
        Map<String, Object> ret = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM")::format)
        ).addIgnores(CommonConstants.commonIgnores).map(tv);
        return ret;
    }

}
