package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.po.BaseVersionLocked;
import lombok.Getter;
import lombok.Setter;

/**
 * @Autohor: hecj
 * @Description: 移动端版本管理实体类DTO
 * @Date: Created in 14:45  2018/2/1.
 * @Midified By:
 */
@Getter
@Setter
public class HbzVersionManagementDTO extends BaseVersionLocked {
    /*//id值
    private Long id;*/
    //创建人
    private String userName;
    //版本号
    private String versionNo;
    //版本名称
    private String versionName;
    //时间
    private Long uploadDate;
    //文件路径
    private String fileUrl;
    //描述信息
    private String recordComment;
    //版本状态
    private String status;
    //版本类别
    private String type;
    //是否禁用
    private String isDisable;
}
