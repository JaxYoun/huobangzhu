package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leecheng on 2017/10/13.
 */
//角色
@Getter
@Setter
@Entity
@Table(name = "hbz_role")
public class HbzRole extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(32) comment '角色类型'")
    private Role role;

    @Column(name = "roleName", columnDefinition = "varchar(255) comment '角色名称'")
    private String roleName;

    @Column(name = "s", columnDefinition = "varchar(32) comment '状态'")
    String state;

    @Column(columnDefinition = "varchar(100) comment '标注'")
    String comments;

    @ManyToMany
    @JoinTable(name = "hbz_user_role", joinColumns = {@JoinColumn(name = "role", columnDefinition = "bigint comment '角色id'")}, inverseJoinColumns = {@JoinColumn(name = "user", columnDefinition = "bigint comment '用户id'")})
    private List<HbzUser> users;

    @ManyToMany
    @JoinTable(name = "hbz_role_auth", joinColumns = {@JoinColumn(name = "role", columnDefinition = "bigint comment '角色id'")}, inverseJoinColumns = {@JoinColumn(name = "auth", columnDefinition = "bigint comment '权限id'")})
    private List<HbzAuth> auths;

    @ManyToMany
    @JoinTable(name = "hbz_role_menu", inverseJoinColumns = {@JoinColumn(name = "menu", columnDefinition = "bigint comment '菜单id'")}, joinColumns = {@JoinColumn(name = "role", columnDefinition = "bigint comment '角色id'")})
    private List<HbzMenu> menus;

}
