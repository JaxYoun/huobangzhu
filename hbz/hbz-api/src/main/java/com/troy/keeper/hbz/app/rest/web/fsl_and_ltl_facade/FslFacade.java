package com.troy.keeper.hbz.app.rest.web.fsl_and_ltl_facade;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.HbzFslMapDTO;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/6 19:34
 */
public interface FslFacade {

    /**
     * 货主 创建整车订单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO createFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 货主 确认整车订单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO confirmFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 计算整车价格
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO computePrice(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 月结 同意指派的整车订单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO agreeDrvFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 月结 拒绝指派的整车订单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO refuseDrvFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 货主 分页条件查询我创建的整车订单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO queryFslOrderPage(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 货主-车主 获取整车专线详情
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO getFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 获取详情，修改用
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO loadFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 货主 抢单 同意整车单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO agreeFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 货主 抢单 拒绝整车单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO refuseFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 装货
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO takeFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 确认送达
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO comppleteFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 货主 确认签收
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO receiveFslOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 确认收款
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO endOrder(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 分页条件查询已接整车订单
     *
     * @param hbzFslMapDTO
     * @return
     */
    ResponseDTO queryTaskFslOrderPage(HbzFslMapDTO hbzFslMapDTO);

    /**
     * 车主 接整车单
     *
     * @param fslMapDTO
     * @return
     */
    ResponseDTO carryFslOrder(HbzFslMapDTO fslMapDTO);

}
