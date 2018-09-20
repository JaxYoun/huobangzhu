package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/12.
 */
@Setter
@Getter
public class EnumQueryDTO {

    private String enumname;

    private List<String> excludeType;

}
