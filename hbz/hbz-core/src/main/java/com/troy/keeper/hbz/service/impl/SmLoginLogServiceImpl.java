package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.SmLoginLogDTO;
import com.troy.keeper.hbz.service.SmLoginLogServeic;
import com.troy.keeper.hbz.vo.SmLoginLogVO;
import com.troy.keeper.system.domain.SmLoginLog;
import com.troy.keeper.system.repository.SmLoginLogRepository;
import com.troy.keeper.system.service.SmLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @DateTime：2018/1/22 12:56
 */
@Slf4j
@Service
public class SmLoginLogServiceImpl implements SmLoginLogServeic {

    @Autowired
    private SmLoginLogRepository smLoginLogRepository;

    /**
     * 获取日志详情
     *
     * @param smLoginLogDTO
     * @return
     */
    @Override
    public SmLoginLogVO getSmLogDetail(SmLoginLogDTO smLoginLogDTO) {
        return SmLoginLogServeic.entityToVo(smLoginLogRepository.findOne(smLoginLogDTO.getId()));
    }

    /**
     * 分页条件查询后台登录日志
     *
     * @param smLoginLogDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SmLoginLogVO> getSmLogListByPage(SmLoginLogDTO smLoginLogDTO, Pageable pageable) {
        Specification<SmLoginLog> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if(smLoginLogDTO.getUserId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("userId"), smLoginLogDTO.getUserId()));
            }
            if(StringUtils.isNotBlank(smLoginLogDTO.getIp())) {
                predicateList.add(criteriaBuilder.equal(root.get("ip"), smLoginLogDTO.getIp()));
            }
            if(smLoginLogDTO.getLoginType() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("loginType"), smLoginLogDTO.getLoginType()));
            }
            if(smLoginLogDTO.getCreatedDateGE() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("createDt"), smLoginLogDTO.getCreatedDateGE()));
            }
            if(smLoginLogDTO.getCreatedDateLE() != null) {
                predicateList.add(criteriaBuilder.le(root.get("createDt"), smLoginLogDTO.getCreatedDateLE()));
            }
            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        });

        return this.smLoginLogRepository.findAll(specification, pageable).map(SmLoginLogServeic::entityToVo);
    }
}
