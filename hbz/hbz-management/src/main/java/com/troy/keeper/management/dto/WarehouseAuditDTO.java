package com.troy.keeper.management.dto;


import lombok.Getter;
import lombok.Setter;

/**
 * @Autohor: hecj
 * @Description: 仓储审核DTO
 * @Date: Created in 11:12  2018/1/9.
 * @Midified By:
 */
@Getter
@Setter
public class WarehouseAuditDTO {
    //主键id
    private Long id;
    //仓库名字
    private String name;
    //发布时间
    private Long publishDate;
    //审核时间
    private Long checkedDate;
    //审核备注
    private String recordComment;
    //审核状态 00 未审核 01 通过(不能被修改) 02不通过(不能被修改)
    private String type;
    //审核状态值
    //private String typeValue;
    //审核人
    private String createUser;
    //仓储容量
    private Double capacity;
    //租赁价格
    private Double unitPrice;
    //租赁时间
    private Integer minRentTime;
    //联系人
    private String ownerName;
    //联系电话
    private String telephone;
    //仓库地址
    private String address;
    //地址经度
    private Double coordX;
    //地址纬度
    private Double coordY;
    //仓储描述
    private String warehouseDescribe;
    //查询使用
    private Long publishDateStart;//发布开始时间
    private Long publishDateEnd;//发布结束时间

}
