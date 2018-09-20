package com.troy.keeper.hbz.app.rest.web.fsl_and_ltl_facade;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.HbzLtlMapDTO;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/6 19:34
 */
public interface LtlFacade {

    /**
     * 货主 新建零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO createLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 货主 确认零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO confirmLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 计算零担价格
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO computePrice(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 货主 分页条件查询我的零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO queryLtlOrderPage(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 双方 获取零担详情
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO getLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 修改用 查询零担详情
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO loadLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 确认装货
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO takeLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 确认签收
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO comppleteLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 货主 确认收货
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO receiveLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 确认收款
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO endOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 分页条件查询我的已接运订单
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO queryTaskLtlOrderPage(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 月结 同意零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO agreeDrvLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 月结 拒绝零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO refuseDrvLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 货主 抢单 拒绝零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO refuseLtlOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 货主 抢单 同意零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    ResponseDTO agreeLlsOrder(HbzLtlMapDTO hbzLtlMapDTO);

    /**
     * 车主 抢单 零担
     *
     * @param ltlMapDTO
     * @return
     */
    ResponseDTO caLtlOrder(HbzLtlMapDTO ltlMapDTO);

}
