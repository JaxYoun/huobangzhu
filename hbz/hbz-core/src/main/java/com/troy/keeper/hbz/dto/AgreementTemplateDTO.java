package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.po.BaseVersionLocked;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/30 14:19
 */
@Data
public class AgreementTemplateDTO extends BaseDTO {

    @NotBlank(message = "模板名称为必填项")
    @Length(min = 1, max = 200, message = "模板名称长度必须在[1, 200]间")
    private String name;

    @NotBlank(message = "模板类型为必填项")
    @Length(min = 1, max = 2, message = "模板类型长度必须在[1, 2]间")
    private String type;

    @NotBlank(message = "模板描述为必填项")
    @Length(min = 1, max = 200, message = "模板描述长度必须在[1, 200]间")
    private String description;

    private String isEnable;

    @NotBlank(message = "附件路径为必填项")
    @Length(min = 1, max = 300, message = "附件路径长度在[1, 300]间")
    private String attachPath;

    @NotBlank(message = "备注为必填项")
    @Length(min = 1, max = 200, message = "备注长度在[1, 200]间")
    private String remark;

    private Long createDateStart;

    private Long createDateEnd;

    private String code;

}
