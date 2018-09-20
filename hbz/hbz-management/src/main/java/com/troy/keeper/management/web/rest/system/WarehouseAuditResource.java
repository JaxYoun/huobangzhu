package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.utils.SpringUtils;
import com.troy.keeper.hbz.dto.WarehouseDTO;
import com.troy.keeper.hbz.helper.Stacks;
import com.troy.keeper.hbz.po.HbzWarehouseAudit;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.management.dto.WarehouseAuditDTO;
import com.troy.keeper.management.dto.WarehouseEarnestOrderDTO;
import com.troy.keeper.management.dto.WarehouseManageDTO;
import com.troy.keeper.hbz.service.SitePushMessageService;
import com.troy.keeper.management.service.WarehouseAuditService;
import com.troy.keeper.management.service.impl.WarehouseAuditServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Autohor: hecj
 * @Description: 仓储管理与审核
 * @Date: Created in 10:54  2018/1/9.
 * @Midified By:
 */
@RestController
@RequestMapping("/api/manager")
public class WarehouseAuditResource {

    private final Logger log = LoggerFactory.getLogger(WarehouseAuditResource.class);

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    private WarehouseAuditService warehouseAuditService;

    @Autowired
    private SitePushMessageService sitePushMessageService;

    @Autowired
    WarehouseService warehouseService;

    /**
     * 审核分页列表查询
     *
     * @param warehouseAuditDTO
     * @param pageable
     * @return
     */
    @PostMapping("/queryWarehouseAudit")
    public ResponseDTO queryWarehouseAudit(@RequestBody WarehouseAuditDTO warehouseAuditDTO, Pageable pageable) {
        return new ResponseDTO("200", "仓储审核分页查询", warehouseAuditService.queryWarehouseAudit(warehouseAuditDTO, pageable));
    }

