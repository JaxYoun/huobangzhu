package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Autohor: hecj
 * @Description: 移动端版本管理实体类
 * @Date: Created in 11:20  2018/2/1.
 * @Midified By:
 */
@Setter
@Getter
@Entity
@Table(name = "hbz_version_management")
public class HbzVersionManagement extends BaseVersionLocked {
    //创建人
    @Column(name = "sm_user_id",columnDefinition = "bigint COMMENT '用户id'")
    private Long smUserId;
    //版本号
    @Column(name = "version_no", columnDefinition = "varchar(32) comment '上传版本号'")
    private String versionNo;
    //版本名称
    @Column(name = "version_name", columnDefinition = "varchar(32) comment '版本名称'")
    private String versionName;
    //时间
    @Column(name = "upload_date", columnDefinition = "bigint comment '上传时间'")
    private Long uploadDate;
    //文件路径
    @Column(name = "file_url", columnDefinition = "varchar(200) comment '文件路径'")
    private String fileUrl;
    //描述信息
    @Column(name = "record_comment", columnDefinition = "varchar(200) comment '描述信息'")
    private String recordComment;
    //版本类别
    @Column(name = "type", columnDefinition = "varchar(2) comment '版本类别 0 ios 1 安卓 2 其他'")
    private String type;
    //是否禁用
    @Column(name = "isDisable", columnDefinition = "varchar(2) comment '版本类别 0 是 1 否'")
    private String isDisable;
}
