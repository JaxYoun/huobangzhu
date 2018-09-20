package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzScoreDTO;
import com.troy.keeper.hbz.po.HbzScore;
import com.troy.keeper.hbz.repository.HbzScoreRepository;
import com.troy.keeper.hbz.service.HbzScoreService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2017/12/19.
 */
@Service
@Transactional
public class HbzScoreServiceImplements extends BaseEntityServiceImpl<HbzScore, HbzScoreDTO> implements HbzScoreService {
    @Autowired
    HbzUserService hbzUserService;
    @Autowired
    HbzScoreRepository hbzScoreRepository;

    @Autowired
    HbzScoreMapper hbzScoreMapper;

    @Override
    public BaseMapper<HbzScore, HbzScoreDTO> getMapper() {
        return hbzScoreMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzScoreRepository;
    }

    @Override
    public HbzScoreDTO attach(Long userId) {
        HbzScoreDTO query = new HbzScoreDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(userId);
        List<HbzScoreDTO> scores = query(query);
        if (scores != null && scores.size() > 0) {
            return scores.get(0);
        } else {
            HbzScoreDTO score = new HbzScoreDTO();
            score.setUserId(userId);
            score.setStatus(Const.STATUS_ENABLED);
            score.setScore(0D);
            score = save(score);
            return score;
        }
    }
}
