package com.troy.keeper.hbz.app.rest;

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
import com.troy.keeper.hbz.service.wrapper.AddInfo;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/10.
 */
@RestController
public class HbzLtlOrderResource {

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzLtlOrderService hbzLtlOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    public HbzLtlOrderResource me;

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

    public HbzLtlOrderResource() {
        me = this;
    }

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    HbzPledgeService hbzPledgeService;

    @Autowired
    SitePushMessageService sitePushMessageService;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzCoordinateService hbzCoordinateService;

    @Label("App端 - 零担专线 - 货主方 - 创建订单")
    @RequestMapping(value = "/api/order/ltl/create", method = RequestMethod.POST)
    public ResponseDTO createLtlOrder(@RequestBody HbzLtlMapDTO ltlOrderCreateDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App - 专线 - 零担 - 发货方 - 删除订单")
    @RequestMapping(value = "/api/order/ltl/delete", method = RequestMethod.POST)
    public ResponseDTO deleteFslOrder(@RequestBody HbzLtlMapDTO ltlOrderCreateDTO, HttpServletRequest request, HttpServletResponse response) {
        if (ltlOrderCreateDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id为空，请提供订单id");
        }
        HbzLtlOrderDTO ltl = hbzLtlOrderService.findById(ltlOrderCreateDTO.getId());
        switch (ltl.getOrderTrans()) {
            case NEW:
            case CONFIRMED:
                ltl.setOrderTrans(OrderTrans.INVALID);
                ltl.setStatus("0");
                hbzLtlOrderService.save(ltl);
                break;
            default:
                return new ResponseDTO(Const.STATUS_ERROR, "错误！此状态订单不能被删除");
        }
        return new ResponseDTO(Const.STATUS_OK, "操作成功！", null);
    }

    @Label("App端 - 零担专线 - 货主方 - 确认")
    @RequestMapping(value = "/api/order/ltl/confirm", method = RequestMethod.POST)
    public ResponseDTO confirmLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金", "NeedBond");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c200.get(0), ltl.getOrderNo(), BizCode.ORDER);
                    }

