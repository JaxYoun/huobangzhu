package com.troy.keeper.hbz.service.mapper.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebFslOrderServiceImpl implements WebFslOrderService {

    @Autowired
    private WebFslOrderRepository webFslOrderRepository;

    @Autowired
    private WebFslOrderMapper webFslOrderMapper;

    @Override
    public WebFslOrderVO addWebFslOrder(WebFslOrderDTO webFslOrderDTO) {
        WebFslOrder fslOrder = webFslOrderMapper.dtoToEntity((webFslOrderDTO));
        WebFslOrder webFslOrder = webFslOrderRepository.save(fslOrder);
        return webFslOrderMapper.entityToVO(webFslOrder);
    }

    @Override
    public boolean deleteFslOrder(WebFslOrderDTO webFslOrderDTO) {
        boolean bool = true;
        try {
            webFslOrderDTO.setStatus("0");
            webFslOrderRepository.save(webFslOrderMapper.dtoToEntity(webFslOrderDTO));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            bool = false;
        }
        return bool;
    }

    @Override
    public boolean updateFslOrder(WebFslOrderDTO webFslOrderDTO) {
        boolean bool = true;
        try {
            webFslOrderRepository.save(webFslOrderMapper.dtoToEntity(webFslOrderDTO));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            bool = false;
        }
        return bool;
    }

    @Override
    public WebFslOrderVO getFslOrderById(WebFslOrderDTO webFslOrderDTO) {
        return webFslOrderMapper.entityToVO(webFslOrderRepository.findOne(webFslOrderDTO.getId()));
    }

}
