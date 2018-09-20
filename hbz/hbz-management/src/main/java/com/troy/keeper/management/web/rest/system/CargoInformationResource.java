package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.export.ExcelHandleUtil;
import com.troy.keeper.hbz.helper.ExcelUtils;
import com.troy.keeper.management.dto.*;
import com.troy.keeper.management.service.CargoInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/29.
 */
@RestController
public class CargoInformationResource {

    @Autowired
    private CargoInformationService   cargoInformationService;





    //分页查询
    @RequestMapping("/api/manager/CargoInformationPage")
    public ResponseDTO pageQureyDemo(@RequestBody CargoInformationDTO cargoInformationDTO, Pageable pageable){
        return new ResponseDTO("200", "收货管理分页查询",cargoInformationService.findByCondition(cargoInformationDTO,pageable));
    }

    //收货信息不分页查询
    @RequestMapping("/api/manager/cargoInformationNoPage")
    public ResponseDTO cargormationNoPage(@RequestBody CargoInformationDTO cargoInformationDTO){
        return new ResponseDTO("200", "收货管理不分页查询",cargoInformationService.notTablePage(cargoInformationDTO));
    }



   //新增收货托运客户接口  分页查询
   @RequestMapping("/api/manager/checkedUserTable")
   public ResponseDTO userInformationTable(@RequestBody UserInformationDTO userInformationDTO, Pageable pageable){
       return new ResponseDTO("200", "托运客户信息列表分页查询",cargoInformationService.userTable(userInformationDTO,pageable));
   }

   //新增收货时 收货方条件的过滤  分页查询
   @RequestMapping("/api/manager/receivingUserTable")
   public ResponseDTO receivingUserTable(@RequestBody UserInformationDTO userInformationDTO, Pageable pageable){
       return new ResponseDTO("200", "收货方客户信息列表分页查询",cargoInformationService.receivinguserTable(userInformationDTO,pageable));
   }



    //新增收货
    @RequestMapping("/api/manager/addCargoInformation")
    public ResponseDTO addCargoInformation(@RequestBody CargoInformationDTO cargoInformationDTO){
        Boolean  str= cargoInformationService.addCargoInformation(cargoInformationDTO);
        if (str==true){
            return new ResponseDTO("200", "新增收货信息成功",str);
        }else {
            return new ResponseDTO("401", "运单编号已存在",str);
        }
    }

    //修改收货信息
    @RequestMapping("/api/manager/updateCargoInformation")
    public ResponseDTO updateCargoInformation(@RequestBody CargoInformationDTO cargoInformationDTO){
        return new ResponseDTO("200", "修改收货信息成功",cargoInformationService.updateCargoInformation(cargoInformationDTO));

    }

    //删除收货信息
    @RequestMapping("/api/manager/deleteCargoInformation")
    public ResponseDTO deleteCargoInformation(@RequestBody CargoInformationDTO cargoInformationDTO){
         Boolean s=  cargoInformationService.deleteCargoInformation(cargoInformationDTO);
         if (s==true){
             return new ResponseDTO("200", "删除收货信息成功",s);
         }else {
             return new ResponseDTO("401", "删除收货信息失败",s);
         }
    }


    //平台指运订单分页查询
    @RequestMapping("/api/manager/hbzAssignWorkPage")
    public ResponseDTO hbzAssignWorkPage(@RequestBody HbzAssignWorkDTO hbzAssignWorkDTO, Pageable pageable){
        return new ResponseDTO("200", "平台指运订单分页查询",cargoInformationService.findHbzAssignWorkMapDTO(hbzAssignWorkDTO,pageable));
    }

    //快递订单详细信息
    @RequestMapping("/api/manager/selectHbzAssignWorkInfomation")
    public ResponseDTO selectHbzAssignWorkInfomation(@RequestBody HbzAssignWorkDTO hbzAssignWorkDTO){
        return new ResponseDTO("200", "快递订单详细信息",cargoInformationService.findHbzAssignWorkInfomation(hbzAssignWorkDTO));
    }

    //保存平台指定过来的订单信息
    @RequestMapping("/api/manager/saveAssignWorkInfomation")
    public ResponseDTO saveAssignWorkInfomation(@RequestBody  CargoInformationDTO  cargoInformationDTO){
        return new ResponseDTO("200", "保存平台指定过来的订单信息",cargoInformationService.saveHbzAssignWork(cargoInformationDTO));
    }


    //当前登陆人的组织机构id
    @RequestMapping("/api/manager/selectSmOrgId")
    public ResponseDTO selectSmOrgId() {
        Long number = cargoInformationService.smOrgId();
        if (number != 0l) {
            return new ResponseDTO("200", "当前登陆人的组织机构id", number);
        } else {
            return new ResponseDTO("401", "当前登陆人没有组织机构", number);
        }
    }


    //收货导出
    @RequestMapping("/api/manager/exportCargoInformation")
    public void exportCargoInformation(@RequestBody ExprotCargoInformationDTO exprotCargoInformationDTO, HttpServletRequest request, HttpServletResponse response){
        List<ExprotCargoInformationDTO> list = cargoInformationService.cargoInformationExport(exprotCargoInformationDTO);
        ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(ExprotCargoInformationDTO.class), list, response, "ReceivingManagement.xls");
    }




}
