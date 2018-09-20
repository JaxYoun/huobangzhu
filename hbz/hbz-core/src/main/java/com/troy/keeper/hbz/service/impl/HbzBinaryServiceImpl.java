package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzBinary;
import com.troy.keeper.hbz.repository.HbzBinaryRepository;
import com.troy.keeper.hbz.service.HbzBinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/18.
 */
@Service
@Transactional
public class HbzBinaryServiceImpl implements HbzBinaryService {

    @Autowired
    HbzBinaryRepository hbzBinaryRepository;

    @Override
    public String fastSave(String data) {
        String uuid = StringHelper.uuid();
        HbzBinary binary = new HbzBinary();
        binary.setStatus("1");
        binary.setBzHash("UNKNOW");
        binary.setDisplay("UNKNOW");
        binary.setGpHash("UNKNOW");
        binary.setDisplay("未知");
        binary.setKeyHash("UNKNOW");
        binary.setPath("");
        binary.setKey(uuid);
        binary.setData(data);
        binary = hbzBinaryRepository.save(binary);
        return binary.getKey();
    }

    @Override
    public String findData(String key) {
        return hbzBinaryRepository.findByKey(key).getData();
    }
}
