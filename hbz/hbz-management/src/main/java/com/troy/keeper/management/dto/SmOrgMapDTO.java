package com.troy.keeper.management.dto;

import lombok.Data;

/**
 * Created by leecheng on 2018/1/17.
 */
@Data
public class SmOrgMapDTO extends HbzBaseMapDTO {

    private Long id;

    private Long pId;
    private Long parentId;

    private String relationship;

    private String orgName;

    private Long orderCode;

}
