package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.hbz.dto.AgreementTemplateDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.AgreementTemplate;
import com.troy.keeper.hbz.po.HbzTypeVal;
import com.troy.keeper.hbz.repository.AgreementTemplateRepository;
import com.troy.keeper.hbz.repository.HbzTypeValRepo;
import com.troy.keeper.hbz.service.AgreementTemplateService;
import com.troy.keeper.hbz.vo.AgreementTemplateVO;
import com.troy.keeper.hbz.vo.DictionaryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/30 14:51
 */
@Slf4j
@Service
public class AgreementTemplateServiceImpl implements AgreementTemplateService {

    private static final char[] CODE_PREFIX_CHAR_ARR = {'A', '0', '0', '0', '0', '0', '0', '0', '0'};

    private Object lock = new Object();

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private HbzTypeValRepo hbzTypeValRepo;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private AgreementTemplateRepository agreementTemplateRepository;

    /**
     * 添加合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @Override
    @Transactional
    public boolean addAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO) {
        AgreementTemplate agreementTemplate = new AgreementTemplate();
        BeanUtils.copyProperties(agreementTemplateDTO, agreementTemplate);
        agreementTemplate.setCode(this.getAgreementTemplateCode());
        return this.commonRepository.add(agreementTemplate) != null;
    }

    /**
     * 修改合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @Override
    public boolean updateAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO) {
        String attachPath = agreementTemplateDTO.getAttachPath();
        /*if (StringUtils.isNotBlank(attachPath)) {
            agreementTemplateDTO.setAttachPath(StringHelper.getTailFromFullImagePath(this.staticImagePrefix, attachPath));
        }*/
        AgreementTemplate agreementTemplate = (AgreementTemplate) this.commonRepository.findOne(agreementTemplateDTO.getId(), AgreementTemplate.class);
        if (agreementTemplate != null) {
            BeanUtils.copyProperties(agreementTemplateDTO, agreementTemplate, "status", "isEnable", "createdBy", "createdDate", "code");
            return this.commonRepository.update(agreementTemplate) != null;
        }
        return false;
    }

    /**
     * 删除合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @Override
    public boolean deleteAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO) {
        AgreementTemplate agreementTemplate = (AgreementTemplate) this.commonRepository.findOne(agreementTemplateDTO.getId(), AgreementTemplate.class);
        if (agreementTemplate != null) {
            agreementTemplate.setStatus(agreementTemplateDTO.getStatus());
            agreementTemplate.setLastUpdatedBy(agreementTemplateDTO.getLastUpdatedBy());
            agreementTemplate.setLastUpdatedDate(agreementTemplateDTO.getLastUpdatedDate());
            return this.commonRepository.update(agreementTemplate) != null;
        }
        return false;
    }

    /**
     * 启用/禁用合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @Override
    public boolean enableOrDisableAgreementTemplate(AgreementTemplateDTO agreementTemplateDTO) {
        AgreementTemplate agreementTemplate = (AgreementTemplate) this.commonRepository.findOne(agreementTemplateDTO.getId(), AgreementTemplate.class);
        if (agreementTemplate != null) {
            agreementTemplate.setIsEnable(agreementTemplateDTO.getIsEnable());
            agreementTemplate.setLastUpdatedBy(agreementTemplateDTO.getLastUpdatedBy());
            agreementTemplate.setLastUpdatedDate(agreementTemplateDTO.getLastUpdatedDate());
            return this.commonRepository.update(agreementTemplate) != null;
        }
        return false;
    }

    /**
     * 分页条件查询合同模板
     *
     * @param agreementTemplateDTO
     * @return
     */
    @Override
    public Page<AgreementTemplateVO> getAgreementTemplateListByPage(AgreementTemplateDTO agreementTemplateDTO, Pageable pageable) {
        Page<AgreementTemplate> agreementTemplatePage = this.commonRepository.findAll(pageable, AgreementTemplate.class, (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(agreementTemplateDTO.getName())) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + agreementTemplateDTO.getName() + "%"));
            }
            if (StringUtils.isNotBlank(agreementTemplateDTO.getIsEnable())) {
                predicateList.add(criteriaBuilder.equal(root.get("isEnable"), agreementTemplateDTO.getIsEnable()));
            }
            if (StringUtils.isNotBlank(agreementTemplateDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), agreementTemplateDTO.getStatus()));
            }
            if (StringUtils.isNotBlank(agreementTemplateDTO.getType())) {
                predicateList.add(criteriaBuilder.equal(root.get("type"), agreementTemplateDTO.getType()));
            }
            if (agreementTemplateDTO.getCreateDateStart() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("createdDate"), agreementTemplateDTO.getCreateDateStart()));
            }
            if (agreementTemplateDTO.getCreateDateEnd() != null) {
                predicateList.add(criteriaBuilder.le(root.get("createdDate"), agreementTemplateDTO.getCreateDateEnd()));
            }
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });
        return agreementTemplatePage.map(this::entityToVo);
    }

    /**
     * 查询合同模板详情
     *
     * @param agreementTemplateDTO
     * @return
     */
    @Override
    public AgreementTemplateVO getAgreementTemplateDetail(AgreementTemplateDTO agreementTemplateDTO) {
        AgreementTemplate agreementTemplate = (AgreementTemplate) this.commonRepository.findOne(agreementTemplateDTO.getId(), AgreementTemplate.class);
        if (agreementTemplate != null) {
            return this.entityToVo(agreementTemplate);
        }
        return null;
    }

    private AgreementTemplateVO entityToVo(AgreementTemplate agreementTemplate) {
        if (agreementTemplate == null) {
            return null;
        }
        AgreementTemplateVO agreementTemplateVO = new AgreementTemplateVO();
        BeanUtils.copyProperties(agreementTemplate, agreementTemplateVO, "attachPath", "type");
        agreementTemplateVO.setAttachPath(agreementTemplate.getAttachPath());

        if (StringUtils.isNotBlank(agreementTemplate.getType())) {
            HbzTypeVal type = this.hbzTypeValRepo.getByTypeAndVal("AgreementTemplateType", agreementTemplate.getType());
            DictionaryVO typeVo = new DictionaryVO();
            BeanUtils.copyProperties(type, typeVo);
            agreementTemplateVO.setType(typeVo);
        }
        return agreementTemplateVO;
    }

    /**
     * 根据当前数据库最大id生成编号
     *
     * @return
     */
    private String getAgreementTemplateCode() {
        synchronized (this.lock) {
            Long maxId = this.agreementTemplateRepository.getMaxId();
            return maxId == null ? StringHelper.contractCode(0L, this.CODE_PREFIX_CHAR_ARR) : StringHelper.contractCode(maxId, this.CODE_PREFIX_CHAR_ARR);
        }
    }
}