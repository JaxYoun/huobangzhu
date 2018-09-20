package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.AgreementTemplateDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.AgreementTemplateService;
import com.troy.keeper.hbz.vo.AgreementTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/27 9:18
 */
@Slf4j
@RestController
@RequestMapping("/api/web/agreement")
public class AgreementResource {

    @Autowired
    private AgreementTemplateService agreementTemplateService;

    /**
     * 分页条件查询合同列表
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/getAgreementListByPage")
    public ResponseDTO getAgreementListByPage(@RequestBody AgreementTemplateDTO agreementTemplateDTO) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        agreementTemplateDTO.setStatus(Const.STATUS_ENABLED);
        Pageable pageable = new PageRequest(agreementTemplateDTO.getPage(), agreementTemplateDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "成功！", this.agreementTemplateService.getAgreementTemplateListByPage(agreementTemplateDTO, pageable));
    }

    /**
     * 查询合同详情
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/getAgreementDetail")
    public ResponseDTO getAgreementDetail(@RequestBody AgreementTemplateDTO agreementTemplateDTO) {
        if (agreementTemplateDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        } else {
            AgreementTemplateVO agreementTemplateVO = this.agreementTemplateService.getAgreementTemplateDetail(agreementTemplateDTO);
            if (agreementTemplateVO != null) {
                return new ResponseDTO(Const.STATUS_OK, "查询成功", agreementTemplateVO);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "查询失败", null);
            }
        }
    }

    /**
     * 通过页面展现合同
     *
     * @param attachPath
     * @param modelAndView
     * @return
     */
    @GetMapping("/showAgreementDetail")
    public ModelAndView showAgreementDetail(String attachPath, ModelAndView modelAndView) {
        modelAndView.setViewName(StringHelper.cutPostfix(attachPath));
        return modelAndView;
    }

}