package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.PrepayOrderDTO;
import com.troy.keeper.hbz.po.PrepayOrder;
import com.troy.keeper.hbz.repository.PrepayOrderRepository;
import com.troy.keeper.hbz.service.WebPayService;
import com.troy.keeper.hbz.service.mapper.PrepayOrderMapper;
import com.troy.keeper.hbz.vo.PrepayOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author：YangJx
 * @Description：web端支付用
 * @DateTime：2017/12/21 10:02
 */
@Service
public class WebPayServiceImpl implements WebPayService{

    @Autowired
    private PrepayOrderRepository prepayOrderRepository;

    @Autowired
    private PrepayOrderMapper prepayOrderMapper;

    @Override
    public String getPreparePayCodeAndUrl(String orderNo) {
        return null;
    }

    /**
     * 生成预支付订单，并保存
     *
     * @param prepayOrderDTO
     * @return
     */
    @Override
    public PrepayOrderVO generatePrepayOrder(PrepayOrderDTO prepayOrderDTO) {

        PrepayOrder prepayOrder = this.prepayOrderMapper.dtoToEntity(prepayOrderDTO);
        PrepayOrder prepayOrderFromDb = this.prepayOrderRepository.save(prepayOrder);
        PrepayOrderVO prepayOrderVO = this.prepayOrderMapper.entityToVo(prepayOrderFromDb);
        return prepayOrderVO;
    }

}
