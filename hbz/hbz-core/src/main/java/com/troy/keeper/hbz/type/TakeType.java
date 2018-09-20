package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/21.
 */
@AllArgsConstructor
@Getter
public enum TakeType {

    TAKEING("征集中"), TAKE("完成征集"), DISABLE("排除征集");

    private String name;

}
