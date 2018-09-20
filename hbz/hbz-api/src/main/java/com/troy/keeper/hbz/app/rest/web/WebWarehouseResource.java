package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.WarehouseDTO;
import com.troy.keeper.hbz.dto.WarehouseEarnestOrderDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.WebPayUtil;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.hbz.service.WebPayService;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import com.troy.keeper.hbz.vo.WarehouseEarnestOrderVO;
import com.troy.keeper.hbz.vo.WarehouseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：仓储
 * @DateTime：2017/12/21 9:51
 */
@Slf4j
@RestController
@RequestMapping("/api/web/warehouse")
public class WebWarehouseResource extends BaseResource {

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WebPayService webPayService;

    @Config("warehouse.earnest")
    private Double warehouseEarnest;

    private HbzUserDTO getCurrentUser() {
        return this.hbzUserService.currentUser();
    }

    /**
     * 查询租赁诚意金
     *
     * @return
     */
    @PostMapping("/getWarehouseEarnest")
    public ResponseDTO getWarehouseEarnest() {
        return new ResponseDTO(Const.STATUS_OK, "成功！", warehouseEarnest);
    }

    /**
     * 生成仓储租赁诚意金支付订单
     *
     * @param warehouseEarnestOrderDTO
     * @param validResult
     * @return
     */
    @PostMapping("/generateWarehouseEarnestOrder")
    public ResponseDTO generateWarehouseEarnestOrder(@RequestBody @Valid WarehouseEarnestOrderDTO warehouseEarnestOrderDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "输入错误！", fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        HbzUserDTO currentUser = hbzUserService.currentUser();

        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setId(warehouseEarnestOrderDTO.getWarehouseId());
        WarehouseVO warehouseVO = this.warehouseService.getWarehouseById(warehouseDTO);
        if (warehouseVO == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "仓储无效！", null);
        }
        //填充仓储信息
        warehouseEarnestOrderDTO.setWarehouseName(warehouseVO.getName());

        //填充创建人时间信息
        warehouseEarnestOrderDTO.setCreatedBy(currentUser.getId());
        warehouseEarnestOrderDTO.setCreatedDate(Instant.now().getEpochSecond());
        warehouseEarnestOrderDTO.setLastUpdatedBy(currentUser.getId());
        warehouseEarnestOrderDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        warehouseEarnestOrderDTO.setCreateUser(currentUser);

        //填充删除状态、流转、订单号信息
        warehouseEarnestOrderDTO.setStatus(Const.STATUS_ENABLED);
        warehouseEarnestOrderDTO.setPayStatus(WarehouseEarnestPayStatusEnum.NEW);
        warehouseEarnestOrderDTO.setOrderNo(WebPayUtil.generateOrderCode("WAREHOUSE", null));

