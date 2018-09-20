package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.app.constant.CommonConstants;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzUserAddressInfoDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzUserAddressInfoService;
import com.troy.keeper.hbz.sys.Bean2Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/7.
 */
@RestController
@RequestMapping("/api/user/app/address")
public class HbzUserAddressInfoResource {

    @Autowired
    private HbzUserAddressInfoService hbzUserAddressInfoService;

    @Autowired
    private HbzAreaService hbzAreaService;

    //常用地址创建
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseDTO userAddressCr(@RequestBody HbzUserAddressInfoDTO hbzUserAddressInfoDTO) {
        if (hbzUserAddressInfoDTO.getId() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能指定ID");
        } else {
            String[] errors = ValidationHelper.valid(hbzUserAddressInfoDTO, "user_add_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证不通过", errors);
            } else {
                HbzUserAddressInfoDTO hbzUserAddressInfo = new HbzUserAddressInfoDTO();
                BeanUtils.copyProperties(hbzUserAddressInfoDTO, hbzUserAddressInfo, "user", "area");
                if (hbzUserAddressInfoDTO.getAreaCode() != null) {
                    HbzAreaDTO hbzArea = hbzAreaService.findByOutCode(hbzUserAddressInfoDTO.getAreaCode());
                    if (hbzArea != null) {
                        hbzUserAddressInfo.setAreaId(hbzArea.getId());
                        hbzUserAddressInfo.setArea(hbzArea);
                    }
                }
                hbzUserAddressInfo.setUserId(SecurityUtils.getCurrentUserId());
                if (hbzUserAddressInfoDTO.getIndex() == null) {
                    hbzUserAddressInfo.setIndex(0);
                }
                if (hbzUserAddressInfo.getIdefault() == null) {
                    hbzUserAddressInfo.setIdefault(0);
                }
                if (hbzUserAddressInfo.getIdefault().intValue() == 1) {
                    HbzUserAddressInfoDTO query = new HbzUserAddressInfoDTO();
                    query.setUserId(SecurityUtils.getCurrentUserId());
                    query.setIdefault(1);
                    query.setType(hbzUserAddressInfo.getType());
                    query.setStatus(Const.STATUS_ENABLED);
                    List<HbzUserAddressInfoDTO> list = hbzUserAddressInfoService.query(query);
                    list.stream().map(i -> {
                        i.setIdefault(0);
                        return i;
                    }).forEach(hbzUserAddressInfoService::save);
                }
                hbzUserAddressInfo.setStatus(Const.STATUS_ENABLED);
                hbzUserAddressInfo = hbzUserAddressInfoService.save(hbzUserAddressInfo);
                if (hbzUserAddressInfo != null) {
                    hbzUserAddressInfo.getUser().setPassword("");
                    return new ResponseDTO(Const.STATUS_OK, "地址保存成功", Optional.of(hbzUserAddressInfo).map(this::addressInfoMap).get());
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "错误");
                }
            }
        }
    }

    //常用地址更新
    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public ResponseDTO userAddressUpdate(@RequestBody HbzUserAddressInfoDTO hbzUserAddressInfoDTO) {
        if (hbzUserAddressInfoDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "ID为空");
        } else {
            String[] errors = ValidationHelper.valid(hbzUserAddressInfoDTO, "user_add_update");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证不通过", errors);
            } else {
                HbzUserAddressInfoDTO hbzUserAddressInfo = hbzUserAddressInfoService.get(hbzUserAddressInfoDTO);
                if (hbzUserAddressInfo == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "ID非法");
                }
                BeanUtils.copyProperties(hbzUserAddressInfoDTO, hbzUserAddressInfo, StringHelper.conact(Const.ID_AUDIT_FIELDS, "area"));
                if (hbzUserAddressInfoDTO.getAreaCode() != null) {
                    HbzAreaDTO hbzArea = hbzAreaService.findByOutCode(hbzUserAddressInfoDTO.getAreaCode());
                    if (hbzArea != null) {
                        hbzUserAddressInfo.setAreaId(hbzArea.getId());
                        hbzUserAddressInfo.setArea(hbzArea);
                    }
                }
                hbzUserAddressInfo.setUserId(SecurityUtils.getCurrentUserId());
                if (hbzUserAddressInfoDTO.getIndex() == null) {
                    hbzUserAddressInfo.setIndex(0);
                }
                if (hbzUserAddressInfo.getIdefault() == null) {
                    hbzUserAddressInfo.setIdefault(0);
                }
                Long id = hbzUserAddressInfo.getId();
                if (hbzUserAddressInfo.getIdefault().intValue() == 1) {
                    HbzUserAddressInfoDTO query = new HbzUserAddressInfoDTO();
                    query.setUserId(SecurityUtils.getCurrentUserId());
                    query.setIdefault(1);
                    query.setType(hbzUserAddressInfo.getType());
                    query.setStatus(Const.STATUS_ENABLED);
                    List<HbzUserAddressInfoDTO> list = hbzUserAddressInfoService.query(query);
                    list.stream().filter(i -> !i.getId().equals(id)).map(i -> {
                        i.setIdefault(0);
                        return i;
                    }).forEach(hbzUserAddressInfoService::save);
                }
                hbzUserAddressInfo.setStatus(Const.STATUS_ENABLED);
                hbzUserAddressInfo = hbzUserAddressInfoService.save(hbzUserAddressInfo);
                if (hbzUserAddressInfo != null) {
                    return new ResponseDTO(Const.STATUS_OK, "地址保存成功", Optional.of(hbzUserAddressInfo).map(this::addressInfoMap).get());
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "错误");
                }
            }
        }
    }

    //设置某个地址为默认地址
    @RequestMapping(value = {"/setDefault"}, method = RequestMethod.POST)
    public ResponseDTO userAddressDefault(@RequestBody HbzUserAddressInfoDTO hbzUserAddressInfoDTO) {
        if (hbzUserAddressInfoDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "ID为空");
        } else {
            HbzUserAddressInfoDTO hbzUserAddressInfo = hbzUserAddressInfoService.get(hbzUserAddressInfoDTO);
            if (hbzUserAddressInfo == null) {
                return new ResponseDTO(Const.STATUS_ERROR, "ID非法");
            }

            HbzUserAddressInfoDTO query = new HbzUserAddressInfoDTO();
            query.setUserId(SecurityUtils.getCurrentUserId());
            query.setIdefault(1);
            query.setType(hbzUserAddressInfo.getType());
            query.setStatus(Const.STATUS_ENABLED);

            List<HbzUserAddressInfoDTO> list = hbzUserAddressInfoService.query(query);
            list.stream().filter(i -> !i.getId().equals(hbzUserAddressInfoDTO.getId())).map(i -> {
                i.setIdefault(0);
                return i;
            }).forEach(hbzUserAddressInfoService::save);

            hbzUserAddressInfo.setIdefault(1);
            hbzUserAddressInfo.setStatus(Const.STATUS_ENABLED);
            hbzUserAddressInfo = hbzUserAddressInfoService.save(hbzUserAddressInfo);
            if (hbzUserAddressInfo != null) {
                return new ResponseDTO(Const.STATUS_OK, "地址保存成功", Optional.of(hbzUserAddressInfo).map(this::addressInfoMap).get());
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "错误");
            }
        }
    }


    //删除常用地址
    @RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    public ResponseDTO userAddressDelete(@RequestBody HbzUserAddressInfoDTO hbzUserAddressInfoDTO) {
        if (hbzUserAddressInfoDTO.getId() != null) {
            HbzUserAddressInfoDTO hbzUserAddressInfo = hbzUserAddressInfoService.get(hbzUserAddressInfoDTO);
            if (hbzUserAddressInfo == null) {
                return new ResponseDTO(Const.STATUS_ERROR, "错误ID必须");
            } else {
                if (hbzUserAddressInfoService.delete(hbzUserAddressInfo)) {
                    return new ResponseDTO(Const.STATUS_OK, "删除成功", hbzUserAddressInfo.getId());
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "删除失败", hbzUserAddressInfo.getId());
                }
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "错误ID必须");
        }
    }

    //查询地址
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO userAddressQuery(@RequestBody HbzUserAddressInfoDTO hbzUserAddressInfoDTO) {
        hbzUserAddressInfoDTO.setUserId(SecurityUtils.getCurrentUserId());
        hbzUserAddressInfoDTO.setStatus(Const.STATUS_ENABLED);
        List sorts = new ArrayList<String[]>();
        sorts.add(new String[]{"idefault", "desc"});
        sorts.add(new String[]{"index", "desc"});
        hbzUserAddressInfoDTO.setSorts(sorts);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzUserAddressInfoService.query(hbzUserAddressInfoDTO).stream().map(this::addressInfoMap).collect(Collectors.toList()));
    }

    //查询地址分页
    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO userAddressQueryPage(@RequestBody HbzUserAddressInfoDTO hbzUserAddressInfoDTO) {
        hbzUserAddressInfoDTO.setUserId(SecurityUtils.getCurrentUserId());
        hbzUserAddressInfoDTO.setStatus(Const.STATUS_ENABLED);
        List sorts = new ArrayList<String[]>();
        sorts.add(new String[]{"index", "desc"});
        sorts.add(new String[]{"idefault", "desc"});
        hbzUserAddressInfoDTO.setSorts(sorts);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzUserAddressInfoService.queryPage(hbzUserAddressInfoDTO, hbzUserAddressInfoDTO.getPageRequest()).map(this::addressInfoMap));
    }

    private Map<String, Object> addressInfoMap(HbzUserAddressInfoDTO addressInfoDTO) {
        return new Bean2Map(new PropertyMapper<HbzAreaDTO, Map<String, Object>>("area", (area) -> {
            Map<String, Object> areaData = new Bean2Map().addIgnores("parent").addIgnores(CommonConstants.commonIgnores).map(area);
            Integer level = area.getLevel();
            HbzAreaDTO parent = area;
            while (level != 0) {
                areaData.put("Level" + level + "Code", parent.getAreaCode());
                areaData.put("Level" + level + "AreaCode", parent.getOutCode());
                areaData.put("Level" + level + "AreaName", parent.getAreaName());
                parent = parent.getParent();
                level--;
            }
            return areaData;
        })).addIgnores("user").addIgnores(CommonConstants.commonIgnores).map(addressInfoDTO);
    }
}
