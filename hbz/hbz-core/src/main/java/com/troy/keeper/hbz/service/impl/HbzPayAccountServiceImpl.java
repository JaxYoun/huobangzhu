package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayAccountDTO;
import com.troy.keeper.hbz.po.HbzPayAccount;
import com.troy.keeper.hbz.repository.HbzPayAccountRepository;
import com.troy.keeper.hbz.service.HbzPayAccountService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzPayAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2017/11/3.
 */
@Service
@Transactional
public class HbzPayAccountServiceImpl extends BaseEntityServiceImpl<HbzPayAccount, HbzPayAccountDTO> implements HbzPayAccountService {

    @Autowired
    private HbzPayAccountMapper mapper;
    @Autowired
    private HbzPayAccountRepository repository;
    @Autowired
    private HbzUserService hbzUserService;

    @Override
    public BaseMapper<HbzPayAccount, HbzPayAccountDTO> getMapper() {
        return mapper;
    }

    @Override
    public BaseRepository getRepository() {
        return repository;
    }

    @Override
    public HbzPayAccountDTO getDefaultPayAccountByUID(Long uid) {
        HbzPayAccountDTO query = new HbzPayAccountDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setIsDefault(true);
        query.setUserId(uid);
        List<HbzPayAccountDTO> queryList = query(query);
        if (queryList != null && queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }
}
