package com.troy.keeper.hbz.app.rest.web.fsl_and_ltl_facade;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzLtlMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.*;
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
public class LtlFacadeImpl implements LtlFacade {

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzLtlOrderService hbzLtlOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    public LtlFacadeImpl me;

    @Config("com.hbz.location.x.width")
    private Double xWidth;

    @Config("com.hbz.location.y.width")
    private Double yWidth;

    @Autowired
    private MapService mapService;

    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    @Config("com.hbz.price.ltl.formula.heavy")
    private String heavyFormula;

    @Config("com.hbz.price.ltl.formula.light")
    private String lightFormula;

    @Autowired
    HbzFormulaService hbzFormulaService;

    @Autowired
    HbzRateService rate;

    public LtlFacadeImpl() {
        me = this;
    }

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    HbzPledgeService hbzPledgeService;

    /**
     * 货主 新建零担
     * @param ltlOrderCreateDTO
     * @return
     */
    @Override
    public ResponseDTO createLtlOrder(HbzLtlMapDTO ltlOrderCreateDTO) {
        String[] err = ValidationHelper.valid(ltlOrderCreateDTO, "ltl_order_create");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzLtlOrderDTO ltl = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(ltlOrderCreateDTO, ltl,
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        );
        String orderNo = hbzOrderService.createNewOrderNo(OrderType.LTL);
        ltl.setOrderNo(orderNo);
        ltl.setStatus(Const.STATUS_ENABLED);
        ltl.setOrderTrans(OrderTrans.NEW);
        ltl.setOrderType(OrderType.LTL);
        ltl.setOriginArea(hbzAreaService.findByOutCode(ltlOrderCreateDTO.getOriginAreaCode()));
        if (ltl.getOriginArea() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "发货区域编码[" + ltlOrderCreateDTO.getOriginAreaCode() + "]无效");
        ltl.setDestArea(hbzAreaService.findByOutCode(ltlOrderCreateDTO.getDestAreaCode()));
        if (ltl.getDestArea() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "收货区域编码[" + ltlOrderCreateDTO.getDestAreaCode() + "]无效");
        ltl.setCreateUser(hbzUserService.currentUser());
        ltl.setCreateTime(System.currentTimeMillis());
        if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", ltl.getId());
            data.put("orderNo", ltl.getOrderNo());
            hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
            return new ResponseDTO(Const.STATUS_OK, "保存成功", data);
        } else
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
    }

    /**
     * 货主 确认零担
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO confirmLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        if (ltlMapDTO.getSettlementType() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "结算方法为空", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在");
        }
        if (!ltl.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单");
        }
        String srcAddr = ltl.getOriginAddress();
        if (ltl.getOriginArea() != null) {
            HbzAreaDTO area = ltl.getOriginArea();
            while (area != null && area.getLevel() != 0) {
                srcAddr = area.getAreaName() + srcAddr;
                area = area.getParent();
            }
        }
        String dstAddr = ltl.getDestAddress();
        if (ltl.getDestArea() != null) {
            HbzAreaDTO area = ltl.getDestArea();
            while (area != null && area.getLevel() != 0) {
                dstAddr = area.getAreaName() + dstAddr;
                area = area.getParent();
            }
        }

        Double[] srcLocation = mapService.getLocationByAddress(srcAddr);
        Double[] dstLoc = mapService.getLocationByAddress(dstAddr);
        if (srcLocation != null) {
            ltl.setOriginX(srcLocation[0]);
            ltl.setOriginY(srcLocation[1]);
        }
        if (dstLoc != null) {
            ltl.setDestX(dstLoc[0]);
            ltl.setDestY(dstLoc[1]);
        }
        ltl = hbzLtlOrderService.save(ltl);

        if (ltl.getOrderTrans() == OrderTrans.NEW || ltl.getOrderTrans() == OrderTrans.CONFIRMED) {
            switch (ltlMapDTO.getSettlementType()) {
                case ONLINE_PAYMENT: {
                    if (ltl.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
                        return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "订单已经确认无法再确认");
                    }

                    //TODO 判断保证金 - 零担专线
                    HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                    boolean ec2000exists = false;
                    boolean pcExists = false;
                    List<HbzBondDTO> ec2000;
                    List<HbzBondDTO> c200 = null;
                    //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                    if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                        //判断企业货主保存金是否存在
                        ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                        ec2000exists = ec2000.size() > 0;
                    }
                    //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                    if (!ec2000exists) {
                        c200 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_LTL", "C200");
                        pcExists = c200.size() > 0;
                    }
                    if (!ec2000exists && !pcExists) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c200.get(0), ltl.getOrderNo(), BizCode.ORDER);
                    }

                    ltl.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
                    ltl.setSettlementType(ltlMapDTO.getSettlementType());
                    if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                        return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
                    } else
                        return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
                case MONTHLY_SETTLEMENT: {
                    if (ltl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
                        return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "订单已经确认无法再确认");
                    }
                    ltl.setOrderTrans(OrderTrans.LOCKED_ORDER_DRIVER);
                    ltl.setSettlementType(ltlMapDTO.getSettlementType());
                    if (ltlMapDTO.getTakeUserId() == null) {
                        return new ResponseDTO(Const.STATUS_ERROR, "接单用户id不能为空");
                    }

                    ltl.setTakeUserId(ltlMapDTO.getTakeUserId());


                    //TODO 判断保证金 - 零担专线
                    HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                    boolean ec2000exists = false;
                    boolean pcExists = false;
                    List<HbzBondDTO> ec2000;
                    List<HbzBondDTO> c200 = null;
                    //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                    if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                        //判断企业货主保存金是否存在
                        ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                        ec2000exists = ec2000.size() > 0;
                    }
                    //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                    if (!ec2000exists) {
                        c200 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_LTL", "C200");
                        pcExists = c200.size() > 0;
                    }
                    if (!ec2000exists && !pcExists) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c200.get(0), ltl.getOrderNo(), BizCode.ORDER);
                    }

                    if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                        return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
                    } else
                        return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
                case LEVY_ONLINE_PAYMENT: {
                    if (ltl.getOrderTrans() == OrderTrans.CONFIRMED) {
                        return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "订单已经确认无法再确认");
                    }
                    ltl.setOrderTrans(OrderTrans.CONFIRMED);
                    ltl.setSettlementType(ltlMapDTO.getSettlementType());

                    //TODO 判断保证金 - 零担专线
                    HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                    boolean ec2000exists = false;
                    boolean pcExists = false;
                    List<HbzBondDTO> ec2000;
                    List<HbzBondDTO> c200 = null;
                    //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                    if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                        //判断企业货主保存金是否存在
                        ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                        ec2000exists = ec2000.size() > 0;
                    }
                    //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                    if (!ec2000exists) {
                        c200 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_LTL", "C200");
                        pcExists = c200.size() > 0;
                    }
                    if (!ec2000exists && !pcExists) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c200.get(0), ltl.getOrderNo(), BizCode.ORDER);
                    }

                    if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                        return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
                    } else
                        return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
            }
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }

    /**
     * 计算零担价格
     *
     * @param mapDTO
     * @return
     */
    @Override
    public ResponseDTO computePrice(HbzLtlMapDTO mapDTO) {

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
                info.put("unitPrice", new DecimalFormat("#0.00").format(price / (distance / 1000) / mapDTO.getCommodityVolume()) + "元/M³/KM");
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
     * 车主 月结 同意零担
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO agreeDrvLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
            ltl.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }

    /**
     * 车主 月结 拒绝零担
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO refuseDrvLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
            ltl.setOrderTrans(OrderTrans.CONFIRMED);
            ltl.setTakeUserId(null);
            ltl.setTakeUser(null);
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }

    public ResponseDTO queryLtlOrder(@RequestBody HbzLtlMapDTO query, HttpServletRequest request, HttpServletResponse response) {
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setCreateUserId(hbzUserService.currentUser().getId());
        queryDTO.setStatus(Const.STATUS_ENABLED);
        List<HbzLtlOrderDTO> list = hbzLtlOrderService.query(queryDTO);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapLtl).collect(Collectors.toList()));
    }


    /**
     * 货主 分页条件查询我的零担
     * @param query
     * @return
     */
    @Override
    public ResponseDTO queryLtlOrderPage(HbzLtlMapDTO query) {
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setStatus(Const.STATUS_ENABLED);
        queryDTO.setCreateUserId(hbzUserService.currentUser().getId());
        Page<HbzLtlOrderDTO> list = hbzLtlOrderService.queryPage(queryDTO, queryDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapLtl));
    }

    /**
     * 双方 获取零担详情
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO getLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(ltl).map(MapSpec::mapLtl).get());
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        }
    }

    /**
     * 修改用 查询零担详情
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO loadLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(ltl).map(MapSpec::mapLtl).get());
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        }
    }

    /**
     * 车主 抢零担单
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO caLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            HbzUserDTO ad = hbzUserService.getAdministrator(u.getId());
            ltl.setAgent(u);
            ltl.setAgentId(u.getId());
            ltl.setOrderTrans(OrderTrans.LOCKED_ORDER);
            ltl.setTakeUser(ad);
            ltl.setTakeUserId(ad.getId());
            ltl.setAgentTime(System.currentTimeMillis());
            ltl.setTakeTime(System.currentTimeMillis());

            //TODO 判断保证金 - 零担专线
            HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
            boolean ed2000exists = false;
            boolean pdExists = false;
            List<HbzBondDTO> ed2000;
            List<HbzBondDTO> d200 = null;
            //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
            if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                //判断企业货主保存金是否存在
                ed2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "ED2000");
                ed2000exists = ed2000.size() > 0;
            }
            //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
            if (!ed2000exists) {
                d200 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_LTL", "D200");
                pdExists = d200.size() > 0;
            }
            if (!ed2000exists && !pdExists) {
                return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金");
            } else if (!ed2000exists && pdExists) {
                //冻结一笔保证金，并且使用该订单编号
                hbzPledgeService.pledge(d200.get(0), ltl.getOrderNo(), BizCode.ORDER);
            }

            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 货主 月结 同意零担
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO agreeLlsOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.LOCKED_ORDER) {
            ltl.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 货主 月结 拒绝零担
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO refuseLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "tlt_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.LOCKED_ORDER) {
            ltl.setTakeUserId(null);
            ltl.setTakeUser(null);
            ltl.setDealUserId(null);
            ltl.setDealUser(null);
            ltl.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            //解除该订单冻结的司机保证金
            hbzPledgeService.unPledge(ltl.getOrderNo(), BizCode.ORDER, "BOND_LTL", "D200");
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 确认装货
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO takeLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            ltl.setOrderTrans(OrderTrans.TRANSPORT);
            ltl.setDealUser(u);
            ltl.setDealUserId(u.getId());
            ltl.setDealTime(System.currentTimeMillis());
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 确认签收
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO comppleteLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getOrderTrans() == OrderTrans.TRANSPORT || ltl.getSettlementType() == SettlementType.LEVY_ONLINE_PAYMENT) {
            ltl.setCompleteImage(ltlMapDTO.getCompleteImage());
            ltl.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 货主 确认收货
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO receiveLtlOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO ltl = hbzLtlOrderService.findById(ltlMapDTO.getId());
        if (!administrator.getId().equals(ltl.getCreateUserId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "对不起，不是你创建的订单你没有操作该订单权限");
        }
        if (Arrays.asList(OrderTrans.WAIT_TO_CONFIRM, OrderTrans.TRANSPORT).contains(ltl.getOrderTrans()) || ltl.getSettlementType() == SettlementType.LEVY_ONLINE_PAYMENT) {
            if (ltlMapDTO.getPaySelection() != null) {
                switch (ltlMapDTO.getPaySelection().intValue()) {
                    case 1:
                        ltl.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
                        break;
                    case 2:
                        ltl.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT_OFFLINE);
                        break;
                    default:
                        return new ResponseDTO(Const.STATUS_ERROR, "请指定有效的付款方式");
                }
                //非车辆征集的指定订单金额
                if (ltl.getSettlementType() != SettlementType.LEVY_ONLINE_PAYMENT) {
                    if (ltlMapDTO.getAmount() == null) {
                        return new ResponseDTO(Const.STATUS_ERROR, "请指定一个订单金额");
                    } else {
                        ltl.setAmount(ltlMapDTO.getAmount());
                    }
                }
                if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                    hbzPledgeService.unPledge(ltl.getOrderNo(), BizCode.ORDER, "BOND_LTL", "D200");
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
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
     * @param ltlMapDTO
     * @return
     */
    @Override
    public ResponseDTO endOrder(HbzLtlMapDTO ltlMapDTO) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO ltl = hbzLtlOrderService.findById(ltlMapDTO.getId());
        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
        if (!administrator.getId().equals(ltl.getTakeUserId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "对不起，你没有操作该订单权限");
        }
        //线下支持中的货款可以由司机点击已收到
        if (ltl.getOrderTrans() == OrderTrans.WAIT_FOR_PAYMENT_OFFLINE) {
            ltl.setOrderTrans(OrderTrans.LIQUIDATION_COMPLETED);
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                hbzPledgeService.unPledge(ltl.getOrderNo(), BizCode.ORDER, "BOND_LTL", "C200");
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                HbzRateDTO consignorRate = new HbzRateDTO();
                consignorRate.setUserId(ltl.getCreateUserId());
                consignorRate.setUser(ltl.getCreateUser());
                consignorRate.setOrder(ltl);
                consignorRate.setOrderId(ltl.getId());
                consignorRate.setType(RateType.CONSIGNOR);
                consignorRate.setStar(0);
                consignorRate.setStatus("1");
                rate.save(consignorRate);
                HbzRateDTO tranRate = new HbzRateDTO();
                tranRate.setUser(ltl.getTakeUser());
                tranRate.setStatus("1");
                tranRate.setUserId(ltl.getTakeUserId());
                tranRate.setOrder(ltl);
                tranRate.setOrderId(ltl.getId());
                tranRate.setType(RateType.PROVIDER);
                tranRate.setStar(0);
                rate.save(tranRate);
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);

        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    /**
     * 车主 分页条件查询我的已接运订单
     * @param query
     * @return
     */
    @Override
    public ResponseDTO queryTaskLtlOrderPage(HbzLtlMapDTO query) {
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setTakeUserId(hbzUserService.currentUser().getId());
        queryDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzLtlOrderDTO> list = hbzLtlOrderService.queryPage(queryDTO, queryDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapLtl));
    }

}
