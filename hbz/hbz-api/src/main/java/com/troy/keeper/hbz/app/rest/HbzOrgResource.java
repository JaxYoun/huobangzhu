package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzOrgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * Created by leecheng on 2017/10/30.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/user")
public class HbzOrgResource {

    @Autowired
    private HbzOrgService hbzOrgService;

    //常用地址创建
    @RequestMapping(value = {"/app/org/create"}, method = RequestMethod.POST)
    public ResponseDTO orgCreate(@RequestBody HbzOrgDTO hbzOrgDTO) {
        if (hbzOrgDTO.getId() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能指定ID");
        } else {
            String[] errors = ValidationHelper.valid(hbzOrgDTO, "create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证不通过", errors);
            } else {
                HbzOrgDTO hbzOrg = new HbzOrgDTO();
                BeanUtils.copyProperties(hbzOrgDTO, hbzOrg);
                hbzOrg.setStatus(Const.STATUS_ENABLED);
                hbzOrg = hbzOrgService.save(hbzOrg);
                if (hbzOrg != null) {
                    return new ResponseDTO(Const.STATUS_OK, "保存成功", hbzOrg);
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "错误");
                }
            }
        }
    }

    //常用地址创建
    @RequestMapping(value = {"/app/org/update"}, method = RequestMethod.POST)
    public ResponseDTO userAddressup(@RequestBody HbzOrgDTO hbzOrgDTO) {
        if (hbzOrgDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "ID为空");
        } else {
            String[] errors = ValidationHelper.valid(hbzOrgDTO, "update");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证不通过", errors);
            } else {
                HbzOrgDTO hbzOrg = hbzOrgService.get(hbzOrgDTO);
                if (hbzOrg == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "ID非法");
                }
                BeanUtils.copyProperties(hbzOrgDTO, hbzOrg, Const.ID_AUDIT_FIELDS);
                hbzOrg.setStatus(Const.STATUS_ENABLED);
                hbzOrg = hbzOrgService.save(hbzOrg);
                if (hbzOrg != null) {
                    return new ResponseDTO(Const.STATUS_OK, "更新成功", hbzOrg);
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "错误");
                }
            }
        }
    }

    //删除常用地址
    @RequestMapping(value = {"/app/org/delete"}, method = RequestMethod.POST)
    public ResponseDTO userAddressdl(@RequestBody HbzOrgDTO hbzOrgDTO) {
        if (hbzOrgDTO.getId() != null) {
            HbzOrgDTO hbzOrg = hbzOrgService.get(hbzOrgDTO);
            if (hbzOrg == null) {
                return new ResponseDTO(Const.STATUS_ERROR, "错误ID必须");
            } else {
                if (hbzOrgService.delete(hbzOrg)) {
                    return new ResponseDTO(Const.STATUS_OK, "删除成功", hbzOrg.getId());
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "删除失败", hbzOrg.getId());
                }
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "错误ID必须");
        }
    }

    //查询地址
    @RequestMapping(value = {"/app/org/query"}, method = RequestMethod.POST)
    public ResponseDTO userAddressq(@RequestBody HbzOrgDTO hbzOrgDTO) {
        hbzOrgDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzOrgService.query(hbzOrgDTO));
    }

    //查询地址分页
    @RequestMapping(value = {"/app/org/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO userAddressp(@RequestBody HbzOrgDTO hbzOrgDTO) {
        hbzOrgDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzOrgService.queryPage(hbzOrgDTO, new PageRequest(hbzOrgDTO.getPage(), hbzOrgDTO.getSize())));
    }

}
