package com.troy.keeper.hbz.dto;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 12:45
 */
@Data
public class SmLoginLogDTO extends BaseDTO {

    private Long userId;

    private String ip;

    private Integer loginType;

    private Long createDt;

    private Long logoutDt;

}
