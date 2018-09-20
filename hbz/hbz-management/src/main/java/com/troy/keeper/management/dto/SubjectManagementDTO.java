package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2018/1/21.
 */
@Getter
@Setter
public class SubjectManagementDTO {

    private Long  id;


    //科目编码
    private  String  subjectCode;


    //科目名称
    private String  subjectName;


    //科目类型
    private String  subjectType;


    //科目状态
    private String  subjectStatus;


    //备注
    private  String  remarks;




}