                    ltl.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
                    ltl.setSettlementType(ltlMapDTO.getSettlementType());
                    if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                        HbzCoordinateDTO coordinateQuery = new HbzCoordinateDTO();
                        AddInfo addInfo = mapService.getLocationX(ltl.getOriginX(), ltl.getOriginY());
                        if (addInfo != null) {
                            HbzAreaDTO hbzArea = hbzAreaService.findByOutCode(addInfo.getCityCode());
                            if (hbzArea != null) {
                                coordinateQuery.setStatus("1");
                                coordinateQuery.setAreaId(hbzArea.getId());
                                coordinateQuery.setSyncMillisGE(System.currentTimeMillis() - 1800L * 1000L);
                                List<HbzCoordinateDTO> list = hbzCoordinateService.query(coordinateQuery);

                                List<HbzUserDTO> users = list.stream().map(HbzCoordinateDTO::getUser).filter(user -> {
                                    HbzRoleDTO roleQuery = new HbzRoleDTO();
                                    roleQuery.setStatus("1");
                                    roleQuery.setUserId(user.getId());
                                    roleQuery.setRoles(Arrays.asList(Role.EnterpriseAdmin, Role.PersonDriver));
                                    return hbzRoleService.count(roleQuery) > 0;
                                }).collect(Collectors.toList());
                                sitePushMessageService.sendMessageImmediately(users,
                                        "货源通知消息",
                                        "有新的货源",
                                        "您有新的货源消息啦\n" + ltl.getOriginAddress() + "-->" + ltl.getDestAddress() + "（" + ltl.getCommodityName() + "/" + ltl.getCommodityWeight() + "／" + ltl.getCommodityVolume() + "），现在联系货主！");
                            }
                        }
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
                        return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金", "NeedBond");
                    } else if (!ec2000exists && pcExists) {
                        //冻结一笔保证金，并且使用该订单编号
                        hbzPledgeService.pledge(c200.get(0), ltl.getOrderNo(), BizCode.ORDER);
                    }

                    if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                        sitePushMessageService.sendMessageImmediately(Arrays.asList(hbzUserService.findById(ltl.getTakeUserId())),
                                "订单通知消息",
                                "有新的订单!",
                                "恭喜您，车辆征集中您被货主选定为运货司机，快到收货订单中查看订单详情吧。");
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
     * 计算价格
     *
     * @param mapDTO
     * @return
     */
    @Label("App端 - 零担专线 - 货主方 - 预估价格计算")
    @RequestMapping("/api/order/ltl/price")
    public ResponseDTO computePrice(@RequestBody HbzLtlMapDTO mapDTO) {

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

    @Label("App端 - 零担专线 - 货运方 - 月结 - 同意接运")
    @RequestMapping(value = "/api/order/ltl/driving/agree", method = RequestMethod.POST)
    public ResponseDTO agreeDrvLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货运方 - 月结 - 不同意接运")
    @RequestMapping(value = "/api/order/ltl/driving/refuse", method = RequestMethod.POST)
    public ResponseDTO refuseDrvLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败", err);
        }
        HbzLtlOrderDTO query = new HbzLtlOrderDTO();
        query.setId(ltlMapDTO.getId());
        HbzLtlOrderDTO ltl = hbzLtlOrderService.get(query);
        if (ltl.getSettlementType() == SettlementType.MONTHLY_SETTLEMENT && ltl.getOrderTrans() == OrderTrans.LOCKED_ORDER_DRIVER) {
            ltl.setOrderTrans(OrderTrans.CONFIRMED);
            ltl.setTakeUserId(null);
            ltl.setTakeUser(null);
            if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                //解冻该订单所有的货主保存证金
                hbzPledgeService.unPledge(ltl.getOrderNo(), BizCode.ORDER, "BOND_LTL", "C200");
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "确认成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
        }
    }


    @Label("App端 - 零担专线 - 货主方 - 查询我发的订单")
    @RequestMapping(value = "/api/order/ltl/query", method = RequestMethod.POST)
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


    @Label("App端 - 零担专线 - 货主方 - 查询我发的订单 分页")
    @RequestMapping(value = "/api/order/ltl/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryLtlOrderPage(@RequestBody HbzLtlMapDTO query, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货主方、货运方 - 详细信息")
    @RequestMapping(value = "/api/order/ltl/get", method = RequestMethod.POST)
    public ResponseDTO getLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货主方、货运方 - 货物详细信息")
    @RequestMapping(value = "/api/order/ltl/load", method = RequestMethod.POST)
    public ResponseDTO loadLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货运方 - 查找货源")
    @RequestMapping(value = "/api/order/ltl/task/near/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskLtlOrderNear(@RequestBody HbzLtlMapDTO query, HttpServletRequest request, HttpServletResponse response) {
        String[] err = ValidationHelper.valid(query, "ltl_near_search_task");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", err);
        }
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));

        if (query.getLocationX() != null && query.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * query.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (query.getDistance() != null) {
                distanceX = 1D / 111D * query.getDistance() * Math.cos(Math.PI / 4 * query.getLocationY() / 90);
                distanceY = 1D / 111D * query.getDistance();
            }
            //用户在范围内
            queryDTO.setOriginXLE(query.getLocationX() + distanceX);
            queryDTO.setOriginXGE(query.getLocationX() - distanceX);

            queryDTO.setOriginYLE(query.getLocationY() + distanceY);
            queryDTO.setOriginYGE(query.getLocationY() - distanceY);
        } else if (query.getLocationX() != null || query.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个");
        }

        //订单必须是待接运
        queryDTO.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
        queryDTO.setOrderTakeStartGE(System.currentTimeMillis());
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        queryDTO.setSorts(sorts);
        //订单是正常状态
        queryDTO.setStatus(Const.STATUS_ENABLED);
        List<HbzLtlOrderDTO> list = hbzLtlOrderService.query(queryDTO);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapLtl).collect(Collectors.toList()));
    }

    @Label("App端 - 零担专线 - 货运方 - 查询货源 分页")
    @RequestMapping(value = "/api/order/ltl/task/near/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryTaskLtlOrderNearPage(@RequestBody HbzLtlMapDTO query, HttpServletRequest request, HttpServletResponse response) {
        String[] err = ValidationHelper.valid(query, "ltl_near_search_task");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", err);
        }
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));

        if (query.getLocationX() != null && query.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * query.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (query.getDistance() != null) {
                distanceX = 1D / 111D * query.getDistance() * Math.cos(Math.PI / 4 * query.getLocationY() / 90);
                distanceY = 1D / 111D * query.getDistance();
            }
            //用户在范围内
            queryDTO.setOriginXLE(query.getLocationX() + distanceX);
            queryDTO.setOriginXGE(query.getLocationX() - distanceX);

            queryDTO.setOriginYLE(query.getLocationY() + distanceY);
            queryDTO.setOriginYGE(query.getLocationY() - distanceY);
        } else if (query.getLocationX() != null || query.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个");
        }

        //订单必须是待接运
        queryDTO.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
        queryDTO.setOrderTakeStartGE(System.currentTimeMillis());

        //订单是正常状态
        queryDTO.setStatus(Const.STATUS_ENABLED);
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        queryDTO.setSorts(sorts);
        Page<HbzLtlOrderDTO> page = hbzLtlOrderService.queryPage(queryDTO, queryDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapLtl));
    }

    //抢零担专线订单 -- 司机端
    // TODO 接单人要依角色修改为公司账号
    @RequestMapping(value = "/api/order/ltl/carry", method = RequestMethod.POST)
    public ResponseDTO caLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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
                return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金", "NeedBond");
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

    @Label("App端 - 零担专线 - 货主方 - 同意接运")
    @RequestMapping(value = "/api/order/ltl/agree", method = RequestMethod.POST)
    public ResponseDTO agreeLlsOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货主方 - 不同意接运")
    @RequestMapping(value = "/api/order/ltl/refuse", method = RequestMethod.POST)
    public ResponseDTO refuseLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货运方 - 已取货")
    @RequestMapping(value = "/api/order/ltl/take", method = RequestMethod.POST)
    public ResponseDTO takeLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货运方 - 确认送达")
    @RequestMapping(value = "/api/order/ltl/complete", method = RequestMethod.POST)
    public ResponseDTO comppleteLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    @Label("App端 - 零担专线 - 货主方 - 确认收货")
    @RequestMapping(value = "/api/order/ltl/receive", method = RequestMethod.POST)
    public ResponseDTO receiveLtlOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
        String[] err = ValidationHelper.valid(ltlMapDTO, "ltl_order_confirm");
        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzLtlOrderDTO ltl = hbzLtlOrderService.findById(ltlMapDTO.getId());
        if (ltlMapDTO.getAmount() != null)
            ltl.setAmount(ltlMapDTO.getAmount());
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

                if ((ltl = hbzLtlOrderService.save(ltl)) != null) {
                    hbzPledgeService.unPledge(ltl.getOrderNo(), BizCode.ORDER, "BOND_LTL", "D200");
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                    return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
                } else
                    return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
            } else {
                if (ltl.getSettlementType() == SettlementType.MONTHLY_SETTLEMENT) {
                    ltl.setOrderTrans(OrderTrans.LIQUIDATION_COMPLETED);
                    hbzPledgeService.unPledge(ltl.getOrderNo(), BizCode.ORDER, "BOND_LTL", "D200");
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ltl, ltl.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ltl.getOrderTrans(), System.currentTimeMillis()));
                    hbzLtlOrderService.save(ltl);

                    /**
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
                     **/

                    return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
                }
                return new ResponseDTO(Const.STATUS_ERROR, "请选择一种付款方式", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Label("App端 - 零担专线 - 货运方 - 确认收款")
    @RequestMapping(value = "/api/order/ltl/end", method = RequestMethod.POST)
    public ResponseDTO endOrder(@RequestBody HbzLtlMapDTO ltlMapDTO, HttpServletRequest request, HttpServletResponse response) {
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

    //查询零担专线订单 -- 司机端
    @RequestMapping(value = "/api/order/ltl/task/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskLtlOrder(@RequestBody HbzLtlMapDTO query, HttpServletRequest request, HttpServletResponse response) {
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setTakeUserId(hbzUserService.getAdministrator(hbzUserService.currentUser().getId()).getId());
        queryDTO.setStatus(Const.STATUS_ENABLED);
        List<HbzLtlOrderDTO> list = hbzLtlOrderService.query(queryDTO);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapLtl).collect(Collectors.toList()));
    }

    //查询零担专线订单 -- 司机端
    @RequestMapping(value = "/api/order/ltl/task/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryTaskLtlOrderPage(@RequestBody HbzLtlMapDTO query, HttpServletRequest request, HttpServletResponse response) {
        HbzLtlOrderDTO queryDTO = new HbzLtlOrderDTO();
        BeanHelper.copyProperties(query, queryDTO,
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeStartGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse));
        queryDTO.setTakeUserId(hbzUserService.getAdministrator(hbzUserService.currentUser().getId()).getId());
        queryDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzLtlOrderDTO> list = hbzLtlOrderService.queryPage(queryDTO, queryDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapLtl));
    }

}
