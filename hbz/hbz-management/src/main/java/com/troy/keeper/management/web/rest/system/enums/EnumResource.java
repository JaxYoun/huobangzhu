package com.troy.keeper.management.web.rest.system.enums;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTypeValDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzTypeValService;
import com.troy.keeper.hbz.sys.Bean2Map;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.management.dto.EnumQueryDTO;
import com.troy.keeper.management.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2017/12/12.
 */
@RestController("enumQueryResource")
public class EnumResource {

    @Autowired
    HbzTypeValService hbzTypeValService;



    @RequestMapping(value = "/api/manager/enums", method = RequestMethod.POST)
    public ResponseDTO queryEnum(@RequestBody EnumQueryDTO enumQuery) {
        Map<String, Object> kv = new LinkedHashMap<>();
        try {
            Class<?> enumClass = Class.forName("com.troy.keeper.hbz.type." + enumQuery.getEnumname());
            if (!enumClass.isEnum()) {
                return new ResponseDTO(Const.STATUS_ERROR, "失败");
            } else {
                Method valuesMethod = enumClass.getMethod("values");
                Object[] values = (Object[]) valuesMethod.invoke(null);
                for (Object value : values) {
                    String key = value.toString();
                    if(enumQuery.getExcludeType()==null || !enumQuery.getExcludeType().contains(key)) {
                        Method getNameMethod = enumClass.getMethod("getName");
                        String name = (String) getNameMethod.invoke(value);
                        kv.put(key, name);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseDTO(Const.STATUS_ERROR, "失败");
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", kv);
    }


    /**
     * @param tv
     * @return
     */
    @RequestMapping(value = "/api/manager/typeval", method = RequestMethod.POST)
    public ResponseDTO typeVal(@RequestBody HbzTypeValDTO tv) {
        String language = "zh_CN";
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzTypeValService.getByTypeAndLanguage(tv.getType(), language).stream().filter(t->tv.getExcludeValue() == null || !tv.getExcludeValue().contains(t.getVal())).map(this::mapType).collect(Collectors.toList()));
    }

    /**
     * @param tv
     * @return
     */
    @RequestMapping(value = "/api/manager/gettype", method = RequestMethod.POST)
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
