package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/20.
 */
@Getter
@Setter
public class HbzAccountFreightDTO extends BaseDTO {

    private HbzUserDTO user;
    @QueryColumn(propName = "user.id")
    private Long userId;

    private HbzUserDTO host;
    @QueryColumn(propName = "host.id")
    private Long hostId;
    @QueryColumn
    private Integer index;
}
