package com.troy.keeper.hbz.po;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leecheng on 2017/10/13.
 */

//权限表
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hbz_auth")
public class HbzAuth extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(100) COMMENT '权限名'")
    private String authName;

    @Column(name = "stas")
    String state;

    @Lob
    @Column(name = "details", columnDefinition = "longtext COMMENT '权限详细'")
    private String details;

    @ManyToMany
    @JoinTable(name = "hbz_role_auth", joinColumns = {@JoinColumn(name = "auth", columnDefinition = "bigint comment '权限id'")}, inverseJoinColumns = {@JoinColumn(name = "role", columnDefinition = "bigint comment '角色id'")})
    private List<HbzRole> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "hbz_url_auth", joinColumns = @JoinColumn(name = "auth"), inverseJoinColumns = @JoinColumn(name = "url"))
    private List<HbzUrl> urls;

    @Column(columnDefinition = "longtext comment '备注'")
    String comments;

}
