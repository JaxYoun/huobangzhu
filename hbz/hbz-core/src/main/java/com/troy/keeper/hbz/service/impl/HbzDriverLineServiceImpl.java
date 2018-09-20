package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzDriverLineDTO;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.po.HbzDriverLine;
import com.troy.keeper.hbz.po.HbzTransSize;
import com.troy.keeper.hbz.repository.HbzDriverLineRepository;
import com.troy.keeper.hbz.repository.HbzTransSizeRepository;
import com.troy.keeper.hbz.service.HbzDriverLineService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzDriverLineMapper;
import com.troy.keeper.hbz.service.mapper.HbzTransSizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/6.
 */
@Service
@Transactional
public class HbzDriverLineServiceImpl extends BaseEntityServiceImpl<HbzDriverLine, HbzDriverLineDTO> implements HbzDriverLineService {

    @Autowired
    HbzDriverLineRepository hbzDriverLineRepository;

    @Autowired
    HbzDriverLineMapper hbzDriverLineMapper;

    @Autowired
    HbzTransSizeRepository hbzTransSizeRepository;

    @Autowired
    HbzTransSizeMapper hbzTransSizeMapper;

    @Override
    public BaseMapper<HbzDriverLine, HbzDriverLineDTO> getMapper() {
        return hbzDriverLineMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzDriverLineRepository;
    }

    @Override
    public boolean bindTransSizes(HbzDriverLineDTO hbzDriverLineDTO, List<HbzTransSizeDTO> transSizeDTOS) {
        HbzDriverLine driverLine = hbzDriverLineRepository.findOne(hbzDriverLineDTO.getId());
        driverLine.setTransSizes(transSizeDTOS.stream().map(HbzTransSizeDTO::getId).map(hbzTransSizeRepository::findOne).collect(Collectors.toList()));
        return !(hbzDriverLineRepository.save(driverLine) == null);
    }

    @Override
    public List<HbzTransSizeDTO> queryTransSizes(HbzDriverLineDTO hbzDriverLineDTO) {
        HbzDriverLine driverLine = hbzDriverLineRepository.findOne(hbzDriverLineDTO.getId());
        List<HbzTransSize> transSizes = driverLine.getTransSizes();
        if (transSizes != null)
            return transSizes.stream().map(hbzTransSizeMapper::map).collect(Collectors.toList());
        return null;
    }

    @Override
    public HbzDriverLineDTO queryDriverLineDetail(HbzDriverLineDTO hbzDriverLineDTO) {
        HbzDriverLine hbzDriverLine = this.hbzDriverLineRepository.findOne(hbzDriverLineDTO.getId());
        HbzDriverLineDTO hbzDriverLineDTO1 = new HbzDriverLineDTO();
        if (hbzDriverLine == null) {
            return null;
        }
        hbzDriverLineMapper.entity2dto(hbzDriverLine, hbzDriverLineDTO1);
        return hbzDriverLineDTO1;
    }

    @Override
    @Transactional
    public HbzDriverLineDTO importOne(HbzDriverLineDTO hbzDriverLineDTO) {
        HbzDriverLine hbzDriverLine = new HbzDriverLine();
        this.hbzDriverLineMapper.dto2entity(hbzDriverLineDTO, hbzDriverLine);
        HbzDriverLine hbzDriverLineFromDB = this.hbzDriverLineRepository.save(hbzDriverLine);  //单纯插入专线并返回专线对象

        HbzDriverLineDTO hbzDriverLineDTOFromDB = new HbzDriverLineDTO();
        this.hbzDriverLineMapper.entity2dto(hbzDriverLineFromDB, hbzDriverLineDTOFromDB);  //将返回专线对象转为DTO

        //绑定若干车辆长度
        if (hbzDriverLineFromDB != null && hbzDriverLineDTO.getTransSizes() != null && hbzDriverLineDTO.getTransSizes().size() > 0) {
            List<HbzTransSizeDTO> transSizes = hbzDriverLineDTO.getTransSizes()
                    .stream()
                    .map(it -> hbzTransSizeMapper.map(hbzTransSizeRepository.findByTransSize(it)))
                    .filter(ele -> ele != null)
                    .collect(Collectors.toList());
            this.bindTransSizes(hbzDriverLineDTOFromDB, transSizes);
        }
        return hbzDriverLineDTOFromDB;
    }
}
