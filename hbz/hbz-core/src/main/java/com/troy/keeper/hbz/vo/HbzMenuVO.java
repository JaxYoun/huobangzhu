package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.type.RequireAuthEnum;
import com.troy.keeper.hbz.type.WebModule;
import lombok.Data;

@Data
public class HbzMenuVO extends BaseDTO {

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单链接地址
     */
    private String url;

    /**
     * 菜单图标链接地址
     */
    private String iconUrl;

    /**
     * 菜单排序号
     */
    private Integer orderCode;

    /**
     * 是否为叶子节点，0：否，1：是
     */
    private Integer ifLeaf;

    /**
     * 所属模块编码
     */
    private WebModule webModule;

    /**
     * 是否需要认证
     */
    private RequireAuthEnum requireAuth;

}