        WarehouseDTO tempWarehouseDTO = new WarehouseDTO();
        tempWarehouseDTO.setId(warehouseVO.getId());
        warehouseEarnestOrderDTO.setWarehouse(tempWarehouseDTO);
        WarehouseEarnestOrderVO warehouseEarnestOrderVOFromDb = warehouseService.generateWarehouseEarnestOrder(warehouseEarnestOrderDTO);
        return new ResponseDTO(Const.STATUS_OK, "成功！", warehouseEarnestOrderVOFromDb);
    }

    /**
     * 获取仓储租赁诚意金详情
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @PostMapping("/getWarehouseEarnestOrderDetail")
    public ResponseDTO getWarehouseEarnestOrderDetail(@RequestBody WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        if (warehouseEarnestOrderDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "必须传入订单id", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "成功！", this.warehouseService.getWarehouseEarnestOrderDetail(warehouseEarnestOrderDTO));
    }

    /**
     * 分页条件查询保证金列表
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @PostMapping("/getMyWarehouseEarnestOrderByPage")
    public ResponseDTO getMyWarehouseEarnestOrderByPage(@RequestBody WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        HbzUserDTO currentUser = hbzUserService.currentUser();
        warehouseEarnestOrderDTO.setStatus(Const.STATUS_ENABLED);
        warehouseEarnestOrderDTO.setCreatedBy(currentUser.getId());

        String createDate = warehouseEarnestOrderDTO.getCreateDate();
        if (StringUtils.isNotBlank(createDate)) {
            String createDateGE = DateUtils.joinDateAndTime(createDate, " 00:00");
            String createDateLE = DateUtils.joinDateAndTime(createDate, " 23:59");
            try {
                Long createDateLongGE = DateUtils.stringToLong(createDateGE, DateUtils.yyyy_MM_dd_HH);
                Long createDateLongLE = DateUtils.stringToLong(createDateLE, DateUtils.yyyy_MM_dd_HH);

                warehouseEarnestOrderDTO.setCreatedDateGE(createDateLongGE);
                warehouseEarnestOrderDTO.setCreatedDateLE(createDateLongLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        List<Sort.Order> orderList = new ArrayList<>(2);
        orderList.add(new Sort.Order(Sort.Direction.DESC, "lastUpdatedDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));

        Pageable pageable = new PageRequest(warehouseEarnestOrderDTO.getPage(), warehouseEarnestOrderDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", this.warehouseService.getMyWarehouseEarnestOrderByPage(warehouseEarnestOrderDTO, pageable));
    }

    /**
     * 创建仓储咨询
     *
     * @param warehouseDTO
     * @param validResult
     * @return
     */
    @PostMapping("/createWarehouse")
    public ResponseDTO createWarehouse(@RequestBody @Valid WarehouseDTO warehouseDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()).toString(), fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }

        HbzUserDTO currentUser = this.getCurrentUser();
        warehouseDTO.setCreatedBy(currentUser.getId());
        warehouseDTO.setCreatedDate(Instant.now().getEpochSecond());
        warehouseDTO.setLastUpdatedBy(currentUser.getId());
        warehouseDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        warehouseDTO.setCreateUserDTO(currentUser);
        warehouseDTO.setLifecycle(Const.WAREHOUSE_CREATED);
        warehouseDTO.setStatus(Const.STATUS_ENABLED);
        WarehouseVO warehouseVOFromDB = this.warehouseService.createWarehouse(warehouseDTO);
        if (warehouseVOFromDB == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "创建失败!", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", warehouseVOFromDB);
    }

    /**
     * 查询仓储详情
     *
     * @param warehouseDTO
     * @return
     */
    @PostMapping("/getWarehouseDetail")
    public ResponseDTO getWarehouseDetail(@RequestBody WarehouseDTO warehouseDTO) {
        if (warehouseDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }

        ResponseDTO responseDTO;
        WarehouseVO warehouseVOFromDB = this.warehouseService.getWarehouseDetail(warehouseDTO);
        if (warehouseVOFromDB == null) {
            responseDTO = new ResponseDTO(Const.STATUS_ERROR, "查询失败！", null);
        } else {
            responseDTO = new ResponseDTO(Const.STATUS_OK, "成功！", warehouseVOFromDB);
        }
        return responseDTO;
    }

    /**
     * 删除仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    @PostMapping("/deleteWarehouse")
    public ResponseDTO deleteWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        HbzUserDTO currentUser = this.getCurrentUser();
        if (warehouseDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }
        WarehouseVO warehouseVOFromDB = this.warehouseService.getWarehouseById(warehouseDTO);
        if (!currentUser.getId().equals(warehouseVOFromDB.getCreatedBy())) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能修改他人的仓储信息！", null);
        }
        if (!Const.WAREHOUSE_CREATED.equals(warehouseVOFromDB.getLifecycle())) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能修改已发布的仓储信息！", null);
        }

        warehouseDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        warehouseDTO.setLastUpdatedBy(this.getCurrentUser().getId());
        ResponseDTO responseDTO;
        if (!this.warehouseService.deleteWarehouse(warehouseDTO)) {
            responseDTO = new ResponseDTO(Const.STATUS_ERROR, "删除失败！", null);
        } else {
            responseDTO = new ResponseDTO(Const.STATUS_OK, "删除成功！", null);
        }
        return responseDTO;
    }

    /**
     * 修改仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    @PostMapping("/updateWarehouse")
    public ResponseDTO updateWarehouse(@RequestBody @Valid WarehouseDTO warehouseDTO, BindingResult validResult) {
        List<FieldError> fieldErrorList = validResult.getFieldErrors();
        if (fieldErrorList.size() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()).toString(), fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }
        if (warehouseDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }

        HbzUserDTO currentUser = this.getCurrentUser();
        WarehouseVO warehouseVOFromDB = this.warehouseService.getWarehouseById(warehouseDTO);
        if (!currentUser.getId().equals(warehouseVOFromDB.getCreatedBy())) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能修改他人的仓储信息！", null);
        }
        if (!Const.WAREHOUSE_CREATED.equals(warehouseVOFromDB.getLifecycle())) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能修改已发布的仓储信息！", null);
        }

        warehouseDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        warehouseDTO.setLastUpdatedBy(this.getCurrentUser().getId());
        ResponseDTO responseDTO;
        if (!this.warehouseService.updateWarehouse(warehouseDTO)) {
            responseDTO = new ResponseDTO(Const.STATUS_ERROR, "修改失败！", null);
        } else {
            responseDTO = new ResponseDTO(Const.STATUS_OK, "修改成功！", null);
        }
        return responseDTO;
    }

    /**
     * 分页条件查询我创建的仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    @PostMapping("/getMyWarehouseListByPage")
    public ResponseDTO getMyWarehouseListByPage(@RequestBody WarehouseDTO warehouseDTO) {
        warehouseDTO.setCreatedBy(this.getCurrentUser().getId());
        warehouseDTO.setStatus(Const.STATUS_ENABLED);

        List<Sort.Order> orderList = new ArrayList<>(2);
        orderList.add(new Sort.Order(Sort.Direction.DESC, "lastUpdatedDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));

        Pageable pageable = new PageRequest(warehouseDTO.getPage(), warehouseDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", this.warehouseService.getMyWarehouseListByPage(warehouseDTO, pageable));
    }

    /**
     * 分页条件查询已发布仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    @Deprecated
    @PostMapping("/getAvailableWarehouseListByPage")
    public ResponseDTO getAvailableWarehouseListByPage(@RequestBody WarehouseDTO warehouseDTO) {
        warehouseDTO.setStatus(Const.STATUS_ENABLED);
        warehouseDTO.setLifecycle(Const.WAREHOUSE_PUBLISHED);
        List<Sort.Order> orderList = new ArrayList<>(2);
        orderList.add(new Sort.Order(Sort.Direction.DESC, "lastUpdatedDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));

        Pageable pageable = new PageRequest(warehouseDTO.getPage(), warehouseDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", this.warehouseService.getMyWarehouseListByPage(warehouseDTO, pageable));
    }

}