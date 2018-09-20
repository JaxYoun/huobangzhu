package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.core.base.service.BaseService;

public interface WebFslOrderService extends BaseService {

    /**
     * 添加专线整车货源
     * @param webFslOrderDTO
     * @return
     */
    WebFslOrderVO addWebFslOrder(WebFslOrderDTO webFslOrderDTO);

    /**
     * 删除专线整车货源
     * @param webFslOrderDTO
     * @return
     */
    boolean deleteFslOrder(WebFslOrderDTO webFslOrderDTO);

    /**
     * 修改专线整车货源
     * @param webFslOrderDTO
     * @return
     */
    boolean updateFslOrder(WebFslOrderDTO webFslOrderDTO);

    /**
     * 获取整车货源详情
     * @param webFslOrderDTO
     * @return
     */
    WebFslOrderVO getFslOrderById(WebFslOrderDTO webFslOrderDTO);

}
