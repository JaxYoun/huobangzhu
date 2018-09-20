package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.OutsourcingGoodsDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author 李奥
 * @date 2018/1/15.
 */
public interface OutsourcingGoodsService {

    //分页   司机确认
    public Page<UserInformationDTO> findByCondition(UserInformationDTO userInformationDTO, Pageable pageable);


    //批量保存分包货物信息
    public Boolean addBatchOutCommodity(OutsourcingGoodsDTO outsourcingGoodsDTO);


    //外包发车单分页查询
    public Page<OutsourcingGoodsDTO> findByOutsourcingGoods(OutsourcingGoodsDTO outsourcingGoodsDTO, Pageable pageable);


    //点击车辆信息中的 编辑OutsourcingDetailsDTO
    public OutsourcingGoodsDTO  selectUpdateOutsourcingGoods(OutsourcingGoodsDTO outsourcingGoodsDTO);

    //新建状态下  编辑初始分包用户信息 保存功能
    public  Boolean addNewBatchOutCommodity(OutsourcingGoodsDTO outsourcingGoodsDTO);


    //点击编辑备注
    public Boolean addRemarks(OutsourcingGoodsDTO outsourcingGoodsDTO);


    //点击发车确认
    public  Boolean saveStartCarIsTrue(OutsourcingGoodsDTO outsourcingGoodsDTO);

    //点击收货确认
    public Boolean saveConfirmationIsTrue(OutsourcingGoodsDTO outsourcingGoodsDTO);

    //外包发车单导出
    public List<OutsourcingGoodsDTO> outsourcingGoodsExport(OutsourcingGoodsDTO outsourcingGoodsDTO);

    //删除外包发车单
    public Boolean deleteOutInformation(OutsourcingGoodsDTO outsourcingGoodsDTO);



}
