package com.troy.keeper.hbz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by leecheng on 2017/11/22.
 */
@Getter
@Setter
public class HbzTypeValDTO extends BaseDTO {

    private String type;

    private String val;

    private String name;

    private String language;

    private List<String> excludeValue;
}
