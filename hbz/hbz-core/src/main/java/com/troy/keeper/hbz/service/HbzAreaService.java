package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.po.HbzArea;

import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
public interface HbzAreaService extends BaseEntityService<HbzArea, HbzAreaDTO> {

    HbzAreaDTO findByOutCode(String outCode);

    /**
     * 根据行政区域名称(中文)查询区域对象
     *
     * @param fullChineseName 格式如：省-市-县
     * @return
     */
    HbzAreaDTO findCountyByAreaName(String fullChineseName);

    /**
     * 根据父区域获取子区域列表
     *
     * @param parentHbzAreaDTO
     * @return
     */
    List<HbzAreaDTO> getAreaByParent(HbzAreaDTO parentHbzAreaDTO);

}
