package com.troy.keeper.hbz.vo;

import com.troy.keeper.core.base.entity.BaseEntity;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 12:52
 */
@Data
public class SmLoginLogVO extends BaseEntity {

    private Long userId;

    private String ip;

    private Integer loginType;

    private Long createDt;

    private Long logoutDt;

}
