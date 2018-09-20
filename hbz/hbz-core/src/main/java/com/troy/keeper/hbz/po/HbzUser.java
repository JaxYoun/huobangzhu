package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.Sex;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * Created by leecheng on 2017/10/12.
 */
//账号
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hbz_user")
public class HbzUser extends BaseVersionLocked {

    @Column(length = 50, unique = true, nullable = false, columnDefinition = "varchar(32) comment '用户名'")
    private String login;

    @Column(nullable = false, columnDefinition = "varchar(32) comment '昵称'")
    private String nickName;

    @Column(nullable = false, name = "password", columnDefinition = "varchar(32) comment '密码'")
    private String password;

    @Column(name = "starLevel", columnDefinition = "int comment '货运方星级'")
    private Integer starLevel;

    @Column(name = "userStarLevel", columnDefinition = "int comment '货主身份星级'")
    private Integer userStarLevel;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean activated;

    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Column(name = "activation_key", length = 20)
    private String activationKey;

    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @Column(columnDefinition = "int comment '货运方积分'")
    private Integer score;

    @Column(columnDefinition = "int comment '货主方积分'")
    private Integer userScore;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(8) comment '性别'")
    private Sex sex;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(64) comment '电话'")
    private String telephone;

    @ManyToOne
    @JoinColumn(name = "org", nullable = true, columnDefinition = "bigint comment '所属机构'")
    private HbzOrg org;

    @ManyToOne
    @JoinColumn(name = "ent", nullable = true, columnDefinition = "bigint comment '所属企业'")
    private HbzOrg ent;

    @ManyToMany
    @JoinTable(name = "hbz_user_role", joinColumns = {@JoinColumn(name = "user", columnDefinition = "bigint comment '用户id'")}, inverseJoinColumns = {@JoinColumn(name = "role", columnDefinition = "bigint comment '角色id'")})
    private List<HbzRole> roles;

    @Column(columnDefinition = "longtext comment '备注'")
    private String comments;

}
