package com.troy.keeper.management.web.rest.system;

import com.alibaba.fastjson.JSONArray;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.core.export.ExcelHandleUtil;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.helper.ExcelUtils;
import com.troy.keeper.management.dto.ExprotCargoInformationDTO;
import com.troy.keeper.management.dto.HelpBuyTableDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import com.troy.keeper.management.service.OnTheWayStartService;
import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/9.
 */
@RestController
public class OnTheWayStartResource {

    @Autowired
    private OnTheWayStartService onTheWayStartService;


    ////////// 司机确认分页查询//////////////////////
    //分页查询
    @RequestMapping("/api/manager/driverConfirmedPage")
    public ResponseDTO driverConfirmedPage(@RequestBody StartVehicleDTO startVehicleDTO, Pageable pageable) {
        return new ResponseDTO("200", "司机确认分页查询", onTheWayStartService.driverConfirmed(startVehicleDTO, pageable));
    }

    //司机确认装货
    @RequestMapping("/api/manager/addStatus")
    public ResponseDTO addStatus(@RequestBody StartVehicleDTO startVehicleDTO) {
        Boolean str = onTheWayStartService.updateStatus(startVehicleDTO);
        if (str == true) {
            return new ResponseDTO("200", "司机已经确认", str);
        } else {
            return new ResponseDTO("401", "司机确认失败", str);
        }
    }


    /////////////////////在途分页查询//////////////////////////////
    //分页查询
    @RequestMapping("/api/manager/onTheWayPage")
    public ResponseDTO onTheWayPage(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO, Pageable pageable) {
        return new ResponseDTO("200", "在途分页查询", onTheWayStartService.findByCondition(startVehicleInformationDTO, pageable));
    }


    //编辑修改车辆的状态或者备注
    @RequestMapping("/api/manager/updateTransitState")
    public ResponseDTO updateTransitState(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO) {
        Boolean str = onTheWayStartService.updateStartVehicleInformation(startVehicleInformationDTO);
        if (str == true) {
            return new ResponseDTO("200", "编辑修改车辆的状态或者备注", str);
        } else {
            return new ResponseDTO("401", "请传id，修改状态，修改备注", str);

        }
    }

    //通过车辆的id  查询车中的货物信息
    @RequestMapping("/api/manager/selectAllStartVehicleInformation")
    public ResponseDTO selectAllStartVehicleInformation(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO) {
        return new ResponseDTO("200", "装车货物的信息", onTheWayStartService.findAllStartVehicleInformation(startVehicleInformationDTO));

    }


    //中途装车
    @RequestMapping("/api/manager/midwayLoading")
    public ResponseDTO midwayLoading(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO) {
        Boolean str = onTheWayStartService.inserAletelStartVehicleInformation(startVehicleInformationDTO);
        if (str == true) {
            return new ResponseDTO("200", "中途装车成功", str);
        } else {
            return new ResponseDTO("401", "中途装车成功失败", str);

        }

    }

//    //卸货
//    @RequestMapping("/api/manager/cargoUnloading")
//    public ResponseDTO cargoUnloading(@RequestBody List<Long> list){
//        return new ResponseDTO("200", "卸货成功",onTheWayStartService.unloadProduct(list));
//
//    }

    // 卸货
    @RequestMapping("/api/manager/cargoUnloading")
    public ResponseDTO cargoUnloading(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO) {
        Boolean str = onTheWayStartService.unloadProduct(startVehicleInformationDTO);

        if (str == true) {
            return new ResponseDTO("200", "卸货成功", str);
        } else {
            return new ResponseDTO("401", "请选择相应的货物", str);

        }
    }

    //发车单导出
    @RequestMapping("/api/manager/exportStartVehicleInformation")
    public void exportStartVehicleInformation(@RequestBody StartVehicleInformationDTO startVehicleInformationDTO, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//        StartVehicleInformationDTO startVehicleInformationDTO = new StartVehicleInformationDTO();
        List<StartVehicleInformationDTO> list = onTheWayStartService.startVehicleInformationExport(startVehicleInformationDTO);
//        String str="发车单导出.xls";
//        String strUTF8 = URLEncoder.encode(str, "iso-8859-1");
        OnTheWayStartResource.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(StartVehicleInformationDTO.class), list, response, "TrainDepartureList.xls");
    }


    public static void exportExcel(JSONArray fieldsJsonArray, Collection<?> dataSet, HttpServletResponse response, String excelName) {
        Validate.notBlank(excelName);
        if (excelName.indexOf("xls") < 0) {
            throw new KeeperException("导出名格式不正确");
        }
        response.setCharacterEncoding("utf-8");
        byte[] bytes = new byte[0];
        try {
            bytes = ExcelHandleUtil.exportExcel(fieldsJsonArray, dataSet);
            response.setContentType("application/octet-stream");
            response.setContentLength(bytes.length);
            response.addHeader("Content-Disposition",
                    "attachment;filename=" +  URLEncoder.encode(excelName, "UTF-8"));
            response.getOutputStream().write(bytes, 0, bytes.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取消按钮的接口
    @RequestMapping("/api/manager/cacleStartVehicle")
    public ResponseDTO cancleLoading(@RequestBody StartVehicleDTO startVehicleDTO) {


        return new ResponseDTO(Const.STATUS_OK,"取消成功",onTheWayStartService.cancleStartVehicle(startVehicleDTO));
    }

}


//@Data
//class IDArrayDTO{
//    private List<Long> ids;
//}
