package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.export.ExcelHandleUtil;
import com.troy.keeper.hbz.helper.ExcelUtils;
import com.troy.keeper.management.dto.OutsourcingGoodsDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.service.OutsourcingGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 李奥   分包管理
 * @date 2018/1/15.
 */
@RestController
public class OutsourcingGoodsResource {

    @Autowired
    private OutsourcingGoodsService outsourcingGoodsService;



    //分包客户信息 分页查询
    @RequestMapping("/api/manager/outUserPage")
    public ResponseDTO onTheWayPage(@RequestBody UserInformationDTO userInformationDTO, Pageable pageable){
        return new ResponseDTO("200", "分包客户信息分页查询",outsourcingGoodsService.findByCondition(userInformationDTO,pageable));
    }



    //批量保存分包客户
    @RequestMapping("/api/manager/addBatchOutCommodity")
    public ResponseDTO addBatchOutCommodity(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){

        return new ResponseDTO("200", "批量保存分包客户",outsourcingGoodsService.addBatchOutCommodity(outsourcingGoodsDTO));
    }

    //分包货物分页查询
    @RequestMapping("/api/manager/selectOutsourcingGoodsPage")
    public ResponseDTO selectOutsourcingGoodsPage(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO, Pageable pageable){
        return new ResponseDTO("200", "分包货物分页查询",outsourcingGoodsService.findByOutsourcingGoods(outsourcingGoodsDTO,pageable));
    }

    //点击编辑--新建状态下的编辑，确认发车,确认收货等三个功能的按钮接口
    @RequestMapping("/api/manager/selectUpdateOutsourcingGoods")
    public ResponseDTO selectUpdateOutsourcingGoods(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){

        return new ResponseDTO("200", "车辆货物详情订单列表",outsourcingGoodsService.selectUpdateOutsourcingGoods(outsourcingGoodsDTO));
    }

    //点击编辑备注
    @RequestMapping("/api/manager/addRemarks")
    public ResponseDTO addRemarks(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){
        Boolean str= outsourcingGoodsService.addRemarks(outsourcingGoodsDTO);
        if (str==true){
            return new ResponseDTO("200", "点击编辑备注",str);
        }else {
            return new ResponseDTO("401", "点击编辑备注失败",str);
        }
    }

   //运输状态下 --编辑时批量保存
   @RequestMapping("/api/manager/saveNewBatchOutCommodity")
   public ResponseDTO saveNewBatchOutCommodity(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){

       return new ResponseDTO("200", "运输状态下 --编辑时批量保存",outsourcingGoodsService.addNewBatchOutCommodity(outsourcingGoodsDTO));
   }


    //点击发车确认
    @RequestMapping("/api/manager/ogSaveStartCarIsTrue")
    public ResponseDTO ogSaveStartCarIsTrue(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){
        Boolean str= outsourcingGoodsService.saveStartCarIsTrue(outsourcingGoodsDTO);
        if (str==true){
            return new ResponseDTO("200", "点击发车确认",str);
        }else {
            return new ResponseDTO("401", "点击发车确认失败",str);
        }

    }

    //点击收货确认
    @RequestMapping("/api/manager/odSaveConfirmationIsTrue")
    public ResponseDTO odSaveConfirmationIsTrue(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){
        Boolean str= outsourcingGoodsService.saveConfirmationIsTrue(outsourcingGoodsDTO);
        if (str==true){
            return new ResponseDTO("200", "点击收货确认",str);
        }else {
            return new ResponseDTO("401", "点击收货确认失败",str);
        }

    }


    //外包发车单导出
    @RequestMapping("/api/manager/exportOutsourcingGoods")
    public void exportOutsourcingGoods(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO, HttpServletRequest request, HttpServletResponse response){
        List<OutsourcingGoodsDTO> list = outsourcingGoodsService.outsourcingGoodsExport(outsourcingGoodsDTO);
        ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(OutsourcingGoodsDTO.class), list, response, "OutsourcedTrainBill.xls");
    }


    //删除外包发车单
    @RequestMapping("/api/manager/deleteOutsourcingGoods")
    public ResponseDTO deleteOutsourcingGoods(@RequestBody OutsourcingGoodsDTO outsourcingGoodsDTO){
       Boolean  str= outsourcingGoodsService.deleteOutInformation(outsourcingGoodsDTO);
       if (str==true){

           return new ResponseDTO("200", "删除外包发车单成功",str);
       }else {
           return new ResponseDTO("401", "删除外包发车单失败",str);

       }
    }









}
