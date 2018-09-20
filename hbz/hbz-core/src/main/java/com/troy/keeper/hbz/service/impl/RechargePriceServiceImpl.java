package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.RechargePriceDTO;
import com.troy.keeper.hbz.po.RechargePrice;
import com.troy.keeper.hbz.repository.RechargePriceRepository;
import com.troy.keeper.hbz.service.RechargePriceService;
import com.troy.keeper.hbz.type.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 10:43
 */
@Slf4j
@Service
public class RechargePriceServiceImpl implements RechargePriceService {

    @Autowired
    private RechargePriceRepository rechargePriceRepository;

    @Override
    public List<RechargePriceDTO> getRechargePriceListByUserRole(List<Role> roleList, String status) {
        List<RechargePrice> rechargePriceList = rechargePriceRepository.getRechargePriceByRoleInAndStatus(roleList, status);
        List<RechargePriceDTO> rechargePriceDTOList = rechargePriceList.stream().map(it -> {
            RechargePriceDTO tmpRechargePriceDTO = new RechargePriceDTO();
            BeanUtils.copyProperties(it, tmpRechargePriceDTO);
            tmpRechargePriceDTO.setRoleName(it.getRole().getName());
            return tmpRechargePriceDTO;
        }).collect(Collectors.toList());
        return rechargePriceDTOList;
    }


}
