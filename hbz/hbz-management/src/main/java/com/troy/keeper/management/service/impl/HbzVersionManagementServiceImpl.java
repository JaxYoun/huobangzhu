package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.core.utils.string.StringUtils;
import com.troy.keeper.hbz.po.HbzVersionManagement;
import com.troy.keeper.management.dto.HbzVersionManagementDTO;
import com.troy.keeper.management.repository.HbzVersionManagementRepository;
import com.troy.keeper.management.service.HbzVersionManagementService;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.repository.SmUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 移动端版本管理Service实现类
 * @Date: Created in 15:45  2018/2/1.
 * @Midified By:
 */
@Service
@Transactional
public class HbzVersionManagementServiceImpl implements HbzVersionManagementService {

    @Autowired
    private HbzVersionManagementRepository hbzVersionManagementRepository;

    @Autowired
    private SmUserRepository userRepository;

    @Override
    public Page<HbzVersionManagementDTO> queryVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO, Pageable pageable) {
        Page<HbzVersionManagement> pageList = hbzVersionManagementRepository.findAll(new Specification<HbzVersionManagement>() {
            @Override
            public Predicate toPredicate(Root<HbzVersionManagement> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("uploadDate")));//按照上传时间倒序排序
                // 版本编号查询
                if (StringUtils.isNotBlank(hbzVersionManagementDTO.getVersionNo())) {
                    predicateList.add(criteriaBuilder.like(root.get("versionNo"), "%" + hbzVersionManagementDTO.getVersionNo() + "%"));
                }
                // 版本名字查询
                if (StringUtils.isNotBlank(hbzVersionManagementDTO.getVersionName())) {
                    predicateList.add(criteriaBuilder.like(root.get("versionName"), "%" + hbzVersionManagementDTO.getVersionName() + "%"));
                }
                //版本类型
                if (StringUtils.isNotBlank(hbzVersionManagementDTO.getType())) {
                    predicateList.add(criteriaBuilder.equal(root.get("type"), hbzVersionManagementDTO.getType()));
                }
                //if (StringUtils.isNotBlank(hbzVersionManagementDTO.getStatus())) {
                //    predicateList.add(criteriaBuilder.equal(root.get("status"), hbzVersionManagementDTO.getStatus()));
                //}
                //版本状态
                predicateList.add(criteriaBuilder.equal(root.get("status"), "1"));
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
        Page<HbzVersionManagementDTO> HbzVersionManagementDTOPage = pageList.map(new Converter<HbzVersionManagement, HbzVersionManagementDTO>() {
            @Override
            public HbzVersionManagementDTO convert(HbzVersionManagement hbzVersionManagement) {
                HbzVersionManagementDTO versionManagementDTO = new HbzVersionManagementDTO();
                versionManagementDTO.setRecordComment(hbzVersionManagement.getRecordComment());
                versionManagementDTO.setFileUrl(hbzVersionManagement.getFileUrl());
                versionManagementDTO.setIsDisable(hbzVersionManagement.getIsDisable());
                versionManagementDTO.setStatus(hbzVersionManagement.getStatus());
                versionManagementDTO.setVersionNo(hbzVersionManagement.getVersionNo());
                versionManagementDTO.setType(hbzVersionManagement.getType());
                versionManagementDTO.setUploadDate(hbzVersionManagement.getUploadDate());
                SmUser smUser = userRepository.findOne(SecurityUtils.getCurrentUserId());
                versionManagementDTO.setUserName(smUser.getUserName());
                versionManagementDTO.setId(hbzVersionManagement.getId());
                versionManagementDTO.setVersionName(hbzVersionManagement.getVersionName());
                return versionManagementDTO;
            }
        });
        return HbzVersionManagementDTOPage;
    }

    @Override
    public Boolean addVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO) {
        HbzVersionManagement hbzVersionManagement = new HbzVersionManagement();
        BeanUtils.copyProperties(hbzVersionManagementDTO, hbzVersionManagement);
        hbzVersionManagement.setStatus("1");
        hbzVersionManagement.setIsDisable("1");//是否禁用
        hbzVersionManagement.setUploadDate(new Date().getTime());
        hbzVersionManagement.setCreatedDate(hbzVersionManagementDTO.getCreatedDate());
        hbzVersionManagement.setSmUserId(SecurityUtils.getCurrentUserId());
        return hbzVersionManagementRepository.save(hbzVersionManagement) != null;
    }

    @Override
    public Boolean updateVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO) {
        HbzVersionManagement versionManagement = hbzVersionManagementRepository.findOne(hbzVersionManagementDTO.getId());
        if (null != versionManagement) {
            BeanUtils.copyProperties(hbzVersionManagementDTO, versionManagement, "uploadDate");
            versionManagement.setStatus("1");
            return hbzVersionManagementRepository.save(versionManagement) != null;
        } else {
            return false;
        }
    }

    /**
     * 删除app版本
     *
     * @param hbzVersionManagementDTO
     * @return
     */
    @Override
    public boolean deleteVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO) {
        HbzVersionManagement hbzVersionManagement = this.hbzVersionManagementRepository.findOne(hbzVersionManagementDTO.getId());
        if (hbzVersionManagement != null) {
            hbzVersionManagement.setStatus(hbzVersionManagementDTO.getStatus());
            hbzVersionManagement.setLastUpdatedBy(hbzVersionManagementDTO.getLastUpdatedBy());
            hbzVersionManagement.setLastUpdatedDate(hbzVersionManagementDTO.getLastUpdatedDate());
            return this.hbzVersionManagementRepository.save(hbzVersionManagement) != null;
        } else {
            return false;
        }
    }

    /**
     * 是否禁用app版本
     *
     * @param hbzVersionManagementDTO
     * @return
     */
    public boolean isDisableVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO) {
        HbzVersionManagement hbzVersionManagement = this.hbzVersionManagementRepository.findOne(hbzVersionManagementDTO.getId());
        if (null != hbzVersionManagement) {
            hbzVersionManagement.setIsDisable(hbzVersionManagementDTO.getIsDisable());
            return hbzVersionManagementRepository.save(hbzVersionManagement) != null;
        } else {
            return false;
        }
    }
}
