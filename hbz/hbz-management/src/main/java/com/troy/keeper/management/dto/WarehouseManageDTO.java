package com.troy.keeper.management.dto;


import com.troy.keeper.hbz.dto.HbzAreaDTO;
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
public class WarehouseManageDTO {
    //主键id
    private Long id;
    //仓库名字
    private String name;
    //发布时间
    private Long createdDate;
    //仓库状态 00 未审核 01 可用 02过期 03失效
    private String lifecycle;
    //仓库状态值 00 未审核 01 可用 02过期 03失效
    private String lifecycleValue;
    //审核人
    private String createUser;
    //审核时间
    private Long checkedDate;
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
    //审核备注
    private String recordComment;
    //区域名字
    private String originAreaName;
    //仓储归属地ID 查询使用
    private String originAreaId;
    //查询使用
    private Long createdDateStart;//新建开始时间
    private Long createdDateEnd;//新建结束时间

}
