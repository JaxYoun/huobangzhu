package com.troy.keeper.hbz.vo;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/11 11:24
 */
@Data
public class AppVersionVO extends BasicVO {

    private String versionNo;

    private String versionName;

    private String fileUrl;

    private String recordComment;

    private String type;

    private String formatedCreatedDate;

    private String formatedLastUpdatedDate;

}
