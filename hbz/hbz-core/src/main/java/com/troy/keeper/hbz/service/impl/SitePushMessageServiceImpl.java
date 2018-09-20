package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.SitePushMessageDTO;
import com.troy.keeper.hbz.helper.JpushUtils;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.*;
import com.troy.keeper.hbz.service.impl.SitePushMessageContainer;
import com.troy.keeper.hbz.type.Role;
import com.troy.keeper.hbz.vo.DictionaryVO;
import com.troy.keeper.hbz.vo.SitePushMessageVO;
import com.troy.keeper.hbz.service.SitePushMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 10:36
 */
@Slf4j
@Service
public class SitePushMessageServiceImpl implements SitePushMessageService {

    private static final char[] CODE_PREFIX_CHAR_ARR = {'S', '0', '0', '0', '0', '0', '0', '0', '0'};

    private Object lock = new Object();

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private HbzTypeValRepo hbzTypeValRepo;

    @Autowired
    private HbzRoleRepository hbzRoleRepository;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private SitePushMessageRepository sitePushMessageRepository;

    @Autowired
    private SitePushMessageRecordRepository sitePushMessageRecordRepository;

    @Autowired
    private JpushUtils jpushUtils;

    /**
     * 添加站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @Override
    public boolean addSitePushMessage(SitePushMessageDTO sitePushMessageDTO) {
        boolean flag = false;
        SitePushMessage sitePushMessage = new SitePushMessage();
        sitePushMessageDTO.setCode(this.getSitePushMessageCode());
        BeanUtils.copyProperties(sitePushMessageDTO, sitePushMessage);
        SitePushMessage sitePushMessageFromDb = this.sitePushMessageRepository.save(sitePushMessage);
        if (sitePushMessageFromDb != null) {
            if ("2".equals(sitePushMessageFromDb.getPushType())) {
                SitePushMessageContainer.getInstance().getMessageMap().put(sitePushMessageFromDb.getId(), sitePushMessageFromDb);
            }
            flag = true;
        }
        return flag;
    }

    /**
     * 删除站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @Override
    public boolean deleteSitePushMessage(SitePushMessageDTO sitePushMessageDTO) {
        SitePushMessage sitePushMessage = this.sitePushMessageRepository.findOne(sitePushMessageDTO.getId());
        if (sitePushMessage != null) {
            sitePushMessage.setStatus(sitePushMessageDTO.getStatus());
            sitePushMessage.setLastUpdatedBy(sitePushMessageDTO.getLastUpdatedBy());
            sitePushMessage.setLastUpdatedDate(sitePushMessageDTO.getLastUpdatedDate());
            return this.sitePushMessageRepository.save(sitePushMessage) != null;
        } else {
            return false;
        }
    }

    /**
     * 获取站内推送消息详情
     *
     * @param sitePushMessageDTO
     * @return
     */
    @Override
    public SitePushMessageVO getSitePushMessageDetail(SitePushMessageDTO sitePushMessageDTO) {
        SitePushMessage sitePushMessage = this.sitePushMessageRepository.findOne(sitePushMessageDTO.getId());
        return this.entityToVo(sitePushMessage);
    }

