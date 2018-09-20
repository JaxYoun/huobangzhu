package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzScoreChangeDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzScoreChange;
import com.troy.keeper.hbz.repository.HbzScoreChangeRepository;
import com.troy.keeper.hbz.service.HbzScoreChangeService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreChangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/22.
 */
@Service
@Transactional
public class HbzScoreChangeServiceImpl extends BaseEntityServiceImpl<HbzScoreChange, HbzScoreChangeDTO> implements HbzScoreChangeService {
    @Autowired
    HbzScoreChangeRepository hbzScoreChangeRepository;
    @Autowired
    HbzScoreChangeMapper hbzScoreChangeMapper;

    @Override
    public BaseMapper<HbzScoreChange, HbzScoreChangeDTO> getMapper() {
        return hbzScoreChangeMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzScoreChangeRepository;
    }

    @Override
    public String createNo() {
        String no;
        int index = 0;
        while (true) {
            no = StringHelper.frontCompWithZero(++index, 6);
            if (hbzScoreChangeRepository.countByRecNo(no) < 1) return no;
        }
    }

    /**
     * 计算变化的分数
     * @param currentId 当前用户id
     * @return
     */
    @Override
    public Integer countByDelta(Long currentId) {
        return hbzScoreChangeRepository.countByDelta(currentId);
    }
}
