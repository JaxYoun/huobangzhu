package com.troy.keeper.hbz.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：YangJx
 * @Description：VO基础类
 * @DateTime：2018/1/17 14:13
 */
@Data
public class BasicVO implements Serializable {

    private Long id;

    private Long createdBy;

    private Long createdDate;

    private Long lastUpdatedBy;

    private Long lastUpdatedDate;

}