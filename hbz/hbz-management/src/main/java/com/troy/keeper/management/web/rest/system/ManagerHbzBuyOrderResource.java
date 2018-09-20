package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.management.dto.HelpBuyDetailsDTO;
import com.troy.keeper.management.dto.HelpBuyOrderDetailsDTO;
import com.troy.keeper.management.dto.HelpBuyTableDTO;
import com.troy.keeper.management.dto.PlatformDetailsDTO;
import com.troy.keeper.management.service.ManagerHbzBuyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥    HelpMeToBuy
 * @date 2017/12/11.帮我买
 */
@RestController
public class ManagerHbzBuyOrderResource {
    @Autowired
    private ManagerHbzBuyOrderService managerHbzBuyOrderService;
  @Autowired
  private EntityService entityService;

//    //分页查询
    @RequestMapping("/api/manager/helpMeToBuyPage")
    public ResponseDTO pageQureyDemo(@RequestBody HelpBuyTableDTO helpBuyTableDTO, Pageable pageable){
        return new ResponseDTO("200", "帮我买分页查询",managerHbzBuyOrderService.findByCondition(helpBuyTableDTO,pageable));
    }

    //订单详情
    @RequestMapping("/api/manager/helpBuyOrderDetails")
    public ResponseDTO helpBuyOrderDetails(@RequestBody HelpBuyOrderDetailsDTO helpBuyOrderDetailsDTO){
        return new ResponseDTO("200", "订单详情",managerHbzBuyOrderService.findHelpBuyOrderDetails(helpBuyOrderDetailsDTO));
    }
    //接单人详情
    @RequestMapping("/api/manager/teakUserDetails")
    public ResponseDTO teakUserDetails(@RequestBody HbzBuyOrderDTO hbzBuyOrderDTO){
        return new ResponseDTO("200", "接单人详情详情",managerHbzBuyOrderService.findHbzBuyOrder(hbzBuyOrderDTO));
    }

    //物流详情按钮
    @RequestMapping("/api/manager/logisticsDetails")
    public ResponseDTO logisticsDetails(@RequestBody HelpBuyDetailsDTO helpBuyDetailsDTO){
        return new ResponseDTO("200", "物流详情按钮",managerHbzBuyOrderService.findHelpBuyDetails(helpBuyDetailsDTO));
    }

    //平台付款详情
    @RequestMapping("/api/manager/platformDetails")
    public ResponseDTO platformDetails(@RequestBody PlatformDetailsDTO platformDetailsDTO){
        if(null==platformDetailsDTO.getId()&& "".equals(platformDetailsDTO.getId())){
            return new ResponseDTO("500", "订单id不能为空",null);
        }
        return new ResponseDTO("200", "平台付款详情",managerHbzBuyOrderService.findPlatformDetails(platformDetailsDTO));
    }





//    @RequestMapping("/api/manager/HelpMeToBuyPage")
//    public ResponseDTO pageQureyDemo(@RequestBody HbzBuyOrderDTO hbzBuyOrderDTO, Pageable pageable){
//        return new ResponseDTO("200", "帮我买分页查询",entityService.queryPage(HbzBuyOrder.class,(a)->{
//            Map<String,Object> r = new Bean2Map(
//                   new PropertyMapper<>("startTime",new TimeMillisFormater("yyyy-MM-dd HH:mm")::format)
//            ).addIgnores(Const.AUDIT_FIELDS).map(a);
//            return r;
//        },hbzBuyOrderDTO.getPageRequest(),hbzBuyOrderDTO));
//    }

}
