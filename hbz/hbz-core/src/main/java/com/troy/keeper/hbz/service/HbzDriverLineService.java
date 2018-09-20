package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzDriverLineDTO;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.po.HbzDriverLine;

import java.util.List;

/**
 * Created by leecheng on 2017/12/6.
 */
public interface HbzDriverLineService extends BaseEntityService<HbzDriverLine, HbzDriverLineDTO> {

    boolean bindTransSizes(HbzDriverLineDTO hbzDriverLineDTO, List<HbzTransSizeDTO> transSizeDTOS);

    List<HbzTransSizeDTO> queryTransSizes(HbzDriverLineDTO hbzDriverLineDTO);

    /**
     * 查询运力专线详情
     * @param hbzDriverLineDTO
     * @return
     */
    HbzDriverLineDTO queryDriverLineDetail(HbzDriverLineDTO hbzDriverLineDTO);

    /**
     * 导入单条专线运力
     * @param hbzDriverLineDTO
     * @return
     */
    HbzDriverLineDTO importOne(HbzDriverLineDTO hbzDriverLineDTO);
}
