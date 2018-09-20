package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.service.StartVehicleInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/10.
 */
@RestController
public class StartVehicleInformationResource {

    @Autowired
    private StartVehicleInformationService startVehicleInformationService;


    //所有车辆编号
    @RequestMapping("/api/manager/selecAlltVehicleNumber")
    public ResponseDTO selecAlltVehicleNumber(){
        return new ResponseDTO("200", "所有车辆编号",startVehicleInformationService.findVehicleNumber());
    }



//    //通过货物路线，车辆编号，发车时间查询  发车管理中的货物信息
//    @RequestMapping("/api/manager/selectAllStartVehicle")
//    public ResponseDTO selectAllStartVehicle(@RequestBody StartVehicleDTO startVehicleDTO){
//        List<StartVehicleDTO> list= startVehicleInformationService.findAllStartVehicle(startVehicleDTO);
//        if (list==null || list.size()==0){
//            return new ResponseDTO("401", "没有传入查询必要的条件",list);
//        }else {
//            return new ResponseDTO("200", "该车辆的所有货物",list);
//        }
//
//
//
//    }


   //批量全部添加
   @RequestMapping("/api/manager/addBatchStartVehicle")
   public ResponseDTO addBatchStartVehicle(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO){
       Boolean  str=  startVehicleInformationService.addBatchStartVehicle(startVehicleInformationDTO);
        if (str==true){
            return new ResponseDTO("200", "批量保存装车信息成功",str);
        }else {
            return new ResponseDTO("401", "运输单中部分运输时，运输数量超过了库存量",str);
        }
   }


   //发车单为新建时--编辑保存
   @RequestMapping("/api/manager/addNewBatchStartVehicle")
   public ResponseDTO addNewBatchStartVehicle(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO){
       Boolean  str=  startVehicleInformationService.addNewBatchStartVehicle(startVehicleInformationDTO);
       if (str==true){
           return new ResponseDTO("200", "发车单为新建时--编辑保存成功",str);
       }else {
           return new ResponseDTO("401", "发车单为新建时--编辑保存失败",str);
       }
   }





    //发车单确认
    @RequestMapping("/api/manager/updateShippingStatus")
    public ResponseDTO updateShippingStatus(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO){
        Boolean  str=  startVehicleInformationService.startCar(startVehicleInformationDTO);
        if (str==true){
            return new ResponseDTO("200", "发车单确认成功",str);
        }else {
            return new ResponseDTO("401", "发车单确认失败",str);
        }
    }


    //删除发车单
    @RequestMapping("/api/manager/deleteCarAndStartVehicle")
    public ResponseDTO deleteCarAndStartVehicle(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO){
        Boolean  str=  startVehicleInformationService.deleteCar(startVehicleInformationDTO);
        if (str==true){
            return new ResponseDTO("200", "删除新建发车单成功",str);
        }else {
            return new ResponseDTO("401", "删除新建发车单失败",str);
        }
    }



















}
