package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 后台平台新闻DTO
 * @Date: Created in 14:14  2018/1/10.
 * @Midified By:
 */

@Getter
@Setter
public class NewsDTO{
    //主键id
    private Long id;
    //信息编号
    private String newsNo;
    //标题
    private String title;
    //内容
    private String content;
    //发布时间
    private Long publishDate;
    //查询使用
    private Long publishDateStart;
    private Long publishDateEnd;
    //标题图片
    private String titleImageList;
    //新闻类型
    private String newsType;
    //新闻类型值
    private String newsTypeValue;
    //是否显示发布时间，0：否，1：是
    private Integer displayPublishDate;
    //新闻简介
    private String summary;
    //展示使用标题图片
    //private List<String> OutTitleImageList;
    //备注
    private String recordComment;
    //信息状态 0 停用 1可用
    private String newsStatus;
}

