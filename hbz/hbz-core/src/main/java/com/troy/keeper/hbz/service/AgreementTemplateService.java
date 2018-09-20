package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.AgreementTemplateDTO;
import com.troy.keeper.hbz.vo.AgreementTemplateVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/30 14:31
 */
public interface AgreementTemplateService {

    /**
     * 添加合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    boolean addAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO);

    /**
     * 修改合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    boolean updateAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO);

    /**
     * 删除合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    boolean deleteAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO);

    /**
     * 启用/禁用合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    boolean enableOrDisableAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO);

    /**
     * 分页条件查询合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    Page<AgreementTemplateVO> getAgreementTemplateListByPage(AgreementTemplateDTO agreementTemplateDTO, Pageable pageable);

    /**
     * 查询合同模板详情
     *
     * @param agreementTemplateDTO
     * @return
     */
    AgreementTemplateVO getAgreementTemplateDetail(AgreementTemplateDTO agreementTemplateDTO);

}
