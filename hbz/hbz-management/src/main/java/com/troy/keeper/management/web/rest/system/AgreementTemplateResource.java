package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.AgreementTemplateDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.AgreementTemplateService;
import com.troy.keeper.hbz.vo.AgreementTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：合同模板管理
 * @DateTime：2018/1/30 14:32
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/agreementTemplate")
public class AgreementTemplateResource {

    @Autowired
    private AgreementTemplateService agreementTemplateService;

    /**
     * 添加合同模板
     *
     * @param agreementTemplateDTO
     * @param validResult
     * @return
     */
    @PostMapping("/addAgreementTemplate")
    public ResponseDTO addAgreementTemplate(@RequestBody @Valid AgreementTemplateDTO agreementTemplateDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        agreementTemplateDTO.setIsEnable(Const.STATUS_ENABLED);
        agreementTemplateDTO.setStatus(Const.STATUS_ENABLED);
        agreementTemplateDTO.setCreatedBy(SecurityUtils.getCurrentUserId());
        agreementTemplateDTO.setCreatedDate(Instant.now().getEpochSecond());
        agreementTemplateDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        agreementTemplateDTO.setLastUpdatedDate(Instant.now().getEpochSecond());

        if (this.agreementTemplateService.addAgreementTemplate(agreementTemplateDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "添加成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "添加失败", null);
    }

    /**
     * 修改合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/updateAgreementTemplate")
    public ResponseDTO updateAgreementTemplate(@RequestBody AgreementTemplateDTO agreementTemplateDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        if (agreementTemplateDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        agreementTemplateDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        agreementTemplateDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.agreementTemplateService.updateAgreementTemplate(agreementTemplateDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "修改成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "修改失败", null);
    }

    /**
     * 删除合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/deleteAgreementTemplate")
    public ResponseDTO deleteAgreementTemplate(@RequestBody AgreementTemplateDTO agreementTemplateDTO) {
        if (agreementTemplateDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        agreementTemplateDTO.setStatus(Const.STATUS_DISABLED);
        agreementTemplateDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        agreementTemplateDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.agreementTemplateService.deleteAgreementTemplate(agreementTemplateDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "删除成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "删除失败", null);
    }

    /**
     * 启用/禁用合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/enableOrDisableAgreementTemplate")
    public ResponseDTO enableOrDisableAgreementTemplate(@RequestBody AgreementTemplateDTO agreementTemplateDTO) {
        if (agreementTemplateDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        agreementTemplateDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        agreementTemplateDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.agreementTemplateService.enableOrDisableAgreementTemplate(agreementTemplateDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "操作失败", null);
    }

    /**
     * 分页条件查询合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/getAgreementTemplateListByPage")
    public ResponseDTO getAgreementTemplateListByPage(@RequestBody AgreementTemplateDTO agreementTemplateDTO) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        agreementTemplateDTO.setStatus(Const.STATUS_ENABLED);
        Pageable pageable = new PageRequest(agreementTemplateDTO.getPage(), agreementTemplateDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "成功", this.agreementTemplateService.getAgreementTemplateListByPage(agreementTemplateDTO, pageable));
    }

    /**
     * 查询合同模板详情
     *
     * @param agreementTemplateDTO
     * @return
     */
    @PostMapping("/getAgreementTemplateDetail")
    public ResponseDTO getAgreementTemplateDetail(@RequestBody AgreementTemplateDTO agreementTemplateDTO) {
        if (agreementTemplateDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        AgreementTemplateVO agreementTemplateVO = this.agreementTemplateService.getAgreementTemplateDetail(agreementTemplateDTO);
        if (agreementTemplateVO != null) {
            return new ResponseDTO(Const.STATUS_OK, "查询成功", agreementTemplateVO);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "查询失败", null);
    }

    /**
     * 通过页面展现合同
     *
     * @param attachPath
     * @param modelAndView
     * @return
     */
    @GetMapping("/showAgreementTemplateDetail")
    public ModelAndView showAgreementTemplateDetail(String attachPath, ModelAndView modelAndView) {
        modelAndView.setViewName(StringHelper.cutPostfix(attachPath));
        return modelAndView;
    }

    @GetMapping("/freemarkerHtmlTest")
    public ModelAndView freemarkerHtmlTest(ModelAndView modelAndView) {
        modelAndView.setViewName("/AgreementTemplateTest");
        return modelAndView;
    }

    @GetMapping("/freemarkerFtlTest")
    public ModelAndView freemarkerFtlTest(ModelAndView modelAndView) {
        modelAndView.setViewName("/AgreementTemplateTest");
        return modelAndView;
    }

}
