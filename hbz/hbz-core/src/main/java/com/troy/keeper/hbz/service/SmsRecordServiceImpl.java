package com.troy.keeper.hbz.service;

import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.hbz.dto.SmsRecordDTO;
import com.troy.keeper.hbz.po.SmsRecord;
import com.troy.keeper.hbz.vo.SmsRecordVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 17:15
 */
@Service
public class SmsRecordServiceImpl implements SmsRecordService {

    @Autowired
    private CommonRepository commonRepository;

    /**
     * 分页条件查询短信发送记录
     *
     * @param smsRecordDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SmsRecordVO> getSmsRecordListByPage(SmsRecordDTO smsRecordDTO, Pageable pageable) {
        Specification<SmsRecord> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(smsRecordDTO.getPhoneNo())) {
                predicateList.add(criteriaBuilder.equal(root.get("phoneNo"), smsRecordDTO.getPhoneNo()));
            }
            if (StringUtils.isNotBlank(smsRecordDTO.getSendStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("sendStatus"), smsRecordDTO.getSendStatus()));
            }
            if (smsRecordDTO.getCreatedDateGE() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("createdDate"), smsRecordDTO.getCreatedDateGE()));
            }
            if (smsRecordDTO.getCreatedDateLE() != null) {
                predicateList.add(criteriaBuilder.le(root.get("createdDate"), smsRecordDTO.getCreatedDateLE()));
            }
            if (StringUtils.isNotBlank(smsRecordDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), smsRecordDTO.getStatus()));
            }
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });
        Page<SmsRecord> smsRecordPage = this.commonRepository.findAll(pageable, SmsRecord.class, specification);
        return smsRecordPage.map(SmsRecordService::entityToVo);
    }

    /**
     * 获取短信发送记录详情
     *
     * @param smsRecordDTO
     * @return
     */
    @Override
    public SmsRecordVO getSmsRecordDetail(SmsRecordDTO smsRecordDTO) {
        SmsRecord smsRecord = (SmsRecord) this.commonRepository.findOne(smsRecordDTO.getId(), SmsRecord.class);
        return SmsRecordService.entityToVo(smsRecord);
    }
}
