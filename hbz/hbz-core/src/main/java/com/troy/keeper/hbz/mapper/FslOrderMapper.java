package com.troy.keeper.hbz.mapper;

import com.troy.keeper.hbz.dto.HbzFslOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.EnumUtils;
import com.troy.keeper.hbz.vo.excel.FslOrderExcelVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/9 11:14
 */
//@Mapper(componentModel = "spring", uses = {EnumUtils.class, DateUtils.class})
public interface FslOrderMapper {

    /*@Mappings({
            @Mapping(target = "commodityType", expression = "java(hbzFslOrderDTO.getCommodityType.getName())"),
            @Mapping(target = "weightUnit", expression = "java(hbzFslOrderDTO.getWeightUnit.getName())")
            *//*@Mapping(source = "", target = "", qualifiedByName = {"", ""}),
            @Mapping(source = "", target = "", qualifiedByName = {"", ""}),
            @Mapping(source = "", target = "", qualifiedByName = {"", ""}),
            @Mapping(source = "", target = "", qualifiedByName = {"", ""}),*//*
    })
    FslOrderExcelVO dtoToExcelVo(HbzFslOrderDTO hbzFslOrderDTO);*/

}
