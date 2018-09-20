package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.type.TransType;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/6.
 */
@Data
public class HbzDriverLineMapDto extends HbzBaseMapDTO {

    private Long id;

    private HbzUserDTO user;

    private HbzAreaDTO originArea;
    private Long originAreaId;
    private String originAreaCode;

    private HbzAreaDTO destArea;
    private Long destAreaId;
    private String destAreaCode;

    private TransType transType;

    private Double maxLoad;

    private Double unitPrices;

    private List<Double> transSizes;

    private Double transSize;
    private Double transSizeLT;
    private Double transSizeLE;
    private Double transSizeGT;
    private Double transSizeGE;

}
