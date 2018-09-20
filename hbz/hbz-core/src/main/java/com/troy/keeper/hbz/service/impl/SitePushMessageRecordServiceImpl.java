package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.SitePushMessageRecordDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.HbzTypeVal;
import com.troy.keeper.hbz.po.SitePushMessage;
import com.troy.keeper.hbz.po.SitePushMessageRecord;
import com.troy.keeper.hbz.repository.HbzTypeValRepo;
import com.troy.keeper.hbz.repository.SitePushMessageRecordRepository;
import com.troy.keeper.hbz.service.SitePushMessageRecordService;
import com.troy.keeper.hbz.vo.DictionaryVO;
import com.troy.keeper.hbz.vo.HbzUserVO;
import com.troy.keeper.hbz.vo.SitePushMessageRecoredVO;
import com.troy.keeper.hbz.vo.SitePushMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/25 11:21
 */
@Slf4j
@Service
public class SitePushMessageRecordServiceImpl implements SitePushMessageRecordService {

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private HbzTypeValRepo hbzTypeValRepo;

    @Autowired
    private SitePushMessageRecordRepository sitePushMessageRecordRepository;

    /**
     * 分页条件查询推送记录
     *
     * @param sitePushMessageRecordDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SitePushMessageRecoredVO> getSitePushMessageRecordListByPage(SitePushMessageRecordDTO sitePushMessageRecordDTO, Pageable pageable, boolean... isApp) {
        Specification<SitePushMessageRecord> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(sitePushMessageRecordDTO.getTitle())) {
                predicateList.add(criteriaBuilder.like(root.get("sitePushMessage").get("title"), "%" + sitePushMessageRecordDTO.getTitle() + "%"));
            }
            if (StringUtils.isNotBlank(sitePushMessageRecordDTO.getConsumerType())) {
                predicateList.add(criteriaBuilder.equal(root.get("sitePushMessage").get("consumerType"), sitePushMessageRecordDTO.getConsumerType()));
            }
            if (StringUtils.isNotBlank(sitePushMessageRecordDTO.getMessageType())) {
                predicateList.add(criteriaBuilder.equal(root.get("sitePushMessage").get("messageType"), sitePushMessageRecordDTO.getMessageType()));
            }
            if (StringUtils.isNotBlank(sitePushMessageRecordDTO.getIfRead())) {
                predicateList.add(criteriaBuilder.equal(root.get("ifRead"), sitePushMessageRecordDTO.getIfRead()));
            }
            if (StringUtils.isNotBlank(sitePushMessageRecordDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), sitePushMessageRecordDTO.getStatus()));
            }
            if (sitePushMessageRecordDTO.getConsumerId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("consumer").get("id"), sitePushMessageRecordDTO.getConsumerId()));
            }

            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });

        Page<SitePushMessageRecord> sitePushMessageRecordPage = this.sitePushMessageRecordRepository.findAll(specification, pageable);
        return sitePushMessageRecordPage.map(it -> this.recoredEntityToVO(it, isApp[0]));
    }

    /**
     * 后台管理端、从app列表获取消息推送记录详情
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public SitePushMessageRecoredVO getSitePushMessageRecordDetail(Long id, boolean... isApp) {
        SitePushMessageRecord sitePushMessageRecord = this.sitePushMessageRecordRepository.findOne(id);
        if (isApp[0] && sitePushMessageRecord != null && "0".equals(sitePushMessageRecord.getIfRead())) {
            this.sitePushMessageRecordRepository.updateIfReadAndReadTimeById(Const.STATUS_ENABLED, new Date().getTime(), sitePushMessageRecord.getId());
        }
        return this.recoredEntityToVO(sitePushMessageRecord, isApp[0]);
    }

    /**
     * app端从【消息窗】阅读送消息记录
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    @Override
    @Transactional
    public SitePushMessageRecoredVO readSitePushMessageRecord(SitePushMessageRecordDTO sitePushMessageRecordDTO, boolean... isApp) {
        SitePushMessageRecord sitePushMessageRecord = this.sitePushMessageRecordRepository.getSitePushMessageRecordByConsumerIdAndSitePushMessageIdAndStatus(
                sitePushMessageRecordDTO.getConsumerId(),
                sitePushMessageRecordDTO.getSitePushMessageId(),
                sitePushMessageRecordDTO.getStatus()
        );
        SitePushMessageRecoredVO sitePushMessageRecoredVO = null;
        if (sitePushMessageRecord != null) {
            sitePushMessageRecoredVO = this.recoredEntityToVO(sitePushMessageRecord, isApp[0]);
            this.sitePushMessageRecordRepository.updateIfReadAndReadTimeById(Const.STATUS_ENABLED, new Date().getTime(), sitePushMessageRecord.getId());
        }
        return sitePushMessageRecoredVO;
    }

    /**
     * 获取当前用户的未阅读站内消息条数
     *
     * @param phoneNo
     * @param isApp
     * @return
     */
    @Override
    public Integer getMyUnreadSitePushMessageRecordCount(String phoneNo, boolean... isApp) {
        return this.sitePushMessageRecordRepository.countByPhoneNoAndStatusAndIfRead(phoneNo, Const.STATUS_ENABLED, "0");
    }

