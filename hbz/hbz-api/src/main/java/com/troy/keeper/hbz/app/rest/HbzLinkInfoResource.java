package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzLinkInfoDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzLinkInfoService;
import com.troy.keeper.hbz.service.MapService;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/30.
 */
@RestController
@RequestMapping("/api/user")
public class HbzLinkInfoResource {

    @Autowired
    private HbzLinkInfoService hbzLinkInfoService;
    @Autowired
    HbzAreaService hbzAreaService;
    @Autowired
    MapService map;

    @Label("App端 - 常用联系人 - 增加")
    @RequestMapping(value = {"/app/link/create"}, method = RequestMethod.POST)
    public ResponseDTO linkInfoCreate(@RequestBody HbzLinkInfoDTO linkParameter) {
        if (linkParameter.getId() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能指定ID");
        } else {
            String[] errors = ValidationHelper.valid(linkParameter, "link_info_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证不通过", errors);
            } else {
                HbzLinkInfoDTO link = new HbzLinkInfoDTO();
                BeanUtils.copyProperties(linkParameter, link, "user");
                link.setUserId(SecurityUtils.getCurrentUserId());
                if (link.getIndex() == null) {
                    link.setIndex(0);
                }
                if (link.getIdefault() == null) {
                    link.setIdefault(0);
                }
                if (link.getIdefault().equals(Integer.valueOf(1))) {
                    HbzLinkInfoDTO query = new HbzLinkInfoDTO();
                    query.setIdefault(1);
                    query.setUserId(SecurityUtils.getCurrentUserId());
                    query.setUsein(link.getUsein());
                    query.setStatus(Const.STATUS_ENABLED);
                    hbzLinkInfoService.query(query).stream().map(ds -> {
                        ds.setIdefault(0);
                        return ds;
                    }).forEach(hbzLinkInfoService::save);
                }
                if (StringHelper.notNullAndEmpty(linkParameter.getAreaCode())) {
                    HbzAreaDTO area = hbzAreaService.findByOutCode(linkParameter.getAreaCode());
                    if (area != null) {
                        link.setArea(area);
                        link.setAreaId(area.getId());
                    }
                } else {
                    HbzAreaDTO area = map.getAreaByLocation(linkParameter.getLng(), linkParameter.getLat());
                    if (area != null) {
                        link.setArea(area);
                        link.setAreaId(area.getId());
                    }
                }
                link.setStatus(Const.STATUS_ENABLED);
                link = hbzLinkInfoService.save(link);
                if (link != null) {
                    return new ResponseDTO(Const.STATUS_OK, "地址保存成功", null);
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "错误");
                }
            }
        }
    }

    @Label("App端 - 常用联系人 - 修改")
    @RequestMapping(value = {"/app/link/update"}, method = RequestMethod.POST)
    public ResponseDTO linkInfoUpdate(@RequestBody HbzLinkInfoDTO linkParameter) {
        if (linkParameter.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "ID为空");
        } else {
            String[] errors = ValidationHelper.valid(linkParameter, "link_info_update");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证不通过", errors);
            } else {
                HbzLinkInfoDTO link = hbzLinkInfoService.get(linkParameter);
                if (link == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "ID非法");
                }
                BeanUtils.copyProperties(linkParameter, link, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user"));
                link.setUserId(SecurityUtils.getCurrentUserId());
                if (link.getIndex() == null) {
                    link.setIndex(0);
                }
                if (link.getIdefault() == null) {
                    link.setIdefault(0);
                }
                if (link.getIdefault().equals(Integer.valueOf(1))) {
                    HbzLinkInfoDTO query = new HbzLinkInfoDTO();
                    query.setIdefault(1);
                    query.setUserId(SecurityUtils.getCurrentUserId());
                    query.setUsein(link.getUsein());
                    query.setStatus(Const.STATUS_ENABLED);
                    hbzLinkInfoService.query(query).stream().map(ds -> {
                        ds.setIdefault(0);
                        return ds;
                    }).forEach(hbzLinkInfoService::save);
                }
                if (StringHelper.notNullAndEmpty(linkParameter.getAreaCode())) {
                    HbzAreaDTO area = hbzAreaService.findByOutCode(linkParameter.getAreaCode());
                    if (area != null) {
                        link.setArea(area);
                        link.setAreaId(area.getId());
                    }
                } else {
                    HbzAreaDTO area = map.getAreaByLocation(linkParameter.getLng(), linkParameter.getLat());
                    if (area != null) {
                        link.setArea(area);
                        link.setAreaId(area.getId());
                    }
                }
                link.setStatus(Const.STATUS_ENABLED);
                link = hbzLinkInfoService.save(link);
                if (link != null) {
                    return new ResponseDTO(Const.STATUS_OK, "地址保存成功");
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "错误");
                }
            }
        }
    }

    @Label("App端 - 常用联系人 - 默认")
    @RequestMapping(value = {"/app/link/setDefault"}, method = RequestMethod.POST)
    public ResponseDTO linkInfodefault(@RequestBody HbzLinkInfoDTO linkParameter) {
        if (linkParameter.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证错误,id缺失");
        } else {
            HbzLinkInfoDTO link = hbzLinkInfoService.get(linkParameter);
            if (link == null) {
                return new ResponseDTO(Const.STATUS_ERROR, "ID非法");
            }

            HbzLinkInfoDTO query = new HbzLinkInfoDTO();
            query.setIdefault(1);
            query.setUserId(SecurityUtils.getCurrentUserId());
            query.setUsein(link.getUsein());
            query.setStatus(Const.STATUS_ENABLED);
            hbzLinkInfoService.query(query).stream().filter(i -> !i.getId().equals(linkParameter.getId())).map(ds -> {
                ds.setIdefault(0);
                return ds;
            }).forEach(hbzLinkInfoService::save);

            link.setIdefault(1);
            link.setStatus(Const.STATUS_ENABLED);
            link = hbzLinkInfoService.save(link);
            if (link != null) {
                return new ResponseDTO(Const.STATUS_OK, "地址保存成功");
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "错误");
            }
        }
    }


    @Label("App端 - 常用联系人 - 去除")
    @RequestMapping(value = {"/app/link/delete"}, method = RequestMethod.POST)
    public ResponseDTO linkInfoDelete(@RequestBody HbzLinkInfoDTO linkParameter) {
        if (linkParameter.getId() != null) {
            HbzLinkInfoDTO link = hbzLinkInfoService.get(linkParameter);
            if (link == null) {
                return new ResponseDTO(Const.STATUS_ERROR, "错误ID必须");
            } else {
                if (hbzLinkInfoService.delete(link)) {
                    return new ResponseDTO(Const.STATUS_OK, "删除成功");
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "删除失败");
                }
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "错误ID必须");
        }
    }

    @Label("App端 - 常用联系人 - 查询")
    @RequestMapping(value = {"/app/link/query"}, method = RequestMethod.POST)
    public ResponseDTO linkInfoQuery(@RequestBody HbzLinkInfoDTO linkParameter) {
        linkParameter.setUserId(SecurityUtils.getCurrentUserId());
        linkParameter.setStatus(Const.STATUS_ENABLED);
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"index", "desc"});
        linkParameter.setSorts(sorts);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzLinkInfoService.query(linkParameter).stream().map(MapSpec::mapLinkInfo).collect(Collectors.toList()));
    }

    @Label("App端 - 常用联系人 - 分布查询")
    @RequestMapping(value = {"/app/link/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO pageQuery(@RequestBody HbzLinkInfoDTO linkParameter) {
        linkParameter.setUserId(SecurityUtils.getCurrentUserId());
        linkParameter.setStatus(Const.STATUS_ENABLED);
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"index", "desc"});
        linkParameter.setSorts(sorts);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzLinkInfoService.queryPage(linkParameter, linkParameter.getPageRequest()).map(MapSpec::mapLinkInfo));
    }
}
