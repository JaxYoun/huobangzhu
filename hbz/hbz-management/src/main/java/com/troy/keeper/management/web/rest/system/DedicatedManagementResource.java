package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.HbzFslOrder;
import com.troy.keeper.hbz.po.HbzLtlOrder;
import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Map;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.SettlementType;
import com.troy.keeper.management.dto.*;
import com.troy.keeper.management.service.DedicatedManagementService;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2017/11/28.
 */
@RestController
public class DedicatedManagementResource {
     @Autowired
     private  DedicatedManagementService dedicatedManagementService;

     @Autowired
     private HbzFslOrderService hbzFslOrderService;

     @Autowired
     private HbzAreaService hbzAreaService;

     @Autowired
     private HbzTakerInfoService hbzTakerInfoService;

     @Autowired
     private HbzPayService hbzPayService;

    @Autowired
    HbzTypeValService hbzTypeValService;
//
//    @RequestMapping("/api/manager/dedicatedManagementPage")
//    public ResponseDTO DedicatedManagementPage(@RequestBody HbzFslOrderDTO hbzFslOrderDTO,Pageable pageable){
//        return new ResponseDTO("200", "历史信息列表",hbzFslOrderService.queryPage(hbzFslOrderDTO,pageable).map(new Bean2Map().addIgnores(
//                Const.AUDIT_FIELDS
//        ).addIgnores(
//                "pageRequest","size","class"
//        )::map));
//    }

//    @RequestMapping("/api/manager/quyu")
//    public ResponseDTO quyu(@RequestBody HbzAreaDTO area){
//        return new ResponseDTO("200", "历史信息列表",hbzAreaService.query(area).stream().map(new Bean2Map().addIgnores(
//                Const.AUDIT_FIELDS
//        ).addIgnores("parent")::map).collect(Collectors.toList()));
//    }

    //城市联动查询
    @RequestMapping("/api/manager/Linkage")
    public ResponseDTO linkage(@RequestBody HbzAreaDTO hbzAreaDTO){


      if (hbzAreaDTO.getParentId()==null){
          return new ResponseDTO("200", "父id不能为空");
      }
        return new ResponseDTO("200", "查询成功",dedicatedManagementService.findCity(hbzAreaDTO).stream().map(new Bean2Map().addIgnores(
                Const.AUDIT_FIELDS
        ).addIgnores("parent","pageRequest","page","size","class")::map).collect(Collectors.toList()));
    }

   //分页查询
    @RequestMapping("/api/manager/hbzFslOrderpage")
    public ResponseDTO pageQureyDemo(@RequestBody DedicatedLineManagementDTO dedicatedLineManagementDTO, Pageable pageable){
        return new ResponseDTO("200", "整车分页查询",dedicatedManagementService.findByCondition(dedicatedLineManagementDTO,pageable));
    }
    //点击订单详情按钮 订单id 查询订单信息
    @RequestMapping("/api/manager/hbzFslOrderDetails")
    public ResponseDTO hbzFslOrderDetails(@RequestBody DedicatedLineManagementDTO dedicatedLineManagementDTO){
        return new ResponseDTO("200", "订单单人详情按钮",dedicatedManagementService.findHbzFslOrder(dedicatedLineManagementDTO));
    }


    //接单人  详情
    @RequestMapping("/api/manager/teakUserInformation")
    public ResponseDTO teakUserInformation(@RequestBody TeakUserInformationDTO teakUserInformationDTO){
        return new ResponseDTO("200", "接单人详情",dedicatedManagementService.findTeakUserInformation(teakUserInformationDTO));
    }

    //根据订单编号  查询支付信息
    @RequestMapping("/api/manager/payInformation")
    public ResponseDTO payInformation(@RequestBody HbzPayChildDTO hbzPayChildDTO){
        return new ResponseDTO("200", "支付信息",dedicatedManagementService.findHbzPay(hbzPayChildDTO));
    }

    //根据订单id 查询车辆征集条件
    @RequestMapping("/api/manager/cehiclecCollections")
    public ResponseDTO cehiclecCollections(@RequestBody HbzTendersDTO hbzTendersDTO){
        HbzTendersDTO hbzTenderDTO1=  dedicatedManagementService.findHbzTender(hbzTendersDTO);
        return new ResponseDTO("200", "车辆征集条件",hbzTenderDTO1);
//        String need = "--";
//        String registryMoney = "--";
//        String bond = "--";
//        String starLevel = "--";
//            if (hbzTenderDTO1 !=null){
//
//            HbzTypeValDTO hbzTypeValDTO = hbzTypeValService.getByTypeAndValAndLanguage("Driver", String.valueOf(hbzTenderDTO1.getNeed()), "zh_CN");
//
//            if (hbzTypeValDTO != null) {
//                need = hbzTypeValDTO.getName();
//            }
//            HbzTypeValDTO hbzTypeValDTO2 = hbzTypeValService.getByTypeAndValAndLanguage("Registered_funds", String.valueOf(hbzTenderDTO1.getRegistryMoney()), "zh_CN");
//
//            if (hbzTypeValDTO2 != null) {
//                registryMoney = hbzTypeValDTO2.getName();
//            }
//            HbzTypeValDTO hbzTypeValDTO3 = hbzTypeValService.getByTypeAndValAndLanguage("Security_deposit", String.valueOf(hbzTenderDTO1.getBond()), "zh_CN");
//
//            if (hbzTypeValDTO3 != null) {
//                bond = hbzTypeValDTO3.getName();
//            }
//            HbzTypeValDTO hbzTypeValDTO4 = hbzTypeValService.getByTypeAndValAndLanguage("Credit_level", String.valueOf(hbzTenderDTO1.getStarLevel()), "zh_CN");
//
//            if (hbzTypeValDTO4 != null) {
//                starLevel = hbzTypeValDTO4.getName();
//            }
//
//            Map<String, Object> map = MapSpec.mapTender(hbzTenderDTO1);
//            map.put("need", need);
//            map.put("registryMoney", registryMoney);
//            map.put("bond", bond);
//            map.put("starLevel", starLevel);
//            return new ResponseDTO("200", "车辆征集条件",map);
//            }else {
//
//            }


    }


    //点击物流详情
    @RequestMapping("/api/manager/details")
    public ResponseDTO details(@RequestBody LogisticsDetailsDTO logisticsDetailsDTO){
        return new ResponseDTO("200", "物流详情按钮",dedicatedManagementService.findLogisticsDetails(logisticsDetailsDTO));
    }


    //测试自己的封装  征集信息
    @RequestMapping("/api/manager/hbzTakerInfoInformation")
    public ResponseDTO oko(@RequestBody HbzTakerInfoCallDTO hbzTakerInfoCallDTO){
        return new ResponseDTO("200", "车辆征集信息",dedicatedManagementService.findCall(hbzTakerInfoCallDTO));
    }










}
