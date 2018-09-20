package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.service.StartVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/3.
 */
@RestController
public class StartVehicleResource {

    @Autowired
    private StartVehicleService startVehicleService;








//    //所有车辆编号
//    @RequestMapping("/api/manager/selecAlltVehicleNumber")
//    public ResponseDTO selecAlltVehicleNumber(@RequestBody StartVehicleDTO startVehicleDTO){
//        return new ResponseDTO("200", "所有车辆编号",startVehicleService.findVehicleNumber());
//    }

    //通过车辆编号查询 车辆的信息
    @RequestMapping("/api/manager/selectVehicleInformation")
    public ResponseDTO selectVehicleInformation(@RequestBody VehicleInformationDTO vehicleInformationDTO){
        VehicleInformationDTO vi=  startVehicleService.finddVehicleInformation(vehicleInformationDTO);
        if (vi==null){
            return new ResponseDTO("401", "没有收到相应的车牌号",vi);
        }else {
            return new ResponseDTO("200", "该车辆编号的车牌信息",vi);
        }


    }


//    //通过货物路线，车辆编号，发车时间查询  发车管理中的货物信息
//    @RequestMapping("/api/manager/selectAllStartVehicle")
//    public ResponseDTO selectAllStartVehicle(@RequestBody StartVehicleDTO startVehicleDTO){
//        List<StartVehicleDTO>  list= startVehicleService.findAllStartVehicle(startVehicleDTO);
//        if (list==null || list.size()==0){
//            return new ResponseDTO("401", "没有传入查询必要的条件",list);
//        }else {
//            return new ResponseDTO("200", "该车辆的所有货物",list);
//        }
//
//
//
//    }

//    //批量保存  全部装车的货物信息
//    @RequestMapping("/api/manager/addBatchStartVehicle")
//    public ResponseDTO addBatchStartVehicle(@RequestBody List<StartVehicleDTO> startVehicleDTO){
//        Boolean  str=  startVehicleService.addBatchStartVehicle(startVehicleDTO);
//     if (str==true){
//         return new ResponseDTO("200", "保存成功",str);
//     }else {
//         return new ResponseDTO("401", "请选中需要保存的信息",str);
//     }
//
//
//    }

//
//    //部分保存
//    @RequestMapping("/api/manager/addAleteStartVehicle")
//    public ResponseDTO addAleteStartVehicle(@RequestBody StartVehicleDTO   startVehicleDTO){
//        Boolean  str=  startVehicleService.addAleteStartVehicle(startVehicleDTO);
//     if (str==true){
//         return new ResponseDTO("200", "保存成功",str);
//     }else {
//         return new ResponseDTO("401", "运单的数超过库存",str);
//     }
//
//
//    }













}
