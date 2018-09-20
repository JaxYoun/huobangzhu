package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.HbzVehicleInformationDTO;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.HbzVehicleInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @Autohor: hecj
 * @Description: 车辆信息管理与编辑
 * @Date: Created in 14:31  2018/1/30.
 * @Midified By:
 */
@RestController
@RequestMapping("/api/vehicleInformation")
public class HbzVehicleInformationResource {

    @Autowired
    HbzVehicleInformationService hbzVehicleInformationService;

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryVehicleInformations(@RequestBody HbzVehicleInformationDTO vehicleInformationDTO, Pageable pageable) {
        return new ResponseDTO("200", "车辆信息分页查询",hbzVehicleInformationService.queryVehicleInformations(vehicleInformationDTO, pageable));
    }

    @PostMapping("/addVehicleInformation")
    public ResponseDTO addVehicleInformation(@RequestBody HbzVehicleInformationDTO vehicleInformationDTO) {
        if(null==vehicleInformationDTO.getPlateNumber() || "".equals(vehicleInformationDTO.getPlateNumber())){
            return new ResponseDTO("401", "车牌号不能为空！", null);
        }
        if(null==vehicleInformationDTO.getTransType() || "".equals(vehicleInformationDTO.getTransType())){
            return new ResponseDTO("401", "车辆类型不能为空！", null);
        }
        if(null==vehicleInformationDTO.getMaxLoad() || "".equals(vehicleInformationDTO.getMaxLoad())){
            return new ResponseDTO("401", "载重不能为空！", null);
        }
        if(null==vehicleInformationDTO.getTransSizes() || "".equals(vehicleInformationDTO.getTransSizes())){
            return new ResponseDTO("401", "车长不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!hbzVehicleInformationService.addVehicleInformation(vehicleInformationDTO)) {
            responseDTO = new ResponseDTO("500", "添加失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "添加成功！", null);
        }
        return responseDTO;
    }

    @PostMapping("/updateVehicleInformation")
    public ResponseDTO updateVehicleInformation(@RequestBody HbzVehicleInformationDTO vehicleInformationDTO) {
        if(null==vehicleInformationDTO.getId() || "".equals(vehicleInformationDTO.getId())){
            return new ResponseDTO("401", "ID不能为空！", null);
        }
        if(null==vehicleInformationDTO.getPlateNumber() || "".equals(vehicleInformationDTO.getPlateNumber())){
            return new ResponseDTO("401", "车牌号不能为空！", null);
        }
        if(null==vehicleInformationDTO.getTransType() || "".equals(vehicleInformationDTO.getTransType())){
            return new ResponseDTO("401", "车辆类型不能为空！", null);
        }
        if(null==vehicleInformationDTO.getMaxLoad() || "".equals(vehicleInformationDTO.getMaxLoad())){
            return new ResponseDTO("401", "载重不能为空！", null);
        }
        if(null==vehicleInformationDTO.getTransSizes() || "".equals(vehicleInformationDTO.getTransSizes())){
            return new ResponseDTO("401", "车长不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!hbzVehicleInformationService.updateVehicleInformation(vehicleInformationDTO)) {
            responseDTO = new ResponseDTO("500", "修改失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "修改成功！", null);
        }
        return responseDTO;
    }

    @PostMapping("/deleteVehicleInformation")
    public ResponseDTO deleteVehicleInformation(@RequestBody HbzVehicleInformationDTO vehicleInformationDTO) {
        if(null==vehicleInformationDTO.getId() || "".equals(vehicleInformationDTO.getId())){
            return new ResponseDTO("401", "ID不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!hbzVehicleInformationService.deleteVehicleInformation(vehicleInformationDTO)) {
            responseDTO = new ResponseDTO("500", "修改失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "修改成功！", null);
        }
        return responseDTO;
    }
}
