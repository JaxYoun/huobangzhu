package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.ExLogisticsDetailsDTO;
import com.troy.keeper.management.dto.ManagementHbzExOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/20.
 */
public interface HbzExpressPiecesService {

    //分页查询
    public Page<ManagementHbzExOrderDTO> findByCondition(ManagementHbzExOrderDTO managementHbzExOrderDTO, Pageable pageable);

    //通过快递的 id  查询快递派件表中的 及快递详情
    public ManagementHbzExOrderDTO  findHbzHbzExOrder(ManagementHbzExOrderDTO managementHbzExOrderDTO);

   //保存 快递派件
   public Boolean saveHbzExpressPieces(ManagementHbzExOrderDTO managementHbzExOrderDTO);

    ////通过本表的 id  即是实体类中的exId 查询物流详情记录中的 数据
    public List<ExLogisticsDetailsDTO> findLogisticsDetails(ExLogisticsDetailsDTO exLogisticsDetailsDTO);



}
