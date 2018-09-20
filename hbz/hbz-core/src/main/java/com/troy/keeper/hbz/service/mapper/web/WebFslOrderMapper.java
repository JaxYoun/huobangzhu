package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.HbzFslOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {DateUtils.class})
public interface WebFslOrderMapper {

    WebFslOrderMapper INSTANCE = Mappers.getMapper(WebFslOrderMapper.class);

    @Mappings({
            @Mapping(source = "orderTakeStart", target = "orderTakeStart", qualifiedByName = {"DateMapper", "longToNoSecondString"})
    })
    WebFslOrderVO entityToVO(WebFslOrder webFslOrder);

    @Mappings({
            @Mapping(source = "orderTakeStart", target = "orderTakeStart", qualifiedByName = {"DateMapper", "noSecondStringToLong"})
    })
    WebFslOrder dtoToEntity(WebFslOrderDTO webFslOrderDTO);

}


