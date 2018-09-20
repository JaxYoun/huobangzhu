package com.troy.keeper.hbz.app.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/10/26.
 */
@Data
public class EnumQueryDTO {

    private String enumname;

    private List<String> excludeType;

}
