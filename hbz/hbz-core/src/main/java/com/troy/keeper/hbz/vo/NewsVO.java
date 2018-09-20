package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.dto.BaseDTO;
import lombok.*;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/26 9:12
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsVO extends BaseDTO {

    private String title;

    private String content;

    private HbzUserVO createUser;

    private String createDate;

    private String lastUpdateDate;

    private String titleImageList;

    private List<String> contentImageList;

    private String newsType;

    private Integer displayPublishDate;

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

    public HbzUserVO getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUserVO createUser) {
        this.createUser = createUser;
    }

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

    public List<String> getContentImageList() {
        return contentImageList;
    }

    public void setContentImageList(List<String> contentImageList) {
        this.contentImageList = contentImageList;
    }
}