    /**
     * 添加站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @Override
    public boolean updateSitePushMessage(SitePushMessageDTO sitePushMessageDTO) {
        boolean flag = false;
        SitePushMessage sitePushMessage = this.sitePushMessageRepository.findOne(sitePushMessageDTO.getId());
        BeanUtils.copyProperties(sitePushMessageDTO, sitePushMessage, "id", "createdBy", "createdDate", "status", "code", "imagePath");

        sitePushMessage.setImagePath(StringHelper.getTailFromFullImagePath(staticImagePrefix, sitePushMessageDTO.getImagePath()));

        SitePushMessage sitePushMessageFromDb = this.sitePushMessageRepository.save(sitePushMessage);
        if (sitePushMessageFromDb != null) {
            if ("2".equals(sitePushMessageFromDb.getPushType())) {
                SitePushMessageContainer.getInstance().getMessageMap().put(sitePushMessageFromDb.getId(), sitePushMessageFromDb);
            }
            flag = true;
        }
        return flag;
    }

    /**
     * 分页条件查询站内推送消息
     *
     * @param sitePushMessageDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SitePushMessageVO> getSitePushMessageListByPage(SitePushMessageDTO sitePushMessageDTO, Pageable pageable) {
        Specification<SitePushMessage> pushMessagePredicate = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(sitePushMessageDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), sitePushMessageDTO.getStatus()));
            }
            if (StringUtils.isNotBlank(sitePushMessageDTO.getTitle())) {
                predicateList.add(criteriaBuilder.like(root.get("title"), "%" + sitePushMessageDTO.getTitle() + "%"));
            }
            if (StringUtils.isNotBlank(sitePushMessageDTO.getMessageType())) {
                predicateList.add(criteriaBuilder.equal(root.get("messageType"), sitePushMessageDTO.getMessageType()));
            }
            if (StringUtils.isNotBlank(sitePushMessageDTO.getConsumerType())) {
                predicateList.add(criteriaBuilder.equal(root.get("consumerType"), sitePushMessageDTO.getConsumerType()));
            }
            if (StringUtils.isNotBlank(sitePushMessageDTO.getIfSend())) {
                predicateList.add(criteriaBuilder.equal(root.get("ifSend"), sitePushMessageDTO.getIfSend()));
            }
            if (StringUtils.isNotBlank(sitePushMessageDTO.getReceivePlatformType())) {
                predicateList.add(criteriaBuilder.equal(root.get("receivePlatformType"), sitePushMessageDTO.getReceivePlatformType()));
            }
            if (StringUtils.isNotBlank(sitePushMessageDTO.getPushType())) {
                predicateList.add(criteriaBuilder.equal(root.get("pushType"), sitePushMessageDTO.getPushType()));
            }
            if (sitePushMessageDTO.getSendTimeStart() != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sendTime"), sitePushMessageDTO.getSendTimeStart()));
            }
            if (sitePushMessageDTO.getSendTimeEnd() != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("sendTime"), sitePushMessageDTO.getSendTimeEnd()));
            }
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });
        Page<SitePushMessage> sitePushMessagePage = this.sitePushMessageRepository.findAll(pushMessagePredicate, pageable);
        return sitePushMessagePage.map(this::entityToVo);
    }

    /**
     * 手动推送站内消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @Override
    public boolean sendSitePushMessage(SitePushMessageDTO sitePushMessageDTO) {
        boolean flag = false;
        SitePushMessage sitePushMessage = this.sitePushMessageRepository.findOne(sitePushMessageDTO.getId());
        if (this.pushMessage(sitePushMessage)) {
            if ("1".equals(sitePushMessage.getPushType())) {
                sitePushMessage.setSendTime(new Date().getTime());
            }
            sitePushMessage.setIfSend(Const.STATUS_ENABLED);
            this.sitePushMessageRepository.save(sitePushMessage);
            flag = true;
        }
        return flag;
    }

    /**
     * 通过id查询推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    @Override
    public boolean getSitePushMessageById(SitePushMessageDTO sitePushMessageDTO) {
        return this.sitePushMessageRepository.getSitePushMessageByIdAndIfSendAndStatus(sitePushMessageDTO.getId(), sitePushMessageDTO.getIfSend(), sitePushMessageDTO.getStatus()) != null;
    }


    /**
     * 手动、定时任务都从这里实现消息推送
     *
     * @param sitePushMessage
     * @return
     */
    public boolean pushMessage(SitePushMessage sitePushMessage) {
        boolean flag = false;
        String consumerType = sitePushMessage.getConsumerType();
        String receivePlatformType = sitePushMessage.getReceivePlatformType();
        switch (consumerType) {
            case "1": {  //所有注册用户
                List<HbzUser> hbzUserList = this.hbzUserRepository.getDistinctByActivatedAndStatus(true, Const.STATUS_ENABLED);
                if ("1".equals(receivePlatformType)) {  //定时推送
                    this.pushToApp(sitePushMessage, hbzUserList);
                }
                this.batchInsertPushRecord(sitePushMessage, hbzUserList);
                flag = true;
                break;
            }
            case "2": {  //货主
                List<Role> roleList = Arrays.asList(Role.Consignor, Role.EnterpriseConsignor);
                List<HbzRole> hbzRoleList = this.hbzRoleRepository.getDistinctByRoleInAndStatus(roleList, Const.STATUS_ENABLED);
                List<HbzUser> hbzUserList = this.hbzUserRepository.getDistinctByRolesInAndActivatedAndStatus(hbzRoleList, true, Const.STATUS_ENABLED);
                if ("1".equals(receivePlatformType)) {
                    this.pushToApp(sitePushMessage, hbzUserList);
                }
                this.batchInsertPushRecord(sitePushMessage, hbzUserList);
                flag = true;
                break;
            }
            case "3": {  //车主
                List<Role> roleList = Arrays.asList(Role.PersonDriver, Role.EnterpriseAdmin, Role.EnterpriseDriver);
                List<HbzRole> hbzRoleList = this.hbzRoleRepository.getDistinctByRoleInAndStatus(roleList, Const.STATUS_ENABLED);
                List<HbzUser> hbzUserList = this.hbzUserRepository.getDistinctByRolesInAndActivatedAndStatus(hbzRoleList, true, Const.STATUS_ENABLED);
                if ("1".equals(receivePlatformType)) {
                    this.pushToApp(sitePushMessage, hbzUserList);
                }
                this.batchInsertPushRecord(sitePushMessage, hbzUserList);
                flag = true;
                break;
            }
            case "4": {  //单个用户 OK
                List<HbzUser> hbzUserList = this.hbzUserRepository.getHbzUserByActivatedAndStatusAndTelephone(true, Const.STATUS_ENABLED, sitePushMessage.getConsumerPhoneNo());
                if ("1".equals(receivePlatformType)) {
                    this.pushToApp(sitePushMessage, hbzUserList);
                }
                this.batchInsertPushRecord(sitePushMessage, hbzUserList);
                flag = true;
                break;
            }
            case "5": {
                List<HbzUser> hbzUserList = currentUsers.get().stream().map(HbzUserDTO::getId).map(hbzUserRepository::findOne).collect(Collectors.toList());
                if ("1".equals(receivePlatformType)) {
                    this.pushToApp(sitePushMessage, hbzUserList);
                }
                this.batchInsertPushRecord(sitePushMessage, hbzUserList);
                flag = true;
                break;
            }
            default: {
                break;
            }
        }
        return flag;
    }

