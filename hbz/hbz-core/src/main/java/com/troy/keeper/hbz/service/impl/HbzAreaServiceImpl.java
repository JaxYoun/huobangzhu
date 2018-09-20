package com.troy.keeper.hbz.service.impl;

import com.google.common.collect.Lists;
import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/10.
 */
@Service
@Transactional
public class HbzAreaServiceImpl extends BaseEntityServiceImpl<HbzArea, HbzAreaDTO> implements HbzAreaService {

    @Autowired
    private HbzAreaRepository hbzAreaRepository;

    @Autowired
    private HbzAreaMapper hbzAreaMapper;

    @Override
    public BaseMapper<HbzArea, HbzAreaDTO> getMapper() {
        return hbzAreaMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzAreaRepository;
    }

    @Override
    public HbzAreaDTO findByOutCode(String outCode) {
        return getMapper().map(hbzAreaRepository.findByOutCode(outCode));
    }

    @Override
    public HbzAreaDTO findCountyByAreaName(String fullChineseName) {
        String[] nameArr = fullChineseName.split("-");

        //当通过“-”截取后，子串少于2，则表明填写错误
        if (nameArr.length < 2) {
            return null;
        }

        List<String> reversedNameList = Lists.reverse(Arrays.asList(nameArr));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("areaName =", reversedNameList.get(0));
        paramMap.put("parent.areaName =", reversedNameList.get(1));

        List<HbzAreaDTO> areaDTOList = query(paramMap);
        if (areaDTOList.size() <= 0) {
            return null;
        }
        return areaDTOList.get(0);
    }

    /**
     * 根据父区域获取子区域列表
     *
     * @param parentHbzAreaDTO
     * @return
     */
    @Override
    public List<HbzAreaDTO> getAreaByParent(HbzAreaDTO parentHbzAreaDTO) {

        /*Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parent.id =", parentHbzAreaDTO.getId());
        List<HbzAreaDTO> areaDTOList = query(paramMap);
        return areaDTOList;*/

        return this.hbzAreaRepository
                .getAreaByParentId(parentHbzAreaDTO.getId())
                .stream()
                .map(getMapper()::map)
                .collect(Collectors.toList());
    }

}
