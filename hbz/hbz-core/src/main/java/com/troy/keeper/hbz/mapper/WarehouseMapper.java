package com.troy.keeper.hbz.mapper;

import com.troy.keeper.hbz.dto.WarehouseDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.JsonArrToListMapper;
import com.troy.keeper.hbz.po.Warehouse;
import com.troy.keeper.hbz.vo.WarehouseVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Author：YangJx
 * @Description：仓储mapper
 * @DateTime：2018/1/5 9:16
 */
@Mapper(componentModel = "spring", uses = {DateUtils.class, MyHbzUserMapper.class, JsonArrToListMapper.class})
public interface WarehouseMapper {

    @Mappings({
            @Mapping(source = "createUserDTO", target = "createUser", qualifiedByName = {"MyHbzUserMapper", "dtoToEntity"})
    })
    Warehouse dtoToEntity(WarehouseDTO warehouseDTO);

    @Mappings({
            @Mapping(source = "createUser", target = "createUserVO", qualifiedByName = {"MyHbzUserMapper", "entityToVo"}),
            @Mapping(source = "createdDate", target = "formatedCreateDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "lastUpdatedDate", target = "formatedLastModifiedDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "publishDate", target = "publishDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "titleImageList", target = "titleImageList", ignore = true),
            @Mapping(source = "contentImageList", target = "contentImageList", ignore = true)
    })
    WarehouseVO entityToVo(Warehouse warehouse);

}
