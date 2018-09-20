package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by leecheng on 2017/10/30.
 */
@AllArgsConstructor
@Getter
public enum OrgType {

    Enterprise("企业"),EnDepartment("部门");

    private String name;

}
