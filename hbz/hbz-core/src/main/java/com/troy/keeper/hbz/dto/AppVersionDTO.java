package com.troy.keeper.hbz.dto;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/11 11:34
 */
@Data
public class AppVersionDTO extends BaseDTO {

    private String versionNo;

    private String versionName;

    private String fileUrl;

    private String recordComment;

    private String type;

    private String isDisable;

}