    /**
     * 修改审核
     *
     * @param warehouseAuditDTO
     * @return
     */
    @PostMapping("/updateWarehouseAudit")
    public ResponseDTO updateWarehouseAudit(@RequestBody WarehouseAuditDTO warehouseAuditDTO) {
        if (null == warehouseAuditDTO.getId() || "".equals(warehouseAuditDTO.getId())) {
            return new ResponseDTO("401", "记录Id不能为空！", null);
        }
        if (null == warehouseAuditDTO.getType() || "".equals(warehouseAuditDTO.getType())) {
            return new ResponseDTO("401", "审核状态不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!warehouseAuditService.updateWarehouseAudit(warehouseAuditDTO)) {
            responseDTO = new ResponseDTO("500", "保存失败！", null);
        } else {
            HbzWarehouseAudit warehouseAudit = warehouseAuditService.findById(warehouseAuditDTO.getId());
            WarehouseDTO warehouseDTO = new WarehouseDTO();
            warehouseDTO.setId(warehouseAudit.getWarehouse().getId());
            boolean isAudit = Stacks.pop();
            sitePushMessageService.sendMessageImmediately(Arrays.asList(warehouseService.findByWarehourse(warehouseDTO)), "信息审核", "仓储审核消息", isAudit ? "恭喜您，您发布的仓储信息已经通过审核，其他人可以看到您发布的仓储信息啦。" : "很遗憾，您发布的仓储信息未通过审核，请在我的仓储资讯中查看详情。");
            responseDTO = new ResponseDTO("200", "保存成功！", null);
        }
        return responseDTO;
    }

    /**
     * 审核查询详情
     * queryWarehouseAuditDetail
     *
     * @param warehouseAuditDTO
     * @return
     */
    @PostMapping("/queryWarehouseAuditDetail")
    public ResponseDTO queryWarehouseAuditDetail(@RequestBody WarehouseAuditDTO warehouseAuditDTO) {
        if (null == warehouseAuditDTO.getId() || "".equals(warehouseAuditDTO.getId())) {
            return new ResponseDTO("401", "ID不能为空！", null);
        }
        WarehouseAuditDTO warehouseAudit = warehouseAuditService.queryWarehouseAuditDetail(warehouseAuditDTO);
        if (null != warehouseAudit) {
            return new ResponseDTO("200", "查询成功！", warehouseAudit);
        } else {
            return new ResponseDTO("500", "查询失败！", null);
        }
    }

    /**
     * 管理分页列表查询
     *
     * @param warehouseAuditDTO
     * @param pageable
     * @return
     */
    @PostMapping("/queryWarehouseManage")
    public ResponseDTO queryWarehouseManage(@RequestBody WarehouseManageDTO warehouseManageDTO, Pageable pageable) {
        return new ResponseDTO("200", "仓储管理分页查询", warehouseAuditService.queryWarehouseManage(warehouseManageDTO, pageable));
    }

    /**
     * 管理查询详情
     * queryWarehouseAuditDetail
     *
     * @param warehouseAuditDTO
     * @return
     */
    @PostMapping("/queryWarehouseManageDetail")
    public ResponseDTO queryWarehouseManageDetail(@RequestBody WarehouseManageDTO warehouseManageDTO) {
        if (null == warehouseManageDTO.getId() || "".equals(warehouseManageDTO.getId())) {
            return new ResponseDTO("401", "ID不能为空！", null);
        }
        WarehouseManageDTO warehouseManage = warehouseAuditService.queryWarehouseManageDetail(warehouseManageDTO);
        if (null != warehouseManage) {
            return new ResponseDTO("200", "查询成功！", warehouseManage);
        } else {
            return new ResponseDTO("500", "查询失败！", null);
        }
    }


    /**
     * 修改审核
     *
     * @param warehouseAuditDTO
     * @return
     */
    @PostMapping("/updateWarehouseManage")
    public ResponseDTO updateWarehouseManage(@RequestBody WarehouseManageDTO warehouseManageDTO) {
        if (null == warehouseManageDTO.getId() || "".equals(warehouseManageDTO.getId())) {
            return new ResponseDTO("401", "记录Id不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!warehouseAuditService.updateWarehouseManage(warehouseManageDTO)) {
            responseDTO = new ResponseDTO("500", "保存失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "保存成功！", null);
        }
        return responseDTO;
    }

    /**
     * 仓储状态每天凌晨2点执行一次定时任务
     */
    public void updateLifecycleTimer() {
        this.log.debug("仓储状态过期修改开始..............");
        if (warehouseAuditService == null) {
            warehouseAuditService = SpringUtils.getBean("warehouseAuditServiceImpl");
        }
        warehouseAuditService.updateLifecycleTimer();
        this.log.debug("仓储状态过期修改结束..............");
    }

    /**
     * 修改仓储信息过期
     *
     * @param warehouseAuditDTO
     * @return
     */
    @PostMapping("/updateLifecycleOverdue")
    public ResponseDTO updateLifecycleOverdue(@RequestBody WarehouseManageDTO warehouseManageDTO) {
        if (null == warehouseManageDTO.getId() || "".equals(warehouseManageDTO.getId())) {
            return new ResponseDTO("401", "记录Id不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!warehouseAuditService.updateLifecycleOverdue(warehouseManageDTO)) {
            responseDTO = new ResponseDTO("500", "保存失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "保存成功！", null);
        }
        return responseDTO;
    }

    /**
     * 分页条件查询保证金列表
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @PostMapping("/queryWarehouseEarnestOrderByPage")
    public ResponseDTO queryWarehouseEarnestOrderByPage(@RequestBody WarehouseEarnestOrderDTO warehouseEarnestOrderDTO, Pageable pageable) {
        return new ResponseDTO("200", "查询成功！", warehouseAuditService.queryWarehouseEarnestOrderByPage(warehouseEarnestOrderDTO, pageable));
    }


    /**
     * 获取仓储租赁诚意金详情
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @PostMapping("/queryWarehouseEarnestOrderDetail")
    public ResponseDTO queryWarehouseEarnestOrderDetail(@RequestBody WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        if (warehouseEarnestOrderDTO.getId() == null) {
            return new ResponseDTO("401", "必须传入订单id", null);
        }
        return new ResponseDTO("200", "查询成功！", warehouseAuditService.queryWarehouseEarnestOrderDetail(warehouseEarnestOrderDTO));
    }

    /**
     * 申请退款（租赁诚意金）
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @PostMapping("/refundWarehouseEarnestOrder")
    public ResponseDTO refundWarehouseEarnestOrder(@RequestBody WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        if (warehouseEarnestOrderDTO.getId() == null) {
            return new ResponseDTO("401", "必须传入订单id", null);
        }
        String msg = warehouseAuditService.refundWarehouseEarnestOrder(warehouseEarnestOrderDTO);
        if ("退款成功".equals(msg)) {
            return new ResponseDTO("200", "退款成功！", null);
        } else {
            return new ResponseDTO("200", msg, null);
        }
    }
}
