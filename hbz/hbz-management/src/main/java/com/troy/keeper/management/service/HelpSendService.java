package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.HelpSendDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥  帮我送的service
 * @date 2017/12/18.
 */
public interface HelpSendService {
    //分页查询
    public Page<HelpSendDTO> findByCondition(HelpSendDTO helpSendDTO, Pageable pageable);

   //根据订单id 查询订单信息
    public HelpSendDTO findHelpSend(HelpSendDTO helpSendDTO);

   //接单人详情
    public  HelpSendDTO findTeakUser(HelpSendDTO helpSendDTO);




}
