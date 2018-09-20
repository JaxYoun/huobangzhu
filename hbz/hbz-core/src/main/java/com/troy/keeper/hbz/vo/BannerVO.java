package com.troy.keeper.hbz.vo;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：Banner之VO类
 * @DateTime：2018/1/16 17:16
 */
@Data
public class BannerVO extends BasicVO {

    private String code;

    private String name;

    private DictionaryVO location;

    private DictionaryVO skipType;

    private Integer sortNo;

    private String url;

    private String ifEnable;

    private String imagePath;

    private String remark;

}