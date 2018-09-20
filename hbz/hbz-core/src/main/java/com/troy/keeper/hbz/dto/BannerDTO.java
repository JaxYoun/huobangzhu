package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.Const;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/16 16:20
 */
@Data
public class BannerDTO extends BaseDTO {

    private String code;

    @NotBlank(message = "名称为必填项")
    @Length(min = 1, max = 100, message = "名称长度在[1, 100]之间")
    private String name;

    @NotBlank(message = "位置为必填项")
    private String location;

    @NotBlank(message = "跳转类型为必填项")
    private String skipType;

    @NotNull(message = "排序编号为必填项")
    private Integer sortNo;

    @NotBlank(message = "跳转url为必填项")
    @Length(max = 200)
    private String url;

    private String ifEnable;

    @NotBlank(message = "必须上传图片")
    @Length(max = 200)
    private String imagePath;

    @Length(max = 2000, message = "备注长度不得超过1000")
    private String remark;

}