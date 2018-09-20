package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.service.CommonService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/10/17.
 */
@Service
@Transactional
public class CommonServiceImpl implements CommonService {

    @Override
    public Object txDo(Exe ex, Object in) {
        return ex.txDo(in);
    }
}