    /**
     * 将消息推送记录DO转换为VO
     *
     * @param sitePushMessageRecord
     * @return
     */
    public SitePushMessageRecoredVO recoredEntityToVO(SitePushMessageRecord sitePushMessageRecord, boolean... isApp) {
        if (sitePushMessageRecord == null) {
            return null;
        }
        SitePushMessageRecoredVO sitePushMessageRecoredVO = new SitePushMessageRecoredVO();
        HbzUserVO hbzUserVO = new HbzUserVO();
        BeanUtils.copyProperties(sitePushMessageRecord.getConsumer(), hbzUserVO);
        sitePushMessageRecoredVO.setConsumer(hbzUserVO);
        sitePushMessageRecoredVO.setIfRead("0".equals(sitePushMessageRecord.getIfRead()) ? "未阅读" : "已阅读");
        sitePushMessageRecoredVO.setSitePushMessage(this.entityToVo(sitePushMessageRecord.getSitePushMessage(), isApp));
        if (isApp[0]) {
            sitePushMessageRecoredVO.setFormatedCreateDate(DateUtils.longToNoSecondString(sitePushMessageRecord.getCreatedDate()));
            sitePushMessageRecoredVO.setFormattedUpdateDate(DateUtils.longToNoSecondString(sitePushMessageRecord.getLastUpdatedDate()));
            if (sitePushMessageRecord.getReadTime() != null) {
                sitePushMessageRecoredVO.setFormatedReadTime(DateUtils.longToNoSecondString(sitePushMessageRecord.getReadTime()));
            }
        }
        BeanUtils.copyProperties(sitePushMessageRecord, sitePushMessageRecoredVO, "consumer", "sitePushMessage", "ifRead");
        return sitePushMessageRecoredVO;
    }

    /**
     * 消息传vo
     *
     * @param sitePushMessage
     * @return
     */
    public SitePushMessageVO entityToVo(SitePushMessage sitePushMessage, boolean... isApp) {
        if (sitePushMessage == null) {
            return null;
        }
        SitePushMessageVO sitePushMessageVO = new SitePushMessageVO();
        if (StringUtils.isNotBlank(sitePushMessage.getMessageType())) {
            HbzTypeVal messageType = this.hbzTypeValRepo.getByTypeAndVal("SitePushMessageType", sitePushMessage.getMessageType());
            DictionaryVO messageTypeVo = new DictionaryVO();
            BeanUtils.copyProperties(messageType, messageTypeVo);
            sitePushMessageVO.setMessageType(messageTypeVo);
        }
        if (StringUtils.isNotBlank(sitePushMessage.getConsumerType())) {
            HbzTypeVal consumerType = this.hbzTypeValRepo.getByTypeAndVal("SitePushMessageConsumerType", sitePushMessage.getConsumerType());
            DictionaryVO consumerTypeVo = new DictionaryVO();
            BeanUtils.copyProperties(consumerType, consumerTypeVo);
            sitePushMessageVO.setConsumerType(consumerTypeVo);
        }
        if (StringUtils.isNotBlank(sitePushMessage.getReceivePlatformType())) {
            HbzTypeVal receivePlatformType = this.hbzTypeValRepo.getByTypeAndVal("SitePushReceivePlatformType", sitePushMessage.getReceivePlatformType());
            DictionaryVO receivePlatformTypeVo = new DictionaryVO();
            BeanUtils.copyProperties(receivePlatformType, receivePlatformTypeVo);
            sitePushMessageVO.setReceivePlatformType(receivePlatformTypeVo);
        }
        if (StringUtils.isNotBlank(sitePushMessage.getPushType())) {
            HbzTypeVal pushType = this.hbzTypeValRepo.getByTypeAndVal("SitePushType", sitePushMessage.getPushType());
            DictionaryVO pushTypeVo = new DictionaryVO();
            BeanUtils.copyProperties(pushType, pushTypeVo);
            sitePushMessageVO.setPushType(pushTypeVo);
        }
        if (isApp[0]) {
            sitePushMessageVO.setFormattedCreateDate(DateUtils.longToNoSecondString(sitePushMessage.getCreatedDate()));
            sitePushMessageVO.setFormattedUpdateDate(DateUtils.longToNoSecondString(sitePushMessage.getLastUpdatedDate()));
            sitePushMessageVO.setFormattedSendTime(DateUtils.longToNoSecondString(sitePushMessage.getSendTime()));
        }
        String fullIamgePath = this.staticImagePrefix + sitePushMessage.getImagePath();
        sitePushMessageVO.setImagePath(fullIamgePath);
        BeanUtils.copyProperties(sitePushMessage, sitePushMessageVO, "messageType", "consumerType", "imagePath", "receivePlatformType", "pushType");
        return sitePushMessageVO;
    }

}
