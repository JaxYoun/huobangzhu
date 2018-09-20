package com.troy.keeper.hbz.app.rest.web.fsl_and_ltl_facade;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzFslMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FslFacadeImpl implements FslFacade {

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzFslOrderService hbzFslOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    @Config("com.hbz.location.x.width")
    private Double xWidth;

    @Config("com.hbz.location.y.width")
    private Double yWidth;

    @Autowired
    private MapService mapService;

    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    @Config("com.hbz.price.fsl.formula.heavy")
    private String heavyFormula;

    @Config("com.hbz.price.fsl.formula.light")
    private String lightFormula;

    @Autowired
    HbzFormulaService hbzFormulaService;

    @Autowired
    HbzRateService rates;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    HbzPledgeService hbzPledgeService;

    /**
     * 货主 创建整车订单
     *
     * @param fslOrderCreateDTO
     * @return
     */
    @Override
    public ResponseDTO createFslOrder(HbzFslMapDTO fslOrderCreateDTO) {
        String[] err = ValidationHelper.valid(fslOrderCreateDTO, "fsl_order_create");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzFslOrderDTO fsl = new HbzFslOrderDTO();
        new Bean2Bean(
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).addExcludeProp("id", "name", "orderTrans", "orderType").copyProperties(fslOrderCreateDTO, fsl);
        String orderNo = hbzOrderService.createNewOrderNo(OrderType.FSL);
        fsl.setOrderNo(orderNo);
        fsl.setStatus(Const.STATUS_ENABLED);
        fsl.setOrderTrans(OrderTrans.NEW);
        fsl.setOrderType(OrderType.FSL);
        fsl.setOriginArea(hbzAreaService.findByOutCode(fslOrderCreateDTO.getOriginAreaCode()));
        if (fsl.getOriginArea() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "发货区域编码[" + fslOrderCreateDTO.getOriginAreaCode() + "]无效");
        fsl.setDestArea(hbzAreaService.findByOutCode(fslOrderCreateDTO.getDestAreaCode()));
        if (fsl.getDestArea() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "收货区域编码[" + fslOrderCreateDTO.getDestAreaCode() + "]无效");
        fsl.setCreateUser(hbzUserService.currentUser());
        fsl.setCreateTime(System.currentTimeMillis());
        if ((fsl = hbzFslOrderService.save(fsl)) != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", fsl.getId());
            data.put("orderNo", fsl.getOrderNo());
            hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
            return new ResponseDTO(Const.STATUS_OK, "保存成功", data);
        } else
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
    }

    /**
     * 货主 确认整车订单
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO confirmFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        if (fslMapDTO.getSettlementType() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "结算方法为空", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在");
        }
        if (!fsl.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单");
        }

        String srcAddr = fsl.getOriginAddress();
        if (fsl.getOriginArea() != null) {
            HbzAreaDTO area = fsl.getOriginArea();
            while (area != null && area.getLevel() != 0) {
                srcAddr = area.getAreaName() + srcAddr;
                area = area.getParent();
            }
        }
        String dstAddr = fsl.getDestAddress();
        if (fsl.getDestArea() != null) {
            HbzAreaDTO area = fsl.getDestArea();
            while (area != null && area.getLevel() != 0) {
                dstAddr = area.getAreaName() + dstAddr;
                area = area.getParent();
            }
        }

        Double[] srcLocation = mapService.getLocationByAddress(srcAddr);
        Double[] dstLoc = mapService.getLocationByAddress(dstAddr);
        if (srcLocation != null) {
            fsl.setOriginX(srcLocation[0]);
            fsl.setOriginY(srcLocation[1]);
        }
        if (dstLoc != null) {
            fsl.setDestX(dstLoc[0]);
            fsl.setDestY(dstLoc[1]);
        }
        fsl = hbzFslOrderService.save(fsl);

        if (fsl.getOrderTrans() == OrderTrans.NEW || fsl.getOrderTrans() == OrderTrans.CONFIRMED) {
            switch (fslMapDTO.getSettlementType()) {
                case ONLINE_PAYMENT: {

                    if (fsl.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
                        return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "订单已经发布无法再确认");
                    }

                    //TODO 判断保证金 - 整车专线 - 在线支付
                    HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                    boolean ec2000exists = false;
                    boolean pcExists = false;
                    List<HbzBondDTO> ec2000;
                    List<HbzBondDTO> c500 = null;
                    //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                    if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                        //判断企业货主保存金是否存在
                        ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                        ec2000exists = ec2000.size() > 0;
                    }
                    //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                    if (!ec2000exists) {
                        c500 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_FSL", "C500");
                        pcExists = c500.size() > 0;
                    }
                    if (!ec2000exists && !pcExists) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c500.get(0), fsl.getOrderNo(), BizCode.ORDER);
                    }

                    fsl.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
                    fsl.setSettlementType(fslMapDTO.getSettlementType());
                    if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                        return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
                    } else
                        return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
                case MONTHLY_SETTLEMENT: {
                    if (fsl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
                        return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "订单已经确认无法再确认");
                    }
                    fsl.setOrderTrans(OrderTrans.LOCKED_ORDER_DRIVER);
                    fsl.setSettlementType(fslMapDTO.getSettlementType());
                    if (fslMapDTO.getTakeUserId() == null) {
                        return new ResponseDTO(Const.STATUS_ERROR, "接单用户不能为空");
                    }
                    fsl.setTakeUserId(fslMapDTO.getTakeUserId());

                    //TODO 保证金检查冻结
                    HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                    boolean ec2000exists = false;
                    boolean pcExists = false;
                    List<HbzBondDTO> ec2000;
                    List<HbzBondDTO> c500 = null;
                    //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                    if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                        //判断企业货主保存金是否存在
                        ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                        ec2000exists = ec2000.size() > 0;
                    }
                    //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                    if (!ec2000exists) {
                        c500 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_FSL", "C500");
                        pcExists = c500.size() > 0;
                    }
                    if (!ec2000exists && !pcExists) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c500.get(0), fsl.getOrderNo(), BizCode.ORDER);
                    }

                    if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                        return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
                    } else
                        return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
                case LEVY_ONLINE_PAYMENT: {
                    if (fsl.getOrderTrans() == OrderTrans.CONFIRMED) {
                        return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "订单已经确认无法再确认");
                    }
                    fsl.setOrderTrans(OrderTrans.CONFIRMED);
                    fsl.setSettlementType(fslMapDTO.getSettlementType());

                    //TODO 保证金冻结
                    HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                    boolean ec2000exists = false;
                    boolean pcExists = false;
                    List<HbzBondDTO> ec2000;
                    List<HbzBondDTO> c500 = null;
                    //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                    if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                        //判断企业货主保存金是否存在
                        ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                        ec2000exists = ec2000.size() > 0;
                    }
                    //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                    if (!ec2000exists) {
                        c500 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_FSL", "C500");
                        pcExists = c500.size() > 0;
                    }
                    if (!ec2000exists && !pcExists) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c500.get(0), fsl.getOrderNo(), BizCode.ORDER);
                    }

                    if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                        return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                    }
                }
            }
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }

    /**
     * 计算整车价格
     *
     * @param mapDTO
     * @return
     */
    @Override
    public ResponseDTO computePrice(HbzFslMapDTO mapDTO) {

        String srcAddr = mapDTO.getOriginAddress();
        if (StringHelper.notNullAndEmpty(mapDTO.getOriginAreaCode())) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(mapDTO.getOriginAreaCode());
            while (area != null && area.getLevel().intValue() != 0) {
                srcAddr = area.getAreaName() + srcAddr;
                area = area.getParent();
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无发货区域");
        }

        String dstAddr = mapDTO.getDestAddress();
        if (StringHelper.notNullAndEmpty(mapDTO.getDestAreaCode())) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(mapDTO.getDestAreaCode());
            while (area != null && area.getLevel() != 0) {
                dstAddr = area.getAreaName() + dstAddr;
                area = area.getParent();
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无目标区域");
        }

        Double[] sLoc = mapService.getLocationByAddress(srcAddr);
        Double[] dLoc = mapService.getLocationByAddress(dstAddr);

        MapRouteMatrixDTO route = new MapRouteMatrixDTO();

        MapAddressDTO originAdd = new MapAddressDTO();
        originAdd.setLng(sLoc[0]);
        originAdd.setLat(sLoc[1]);

        MapAddressDTO destAdd = new MapAddressDTO();
        destAdd.setLng(dLoc[0]);
        destAdd.setLat(dLoc[1]);

        route.setTactics(13);
        route.setOperator("driving");
        route.setOrigins(Arrays.asList(originAdd));
        route.setDestinations(Arrays.asList(destAdd));

        Map<String, Object> calc = mapService.route(route);
        List<Map<String, Object>> result = (List) calc.get("result");
        Map<String, Object> it = result.get(0);
        Double distance = Double.valueOf((String) it.get("distance"));

        Map<String, Double> var = new LinkedHashMap<>();
        var.put("d", distance);
        var.put("v", mapDTO.getCommodityVolume());
        var.put("w", mapDTO.getCommodityWeight());

        switch (mapDTO.getCommodityType()) {
            case Heavy: {
                Map<String, Object> info = new LinkedHashMap<>();
                Double price = hbzFormulaService.calculate(heavyFormula, var);
                info.put("price", new DecimalFormat("#0.00").format(price));
                info.put("unitPrice", new DecimalFormat("#0.00").format(price / mapDTO.getCommodityWeight() / (distance / 1000)) + "元/KG/KM");
                info.put("distance", new DecimalFormat("#0.00").format(distance / 1000) + "千米");
                try {
                    return new ResponseDTO(Const.STATUS_OK, "价格计算", info);
                } catch (Exception e) {
                    return new ResponseDTO(Const.STATUS_ERROR, "计算失败", e.getMessage());
                }
            }
            case Light: {
                Map<String, Object> info = new LinkedHashMap<>();
                Double price = hbzFormulaService.calculate(lightFormula, var);
                info.put("price", new DecimalFormat("#0.00").format(price));
                info.put("unitPrice", new DecimalFormat("#0.00").format(price / mapDTO.getCommodityVolume() / (distance / 1000)) + "元/M³/KM");
                info.put("distance", new DecimalFormat("#0.00").format(distance / 1000) + "千米");
                try {
                    return new ResponseDTO(Const.STATUS_OK, "价格计算", info);
                } catch (Exception e) {
                    return new ResponseDTO(Const.STATUS_ERROR, "计算失败", e.getMessage());
                }
            }
            default: {
                return new ResponseDTO(Const.STATUS_ERROR, "不支持的货物类型");
            }
        }
    }

    /**
     * 车主 同意整车订单
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO agreeDrvFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
            fsl.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }

    /**
     * 车主 拒绝整车订单
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO refuseDrvFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
            fsl.setOrderTrans(OrderTrans.CONFIRMED);
            fsl.setTakeUser(null);
            fsl.setTakeUserId(null);
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }

    /**
     * 货主 分页条件查询我创建的整车订单
     *
     * @param query
     * @return
     */
    @Override
    public ResponseDTO queryFslOrderPage(HbzFslMapDTO query) {
        HbzFslOrderDTO queryDTO = new HbzFslOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setStatus(Const.STATUS_ENABLED);
        queryDTO.setCreateUserId(hbzUserService.currentUser().getId());
        Page<HbzFslOrderDTO> list = hbzFslOrderService.queryPage(queryDTO, queryDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapFsl));
    }

    /**
     * 货主-车主 获取整车专线详情
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO getFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(fsl).map(MapSpec::mapFsl).get());
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        }
    }

    /**
     * 获取详情，修改用
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO loadFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(fsl).map(MapSpec::mapFsl).get());
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        }
    }

    /**
     * 车主 接整车单
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO carryFslOrder(HbzFslMapDTO fslMapDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            HbzUserDTO admin = hbzUserService.getAdministrator(user.getId());
            fsl.setOrderTrans(OrderTrans.LOCKED_ORDER);
            fsl.setAgent(user);
            fsl.setAgentId(user.getId());
            fsl.setAgentTime(System.currentTimeMillis());
            fsl.setTakeUser(admin);
            fsl.setTakeUserId(admin.getId());
            fsl.setTakeTime(System.currentTimeMillis());

            //TODO 判断保证金 - 整车专线
            HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
            boolean ed2000exists = false;
            boolean pdExists = false;
            List<HbzBondDTO> ed2000;
            List<HbzBondDTO> d500 = null;
            //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
            if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                //判断企业货主保存金是否存在
                ed2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "ED2000");
                ed2000exists = ed2000.size() > 0;
            }
            //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
            if (!ed2000exists) {
                d500 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_FSL", "D500");
                pdExists = d500.size() > 0;
            }
            if (!ed2000exists && !pdExists) {
                return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
            } else if (!ed2000exists && pdExists) {
                //冻结一笔保证金，并且使用该订单编号
                hbzPledgeService.pledge(d500.get(0), fsl.getOrderNo(), BizCode.ORDER);
            }

            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 货主 同意司机接运
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO agreeFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.LOCKED_ORDER) {
            fsl.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 货主 拒绝司机
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO refuseFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.LOCKED_ORDER) {
            fsl.setDealUser(null);
            fsl.setDealUserId(null);
            fsl.setTakeUserId(null);
            fsl.setTakeUser(null);
            fsl.setAgent(null);
            fsl.setAgentId(null);
            fsl.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            //解除该订单冻结的保证金
            hbzPledgeService.unPledge(fsl.getOrderNo(), BizCode.ORDER, "BOND_FSL", "D500");
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 装货
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO takeFslOrder(HbzFslMapDTO fslMapDTO) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            fsl.setOrderTrans(OrderTrans.TRANSPORT);
            fsl.setDealUser(u);
            fsl.setDealUserId(u.getId());
            fsl.setDealTime(System.currentTimeMillis());
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 确认送达
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO comppleteFslOrder(HbzFslMapDTO fslMapDTO) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO query = new HbzFslOrderDTO();
        query.setId(fslMapDTO.getId());
        HbzFslOrderDTO fsl = hbzFslOrderService.get(query);
        if (fsl.getOrderTrans() == OrderTrans.TRANSPORT || fsl.getSettlementType() == SettlementType.LEVY_ONLINE_PAYMENT) {
            fsl.setCompleteImage(fslMapDTO.getCompleteImage());
            fsl.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 货主 确认签收
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO receiveFslOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO fsl = hbzFslOrderService.findById(fslMapDTO.getId());
        if (!administrator.getId().equals(fsl.getCreateUserId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "对不起，不是你创建的订单你没有操作该订单权限");
        }
        if (Arrays.asList(OrderTrans.WAIT_TO_CONFIRM, OrderTrans.TRANSPORT).contains(fsl.getOrderTrans()) || fsl.getSettlementType() == SettlementType.LEVY_ONLINE_PAYMENT) {
            if (fslMapDTO.getPaySelection() != null) {

                switch (fslMapDTO.getPaySelection().intValue()) {
                    case 1:
                        fsl.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
                        break;
                    case 2:
                        fsl.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT_OFFLINE);
                        break;
                    default:
                        return new ResponseDTO(Const.STATUS_ERROR, "请指定有效的付款方式");
                }
                //非车辆征集的指定订单金额
                if (fsl.getSettlementType() != SettlementType.LEVY_ONLINE_PAYMENT) {
                    if (fslMapDTO.getAmount() == null) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请指定一个订单金额");
                    } else {
                        fsl.setAmount(fslMapDTO.getAmount());
                    }
                }

                if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                    hbzPledgeService.unPledge(fsl.getOrderNo(), BizCode.ORDER, "BOND_FSL", "D500");
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                    return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
                } else
                    return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "请选择一种付款方式", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 确认收款
     *
     * @param fslMapDTO
     * @return
     */
    @Override
    public ResponseDTO endOrder(HbzFslMapDTO fslMapDTO) {
        String[] err = ValidationHelper.valid(fslMapDTO, "fsl_order_confirm");
        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzFslOrderDTO fsl = hbzFslOrderService.findById(fslMapDTO.getId());
        if (!administrator.getId().equals(fsl.getTakeUserId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "对不起，你没有操作该订单权限");
        }
        //等待线下支付中的状态，才能由司机点完成收款
        if (fsl.getOrderTrans() == OrderTrans.WAIT_FOR_PAYMENT_OFFLINE) {
            fsl.setOrderTrans(OrderTrans.LIQUIDATION_COMPLETED);
            if ((fsl = hbzFslOrderService.save(fsl)) != null) {
                hbzPledgeService.unPledge(fsl.getOrderNo(), BizCode.ORDER, "BOND_FSL", "C500");
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(fsl, fsl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), fsl.getOrderTrans(), System.currentTimeMillis()));
                HbzRateDTO consignorRate = new HbzRateDTO();
                consignorRate.setUserId(fsl.getCreateUserId());
                consignorRate.setUser(fsl.getCreateUser());
                consignorRate.setOrder(fsl);
                consignorRate.setOrderId(fsl.getId());
                consignorRate.setType(RateType.CONSIGNOR);
                consignorRate.setStar(0);
                consignorRate.setStatus("1");
                rates.save(consignorRate);

                HbzRateDTO tranRate = new HbzRateDTO();
                tranRate.setUser(fsl.getTakeUser());
                tranRate.setUserId(fsl.getTakeUserId());
                tranRate.setOrder(fsl);
                tranRate.setOrderId(fsl.getId());
                tranRate.setType(RateType.PROVIDER);
                tranRate.setStar(0);
                tranRate.setStatus("1");
                rates.save(tranRate);

                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 分页条件查询已接整车订单
     *
     * @param query
     * @return
     */
    @Override
    public ResponseDTO queryTaskFslOrderPage(HbzFslMapDTO query) {
        HbzFslOrderDTO queryDTO = new HbzFslOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setTakeUserId(hbzUserService.currentUser().getId());
        queryDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzFslOrderDTO> list = hbzFslOrderService.queryPage(queryDTO, queryDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapFsl));
    }

}
