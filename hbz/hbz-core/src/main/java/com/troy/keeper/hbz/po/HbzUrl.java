package com.troy.keeper.hbz.po;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leecheng on 2017/10/11.
 */
//访问URL
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hbz_url")
public class HbzUrl extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(128) comment '资源标签'")
    String urlLabel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "hbz_url_auth", joinColumns = @JoinColumn(name = "url"), inverseJoinColumns = @JoinColumn(name = "auth"))
    private List<HbzAuth> auths;

    @Column(name = "urlPattern", columnDefinition = "varchar(1000) comment 'ANT URL表达式'")
    private String urlPattern;

    @Column(columnDefinition = "varchar(128) comment '包名'")
    private String pack;

    @Column(name = "s")
    private String state;

    @Column(columnDefinition = "longtext comment '备注'")
    private String comments;
}
