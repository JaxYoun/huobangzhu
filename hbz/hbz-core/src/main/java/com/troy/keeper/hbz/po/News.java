package com.troy.keeper.hbz.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @Author：YangJx
 * @Description：资讯实体类
 * @DateTime：2017/12/25 13:38
 */
@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hbz_news")
public class News extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(10) comment '资讯编号'")
    private String newsNo;

    @Column(columnDefinition = "varchar(64) comment '资讯标题'")
    private String title;

    @Column(columnDefinition = "longtext comment '资讯内容'")
    private String content;

    //@ManyToOne(fetch = FetchType.EAGER, optional = false)
    //private HbzUser createUser;

    @Column(columnDefinition = "bigint COMMENT '用户id'")
    private Long createSmUserId;//后台用户

    @Column(columnDefinition = "varchar(1000) comment '标题图片路径-数组'")
    private String titleImageList;

    //@Column(columnDefinition = "varchar(1000) comment '内容图片路径-数组格式'")
    //private String contentImageList;

    @Column(columnDefinition = "varchar(32) comment '资讯类型'")
    private String newsType;

    @Column(columnDefinition = "int comment '是否显示发布时间，0：否，1：是'")
    private Integer displayPublishDate;

    @Column(columnDefinition = "bigint comment '发布时间'")
    private Long publishDate;

    @Column(columnDefinition = "varchar(200) comment '新闻简介'")
    private String summary;

    @Column(columnDefinition = "varchar(200) comment '备注描述'")
    private String recordComment;

    @Column(columnDefinition = "varchar(32) comment '信息状态 0 停用 1可用'")
    private String newsStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //public HbzUser getCreateUser() {
    //    return createUser;
    //}
    //
    //public void setCreateUser(HbzUser createUser) {
    //    this.createUser = createUser;
    //}

    public String getTitleImageList() {
        return titleImageList;
    }

    public void setTitleImageList(String titleImageList) {
        this.titleImageList = titleImageList;
    }

    //public String getContentImageList() {
    //    return contentImageList;
    //}
    //
    //public void setContentImageList(String contentImageList) {
    //    this.contentImageList = contentImageList;
    //}

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public Integer getDisplayPublishDate() {
        return displayPublishDate;
    }

    public void setDisplayPublishDate(Integer displayPublishDate) {
        this.displayPublishDate = displayPublishDate;
    }
}