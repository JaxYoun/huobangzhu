package com.troy.keeper.hbz.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 10:40
 */
@Data
public class SitePushMessageDTO extends BaseDTO {

    private String code;

    @NotBlank(message = "标题为必填项")
    @Length(max = 200, message = "标题长度不得超过200")
    private String title;

    @NotBlank(message = "消息类型为必填项")
    @Length(max = 1, message = "消息类型长度不得超过1")
    private String messageType;

    @NotBlank(message = "接收者为必填项")
    @Length(max = 1, message = "接收者类型长度不得超过1")
    private String consumerType;

    private Long sendTime;

    private Long sendTimeStart;
    private Long sendTimeEnd;

    @Length(min = 11, max = 11, message = "手机号长度必须为11位")
    private String consumerPhoneNo;

    private String ifSend;

    @Length(max = 300, message = "必须上传图片")
    private String imagePath;

    @NotBlank(message = "消息内容长度不得超过1000")
    private String content;

    @Length(max = 1000, message = "备注长度不得超过1000")
    private String remark;

    @NotBlank(message = "接收平台类型为必填项")
    @Length(min = 1, max = 1, message = "接收平台类型长度为必填项")
    private String receivePlatformType;

    @NotBlank(message = "推送方式为必填项")
    @Length(min = 1, max = 1, message = "推送方式长度必须为1")
    private String pushType;

    @NotBlank(message = "消息摘要为必填项")
    @Length(min = 1, max = 200, message = "摘要长度不得在[1,200]间")
    private String summary;

}
