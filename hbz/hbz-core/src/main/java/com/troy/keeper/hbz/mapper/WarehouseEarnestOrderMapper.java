package com.troy.keeper.hbz.mapper;

import com.troy.keeper.hbz.dto.WarehouseEarnestOrderDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.vo.WarehouseEarnestOrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Author：YangJx
 * @Description：仓储租赁订单mapper
 * @DateTime：2018/1/3 17:18
 */
@Mapper(componentModel = "spring", uses = {MyHbzUserMapper.class, DateUtils.class, WarehouseMapper.class})
public interface WarehouseEarnestOrderMapper {

    @Mappings({
            @Mapping(source = "lastUpdatedDate", target = "lastUpdatedDate", qualifiedByName = {"DateMapper", "noSecondStringToLong"}),
            @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = {"DateMapper", "noSecondStringToLong"}),
            @Mapping(source = "warehouse", target = "warehouse", qualifiedByName = {"WarehouseMapper", "dtoToEntity"}),
            @Mapping(source = "dealUser", target = "dealUser", qualifiedByName = {"MyHbzUserMapper", "dtoToEntity"}),
            @Mapping(source = "createUser", target = "createUser", qualifiedByName = {"MyHbzUserMapper", "dtoToEntity"})
    })
    WarehouseEarnestOrder dtoToEntity(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO);

    @Mappings({
            @Mapping(source = "lastUpdatedDate", target = "lastUpdateDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "createdDate", target = "createDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "warehouse", target = "warehouse", qualifiedByName = {"WarehouseMapper", "entityToVo"}),
            @Mapping(source = "dealUser", target = "dealUser", qualifiedByName = {"MyHbzUserMapper", "entityToVo"}),
            @Mapping(source = "createUser", target = "createUser", qualifiedByName = {"MyHbzUserMapper", "entityToVo"})
    })
    WarehouseEarnestOrderVO entityToVo(WarehouseEarnestOrder warehouseEarnestOrder);

}
