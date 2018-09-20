package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/12.
 */
@Data
public class HbzSourceDTO extends BaseDTO {

    @QueryColumn
    private String label;

    @QueryColumn
    private String pack;

    @QueryColumn
    private String src;
}