    private ThreadLocal<List<HbzUserDTO>> currentUsers = new ThreadLocal<>();

    @Override
    public boolean sendMessageImmediately(List<HbzUserDTO> users, String title, String summary, String message) {
        currentUsers.set(users);
        SitePushMessage sitePushMessage = new SitePushMessage();
        sitePushMessage.setCode(getSitePushMessageCode());
        sitePushMessage.setConsumerType("5");
        sitePushMessage.setContent(message);
        sitePushMessage.setIfSend("0");
        sitePushMessage.setImagePath(null);
        sitePushMessage.setMessageType("1");
        sitePushMessage.setPushType("1");
        sitePushMessage.setReceivePlatformType("1");
        sitePushMessage.setRemark("");
        sitePushMessage.setSendTime(null);
        sitePushMessage.setSummary(summary);
        sitePushMessage.setTitle(title);
        sitePushMessage.setStatus("1");
        sitePushMessage = sitePushMessageRepository.save(sitePushMessage);
        SitePushMessageDTO sendDTO = new SitePushMessageDTO();
        sendDTO.setId(sitePushMessage.getId());
        return sendSitePushMessage(sendDTO);
    }

    /**
     * 批量保存推送记录
     *
     * @param sitePushMessage
     * @param receiveUserList
     */
    @Transactional
    public void batchInsertPushRecord(SitePushMessage sitePushMessage, List<HbzUser> receiveUserList) {
        List<SitePushMessageRecord> sitePushMessageRecordList = receiveUserList.stream()
                .map(it -> {
                    SitePushMessageRecord sitePushMessageRecord = new SitePushMessageRecord();
                    sitePushMessageRecord.setConsumer(it);
                    sitePushMessageRecord.setPhoneNo(it.getTelephone());
                    sitePushMessageRecord.setSitePushMessage(sitePushMessage);
                    sitePushMessageRecord.setIfRead(Const.STATUS_DISABLED);
                    sitePushMessageRecord.setStatus(Const.STATUS_ENABLED);
                    sitePushMessageRecord.setCreatedBy(SecurityUtils.getCurrentUserId());
                    sitePushMessageRecord.setCreatedDate(new Date().getTime());
                    sitePushMessageRecord.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
                    sitePushMessageRecord.setLastUpdatedDate(new Date().getTime());
                    return sitePushMessageRecord;
                }).collect(Collectors.toList());
        this.sitePushMessageRecordRepository.save(sitePushMessageRecordList);
    }

