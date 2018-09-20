package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAssignWorkDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzAssignWork;
import com.troy.keeper.hbz.repository.HbzAssignWorkRepository;
import com.troy.keeper.hbz.service.HbzAssignWorkService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzAssignWorkMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leecheng on 2018/1/18.
 */
@Service
@Transactional
public class HbzAssignWorkServiceImpl extends BaseEntityServiceImpl<HbzAssignWork, HbzAssignWorkDTO> implements HbzAssignWorkService {

    @Autowired
    HbzAssignWorkRepository hbzAssignWorkRepository;

    @Autowired
    HbzAssignWorkMapper hbzAssignWorkMapper;

    @Override
    public BaseMapper<HbzAssignWork, HbzAssignWorkDTO> getMapper() {
        return hbzAssignWorkMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzAssignWorkRepository;
    }

    @Override
    @SneakyThrows
    public String createWorkNo() {
        String no;
        int idx = 0;
        while (true) {
            no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + StringHelper.frontCompWithZore(++idx, 10);
            if (hbzAssignWorkRepository.countByWorkNo(no) < 1) return no;
        }
    }
}
