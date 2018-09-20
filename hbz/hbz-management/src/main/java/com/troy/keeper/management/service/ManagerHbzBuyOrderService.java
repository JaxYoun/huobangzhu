package com.troy.keeper.management.service;

import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.management.dto.HelpBuyDetailsDTO;
import com.troy.keeper.management.dto.HelpBuyOrderDetailsDTO;
import com.troy.keeper.management.dto.HelpBuyTableDTO;
import com.troy.keeper.management.dto.PlatformDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/11.帮我买
 */
public interface ManagerHbzBuyOrderService {

    //分页
    public Page<HelpBuyTableDTO> findByCondition(HelpBuyTableDTO helpBuyTableDTO, Pageable pageable);

    //订单详情按钮
    public HelpBuyOrderDetailsDTO findHelpBuyOrderDetails(HelpBuyOrderDetailsDTO helpBuyOrderDetailsDTO);

   //接单人详情
    public HbzBuyOrderDTO findHbzBuyOrder(HbzBuyOrderDTO hbzBuyOrderDTO);

    //帮我买 物流详情按钮
    public List<HelpBuyDetailsDTO>  findHelpBuyDetails(HelpBuyDetailsDTO helpBuyDetailsDTO);


    //平台付款详情
    public  PlatformDetailsDTO findPlatformDetails(PlatformDetailsDTO platformDetailsDTO);




}