    /**
     * 将消息推送至app
     *
     * @param sitePushMessage
     * @param receiveUserList
     */
    public void pushToApp(SitePushMessage sitePushMessage, List<HbzUser> receiveUserList) {
        List<String> aliasList = receiveUserList.stream().map(HbzUser::getTelephone).distinct().map("hbz_"::concat).collect(Collectors.toList());
        String[] aliasArr = new String[aliasList.size()];
        aliasList.toArray(aliasArr);
        Map<String, String> extraMap = new HashMap<>();
        extraMap.put("n_sitePushMessageId", sitePushMessage.getId().toString());

        /*extraMap.put("n_title", sitePushMessage.getTitle());
        extraMap.put("n_content", sitePushMessage.getContent());
        extraMap.put("n_createdDate", DateUtils.instantToString(Instant.now(), "yyyy年MM月dd日"));
        String[] aliasArr = {"hbz_18080818652", "hbz_18781983881", "88888"};*/
        this.jpushUtils.jiguangPush(aliasArr, sitePushMessage.getTitle(), extraMap, sitePushMessage.getSummary());
    }

    /**
     * 消息entity转vo
     *
     * @param sitePushMessage
     * @return
     */
    public SitePushMessageVO entityToVo(SitePushMessage sitePushMessage) {
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
        if (sitePushMessage.getImagePath() != null) {
            String fullIamgePath = this.staticImagePrefix + sitePushMessage.getImagePath();
            sitePushMessageVO.setImagePath(fullIamgePath);
        } else {
            sitePushMessageVO.setImagePath(null);
        }
        BeanUtils.copyProperties(sitePushMessage, sitePushMessageVO, "messageType", "consumerType", "imagePath", "receivePlatformType", "pushType");
        return sitePushMessageVO;
    }

    /**
     * 根据当前数据库最大id生成编号
     *
     * @return
     */
    private String getSitePushMessageCode() {
        synchronized (this.lock) {
            Long maxId = this.sitePushMessageRepository.getMaxIdSitePushMessage();
            return maxId == null ? StringHelper.contractCode(0L, this.CODE_PREFIX_CHAR_ARR) : StringHelper.contractCode(maxId, this.CODE_PREFIX_CHAR_ARR);
        }
    }

    /**
     * 定时推送
     */
    @Scheduled(cron = "59 * * * * *")
    public void timedSend() {
        Iterator<ConcurrentHashMap.Entry<Long, SitePushMessage>> it = SitePushMessageContainer.getInstance().getMessageMap().entrySet().iterator();
        while (it.hasNext()) {
            ConcurrentHashMap.Entry<Long, SitePushMessage> entry = it.next();
            SitePushMessage sitePushMessage = entry.getValue();
            Long timeToSend = sitePushMessage.getSendTime();
            Long now = Instant.now().getEpochSecond() * 1000;
            Long diff = timeToSend - now;
            if (diff <= 60L) {
                /*if (diff <= -259200L) {  //超时三天就不发送，直接清除掉
                    it.remove();
                    continue;
                }*/
                if (Const.STATUS_ENABLED.equals(sitePushMessageRepository.findOne(sitePushMessage.getId()).getIfSend())) {
                    it.remove();
                } else {
                    if (this.pushMessage(sitePushMessage)) {
                        if ("1".equals(sitePushMessage.getPushType())) {
                            sitePushMessage.setSendTime(new Date().getTime());
                        }
                        sitePushMessage.setIfSend(Const.STATUS_ENABLED);
                        this.sitePushMessageRepository.save(sitePushMessage);
                        log.info("推送了第[" + sitePushMessage.getId() + "]号消息");
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "30 * * * * *")
    public void timedSyncFromDb() {
        ConcurrentHashMap<Long, SitePushMessage> map = SitePushMessageContainer.getInstance().getMessageMap();
        List<SitePushMessage> sitePushMessageList = this.sitePushMessageRepository.getDistinctByIfSendAndStatusAndPushType(Const.STATUS_DISABLED, Const.STATUS_ENABLED, "2");
        sitePushMessageList.forEach(it -> map.put(it.getId(), it));
        log.info("消息推送同步[" + map.size() + "]条");
    }

    /*public static void main(String[] args) {
        System.out.println(1521703692000L - Instant.now().getEpochSecond() * 1000);
    }*/
}