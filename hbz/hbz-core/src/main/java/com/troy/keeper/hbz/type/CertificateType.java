package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/25.
 */
@AllArgsConstructor
@Getter
public enum CertificateType {

    ID("身份证"), Driver("驾驶证"), Business("营业照");

    private String name;


}
