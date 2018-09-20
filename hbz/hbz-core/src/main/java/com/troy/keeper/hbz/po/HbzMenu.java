package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.RequireAuthEnum;
import com.troy.keeper.hbz.type.WebModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * web端 菜单 实体类
 * Created by YangJx 1017-11-9
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "hbz_menu")
public class HbzMenu extends BaseVersionLocked {

    /**
     * 父菜单
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_id", columnDefinition = "bigint comment '父节点'")
    private HbzMenu parent;

    /**
     * 菜单名称
     */
    @Column(name = "name", columnDefinition = "varchar(100) comment '菜单名称'")
    private String name;

    /**
     * 菜单链接地址
     */
    @Column(name = "url", columnDefinition = "varchar(200) comment '菜单链接地址'")
    private String url;

    /**
     * 菜单图标链接地址
     */
    @Column(name = "icon_url", columnDefinition = "varchar(200) comment '菜单图标链接地址'")
    private String iconUrl;

    /**
     * 菜单排序号
     */
    @Column(name = "order_code", columnDefinition = "comment '排序编号'")
    private Integer orderCode;

    /**
     * 是否为叶子节点，0：否，1：是
     */
    @Column(name = "if_leaf", columnDefinition = "comment '是否为叶子节点，0：否，1：是'")
    private Integer ifLeaf;

    /**
     * 所属模块代码（对应前台“我是货主：consignorEnterprise、我是车主：transportEnterprise、个人中心：personalCenter”三个模块）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "web_module")
    private WebModule webModule;

    /**
     * 多对多角色关联
     */
    @ManyToMany
    @JoinTable(name = "hbz_role_menu", joinColumns = {@JoinColumn(name = "menu", columnDefinition = "bigint comment '菜单id'")}, inverseJoinColumns = {@JoinColumn(name = "role", columnDefinition = "bigint comment '角色id'")})
    private List<HbzRole> hbzRoleList;

    /**
     * 是否需要认证
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) comment '是否需要认证，YES：需要,NO：不需要'")
    private RequireAuthEnum requireAuth;

}