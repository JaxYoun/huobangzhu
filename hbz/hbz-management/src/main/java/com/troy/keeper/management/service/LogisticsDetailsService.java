package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.ExLogisticsDetailsDTO;
import com.troy.keeper.management.dto.ManagementHbzExOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2017/12/21.
 */
public interface LogisticsDetailsService {

     //分页查询
    public Page<ManagementHbzExOrderDTO> findByCondition(ManagementHbzExOrderDTO managementHbzExOrderDTO, Pageable pageable);

    //保存快递记录 新增信息
    public Boolean saveLogisticsDetails(ExLogisticsDetailsDTO exLogisticsDetailsDTO);

    //删除物流详情
    public  Boolean deletaLogisticsDetails(ExLogisticsDetailsDTO exLogisticsDetailsDTO);

}
