package com.troy.keeper.hbz.mapper;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.vo.HbzUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MyHbzUserMapper {

    MyHbzUserMapper INSTANCE = Mappers.getMapper(MyHbzUserMapper.class);

    HbzUser dtoToEntity(HbzUserDTO dto);

    HbzUserVO entityToVo(HbzUser entity);

}
