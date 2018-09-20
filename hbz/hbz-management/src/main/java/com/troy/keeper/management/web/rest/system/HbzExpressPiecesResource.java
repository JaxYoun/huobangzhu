package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.ExLogisticsDetailsDTO;
import com.troy.keeper.management.dto.ManagementHbzExOrderDTO;
import com.troy.keeper.management.service.HbzExpressPiecesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2017/12/20.
 */
@RestController
public class HbzExpressPiecesResource {

    @Autowired
    private HbzExpressPiecesService hbzExpressPiecesService;

    //分页查询
    @RequestMapping("/api/manager/hbzExpressPiecesPage")
    public ResponseDTO pageQureyDemo(@RequestBody ManagementHbzExOrderDTO managementHbzExOrderDTO, Pageable pageable){
        return new ResponseDTO("200", "快递列表分页查询",hbzExpressPiecesService.findByCondition(managementHbzExOrderDTO,pageable));
    }

    //快递列表 详情
    @RequestMapping("/api/manager/hbzExpressPiecesDetails")
    public ResponseDTO hbzExpressPiecesDetails(@RequestBody ManagementHbzExOrderDTO managementHbzExOrderDTO){
        return new ResponseDTO("200", "快递列表详情",hbzExpressPiecesService.findHbzHbzExOrder(managementHbzExOrderDTO));
    }

    //保存快递派件
    @RequestMapping("/api/manager/saveHbzExpressPieces")
    public ResponseDTO saveHbzExpressPieces(@RequestBody ManagementHbzExOrderDTO managementHbzExOrderDTO){
        return new ResponseDTO("200", "快递列表详情",hbzExpressPiecesService.saveHbzExpressPieces(managementHbzExOrderDTO));
    }

    //物流详情列表
    @RequestMapping("/api/manager/selectDetails")
    public ResponseDTO selectDetails(@RequestBody ExLogisticsDetailsDTO exLogisticsDetailsDTO){
        return new ResponseDTO("200", "快递列表详情",hbzExpressPiecesService.findLogisticsDetails(exLogisticsDetailsDTO));
    }
}
