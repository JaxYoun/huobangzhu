package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/19.
 */
@Data
public class HbzScoreDTO extends BaseDTO {

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    @QueryColumn
    private Double score;

}
