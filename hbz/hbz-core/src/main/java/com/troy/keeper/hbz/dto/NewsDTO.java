package com.troy.keeper.hbz.dto;

import lombok.*;

import javax.persistence.Column;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/26 9:10
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO extends BaseDTO {

    private String title;

    private String content;

    //private HbzUserDTO createUser;

    private String createDate;

    private String lastUpdateDate;

    private String titleImageList;

    private String contentImageList;

    private String newsType;

    private Integer displayPublishDate;

    //创建条件
    private String createDateStart;
    private String createDateEnd;

    //更新时间
    private String updateDateStart;
    private String updateDateEnd;

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getUpdateDateStart() {
        return updateDateStart;
    }

    public void setUpdateDateStart(String updateDateStart) {
        this.updateDateStart = updateDateStart;
    }

    public String getUpdateDateEnd() {
        return updateDateEnd;
    }

    public void setUpdateDateEnd(String updateDateEnd) {
        this.updateDateEnd = updateDateEnd;
    }

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

    //public HbzUserDTO getCreateUser() {
    //    return createUser;
    //}
    //
    //public void setCreateUser(HbzUserDTO createUser) {
    //    this.createUser = createUser;
    //}

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getTitleImageList() {
        return titleImageList;
    }

    public void setTitleImageList(String titleImageList) {
        this.titleImageList = titleImageList;
    }

    public String getContentImageList() {
        return contentImageList;
    }

    public void setContentImageList(String contentImageList) {
        this.contentImageList = contentImageList;
    }
}
