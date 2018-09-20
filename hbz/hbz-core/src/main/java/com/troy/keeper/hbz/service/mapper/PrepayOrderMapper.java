package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.dto.PrepayOrderDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.PrepayOrder;
import com.troy.keeper.hbz.vo.PrepayOrderVO;
import org.mapstruct.Mapper;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/29 0:34
 */
@Mapper(componentModel = "spring", uses = {DateUtils.class, HbzUserMapper.class,})
public interface PrepayOrderMapper {

    /*@Mappings({
            @Mapping(source = "createUser", target = "createUser", qualifiedByName = {"HbzUserMapper", "entityToVo"}),
            @Mapping(source = "lastUpdatedDate", target = "lastUpdateDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "createdDate", target = "createDate", qualifiedByName = {"DateMapper", "longToNoSecondString"})
    })*/
    PrepayOrder dtoToEntity(PrepayOrderDTO prepayOrderDTO);

    /*@Mappings({
            @Mapping(source = "createUser", target = "createUser", qualifiedByName = {"HbzUserMapper", "entityToVo"}),
            @Mapping(source = "lastUpdatedDate", target = "lastUpdateDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "createdDate", target = "createDate", qualifiedByName = {"DateMapper", "longToNoSecondString"})
    })*/
    PrepayOrderVO entityToVo(PrepayOrder prepayOrder);

}
