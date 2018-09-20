package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.HbzVersionManagementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Autohor: hecj
 * @Description: 移动端版本管理Service
 * @Date: Created in 14:47  2018/2/1.
 * @Midified By:
 */
public interface HbzVersionManagementService {
    //版本管理列表查询
    Page<HbzVersionManagementDTO> queryVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO, Pageable pageable);

    //新增版本
    Boolean addVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO);

    //修改版本
    Boolean updateVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO);

    /**
     * 删除app版本
     */
    boolean deleteVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO);
    /**
     * 是否禁用app版本
     */
    boolean isDisableVersionManagement(HbzVersionManagementDTO hbzVersionManagementDTO);
}
