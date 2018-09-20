package com.troy.keeper.hbz.mapper;

import com.troy.keeper.hbz.dto.NewsDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.JsonArrToListMapper;
import com.troy.keeper.hbz.po.News;
import com.troy.keeper.hbz.vo.NewsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Author：YangJx
 * @Description：资讯Mapper
 * @DateTime：2017/12/26 11:39
 */
@Mapper(componentModel = "spring", uses = {MyHbzUserMapper.class, DateUtils.class, JsonArrToListMapper.class})
public interface NewsMapper {

    @Mappings({
            //@Mapping(source = "createUser", target = "createUser", qualifiedByName = {"MyHbzUserMapper", "dtoToEntity"}),
            @Mapping(source = "lastUpdateDate", target = "lastUpdatedDate", qualifiedByName = {"DateMapper", "noSecondStringToLong"}),
            @Mapping(source = "createDate", target = "createdDate", qualifiedByName = {"DateMapper", "noSecondStringToLong"})
    })
    News dtoToEntity(NewsDTO newsDTO);

    @Mappings({
            //@Mapping(source = "createUser", target = "createUser", qualifiedByName = {"MyHbzUserMapper", "entityToVo"}),
            @Mapping(source = "lastUpdatedDate", target = "lastUpdateDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            @Mapping(source = "createdDate", target = "createDate", qualifiedByName = {"DateMapper", "longToNoSecondString"}),
            /*@Mapping(source = "titleImageList", target = "titleImageList", qualifiedByName = {"JsonArrToListMapper", "platArrToList"}),
            @Mapping(source = "contentImageList", target = "contentImageList", qualifiedByName = {"JsonArrToListMapper", "platArrToList"}),*/
            @Mapping(source = "titleImageList", target = "titleImageList", ignore = true)
//            @Mapping(source = "contentImageList", target = "contentImageList", ignore = true)
    })
    NewsVO entityToVo(News news);


}
