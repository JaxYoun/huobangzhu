package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.service.VehicleInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
@RestController
public class VehicleInformationResource {
    @Autowired
    private VehicleInformationService vehicleInformationService;



    //分页查询
    @RequestMapping("/api/manager/vehicleInformationTable")
    public ResponseDTO vehicleInformationTable(@RequestBody VehicleInformationDTO vehicleInformationDTO, Pageable pageable){
        return new ResponseDTO("200", "车辆信息列表分页查询",vehicleInformationService.findByCondition(vehicleInformationDTO,pageable));
    }

    //新增车辆信息
    @RequestMapping("/api/manager/addVehicleInformation")
    public ResponseDTO addVehicleInformation(@RequestBody VehicleInformationDTO vehicleInformationDTO){
         Boolean s=  vehicleInformationService.numberPlate(vehicleInformationDTO);
//         Boolean s1= vehicleInformationService.vehicleNumber(vehicleInformationDTO);
         Boolean s2= vehicleInformationService.ownersTelephone(vehicleInformationDTO);
         Boolean s3= vehicleInformationService.ownerNumber(vehicleInformationDTO);
         Boolean s4= vehicleInformationService.driverTelephone(vehicleInformationDTO);
         Boolean s5= vehicleInformationService.driverNumber(vehicleInformationDTO);
         if (s==false){
             return new ResponseDTO("401", "新增车牌号已存在",s);
         }
//         if (s1==false){
//             return new ResponseDTO("200", "新增车辆编号已存在",s1);
//         }
         if (s2==false){
             return new ResponseDTO("401", "新增车主电话号码已存在",s2);
         }
         if (s3==false){
             return new ResponseDTO("401", "新增车主证件号已存在",s3);
         }
         if (s4==false){
             return new ResponseDTO("401", "司机电话号已存在",s4);
         }
         if (s5==false){
             return new ResponseDTO("401", "司机证件号已存在",s5);
         }

        return new ResponseDTO("200", "新增车辆信息成功",vehicleInformationService.addVehicleInformation(vehicleInformationDTO));
    }


    //修改车辆信息
    @RequestMapping("/api/manager/updateVehicleInformation")
    public ResponseDTO updateVehicleInformation(@RequestBody VehicleInformationDTO vehicleInformationDTO){
        Boolean s=vehicleInformationService.updateNumberPlate(vehicleInformationDTO);
//        Boolean s1=vehicleInformationService.updateVehicleNumber(vehicleInformationDTO);
        Boolean s2=vehicleInformationService.updateOwnersTelephone(vehicleInformationDTO);
        Boolean s3=vehicleInformationService.updateOwnerNumber(vehicleInformationDTO);
        Boolean s4=vehicleInformationService.updateDriverTelephone(vehicleInformationDTO);
        Boolean s5=vehicleInformationService.updateDriverNumber(vehicleInformationDTO);

        if (s==false){
            return new ResponseDTO("401", "车牌号已存在",s);
        }
//        if (s1==false){
//            return new ResponseDTO("200", "车辆编号已存在",s1);
//        }
        if (s2==false){
            return new ResponseDTO("401", "车主电话已存在",s2);
        }
        if (s3==false){
            return new ResponseDTO("401", "车主证件号已存在",s3);
        }
        if (s4==false){
            return new ResponseDTO("401", "司机电话号已存在",s4);
        }
        if (s5==false){
            return new ResponseDTO("401", "司机证件号已存在",s5);
        }

        return new ResponseDTO("200", "修改成功",vehicleInformationService.updateVehicleInformation(vehicleInformationDTO));
    }

    //删除车辆信息
    @RequestMapping("/api/manager/deleteVehicleInformation")
    public ResponseDTO deleteVehicleInformation(@RequestBody VehicleInformationDTO vehicleInformationDTO){
        return new ResponseDTO("200", "删除成功",vehicleInformationService.deleteVehicleInformation(vehicleInformationDTO));
    }








}
