package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 李奥
 * @date 2018/1/10.
 */
@AllArgsConstructor
@Getter
public enum IsUnload {

    PARTUNLOADING("部分卸货"),
    ALLUNLOADING("全部卸货"),
    NOTUNLOADED("未卸货");



    private String name;


}
