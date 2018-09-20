package com.troy.keeper.hbz.vo;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/30 14:27
 */
@Data
public class AgreementTemplateVO extends BasicVO {

    private String name;

    private DictionaryVO type;

    private String description;

    private String isEnable;

    private String attachPath;

    private String remark;

    private String code;

}
