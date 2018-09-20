package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.repository.HbzWareInfoRepository;
import com.troy.keeper.hbz.service.HbzWareInfoService;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.HbzWareInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/21.
 */
@Service
@Transactional
public class HbzWareInfoServiceImpl implements HbzWareInfoService {

    @Autowired
    private HbzWareTypeService hbzWareTypeService;

    @Autowired
    private HbzWareInfoRepository hbzWareInfoRepository;

    @Autowired
    private HbzWareInfoMapper hbzWareInfoMapper;

    @Override
    public String createNo(Long typeId) {
        HbzWareTypeDTO query = new HbzWareTypeDTO();
        query.setId(typeId);
        HbzWareTypeDTO type = hbzWareTypeService.get(query);
        String typeNo = type.getTypeNo();
        int a = 1;
        while (true) {
            String no = typeNo + "-" + StringHelper.frontCompWithZore(a++, 6);
            if (hbzWareInfoRepository.countByWareNo(no).longValue() == 0L) {
                return no;
            }
        }
    }

}
