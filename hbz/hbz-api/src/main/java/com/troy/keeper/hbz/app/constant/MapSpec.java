package com.troy.keeper.hbz.app.constant;

import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.Bean2Map;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.type.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.troy.keeper.hbz.app.constant.CommonConstants.commonIgnores;

/**
 * Created by leecheng on 2017/11/27.
 */
@Slf4j
public class MapSpec {

    public static Map<String, Object> mapOrder(HbzOrderDTO order) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("dealTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea)
        ).map(order);
    }

    public static Map<String, Object> mapSendOrder(HbzSendOrderDTO order) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("dealTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format)
        ).map(order);
    }

    public static Map<String, Object> mapTaker(HbzTakerInfoDTO taker) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUserWithRegistry),
                new PropertyMapper<>("agent", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).addIgnores("order").map(taker);
        Map<String, Object> order = mapTypeOrder(taker.getOrder());
        switch (taker.getTakeType()) {
            case TAKE:
                map.put("takeStatus", "被选中");
                break;
            case DISABLE:
                map.put("takeStatus", "未被选中");
                break;
            case TAKEING:
                map.put("takeStatus", "未定");
                break;
        }
        if (order != null) {
            order.remove("user");
            map.put("order", order);
        }
        return map;
    }

    public static Map<String, Object> mapEnterpriseConsignor(HbzEnterpriseConsignorRegistryDTO transEnterpriseRegistryDTO) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(transEnterpriseRegistryDTO);
    }

    public static Map<String, Object> mapPersonDriverRegistry(HbzPersonDriverRegistryDTO personDriverRegistry) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("drivingValidity", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(personDriverRegistry);
    }

    public static Map<String, Object> mapTransEnterprise(HbzTransEnterpriseRegistryDTO transEnterpriseRegistryDTO) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(transEnterpriseRegistryDTO);
    }

    public static Map<String, Object> mapPersonConsignor(HbzPersonConsignorRegistryDTO personConsignorRegistryDTO) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(personConsignorRegistryDTO);
    }

    public static Map<String, Object> mapA(HbzDeliveryBoyRegistryDTO deliveryBoyRegistryDTO) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(deliveryBoyRegistryDTO);
    }

    public static Map<String, Object> mapRole(HbzRoleDTO r) {
        return new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(CommonConstants.commonIgnores).map(r);
    }

    public static Map<String, Object> mapBuyOrder(HbzBuyOrderDTO order) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("dealTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format)
        ).map(order);
    }

    public static Map<String, Object> mapTender(HbzTenderDTO t) {
        return new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("order", MapSpec::mapOrder)
        ).map(t);
    }

    public static Map<String, Object> maperEx(HbzExOrderDTO order) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("dealTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea)
        ).addIgnores(
                CommonConstants.commonIgnores
        ).map(order);
        return map;
    }

    public static Map<String, Object> mapRegistry(HbzUserRegistryDTO dto) {
        Map<String, Object> map = new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(dto);
        map.put("registryCodeValue", dto.getRegistryCode().getName());
        map.put("registryProgressVal", dto.getRegistryProgress().getName());
        return map;
    }

    public static Map<String, Object> mapUser(HbzUserDTO user) {
        Map<String, Object> userInfo = new Bean2Map().addIgnores(commonIgnores).addIgnores(
                "firstName",
                "lastName",
                "langKey",
                "activationKey",
                "resetKey",
                "resetDate",
                "password").addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("org", MapSpec::mapOrg),
                new PropertyMapper<>("ent", MapSpec::mapOrg)
        ).map(user);

        HbzScoreChangeService hbzScoreChangeService = ApplicationContextHolder.getService(HbzScoreChangeService.class);
        HbzUserService hbzUserService = ApplicationContextHolder.getService(HbzUserService.class);
        Integer spendScore = hbzScoreChangeService.countByDelta(hbzUserService.currentUser().getId());
        HbzScoreService hbzScoreService = ApplicationContextHolder.getService(HbzScoreService.class);
        HbzScoreDTO hbzScoreDTO = hbzScoreService.attach(hbzUserService.currentUser().getId());
        if (user.getImageUrl() == null) {
            userInfo.put("imageUrl", null);
        } else if (user.getImageUrl().contains("http://")) {
            userInfo.put("imageUrl", user.getImageUrl());
        } else {
            userInfo.put("imageUrl", ApplicationContextHolder.getProp("staticImagePrefix") + user.getImageUrl());
        }
        if (spendScore == null) {
            //使用分数
            userInfo.put("spendScore", 0);
            //当前用户积分
            userInfo.put("currentScore", hbzScoreDTO.getScore());
            //绝对值相加分数
            userInfo.put("totalScore", hbzScoreDTO.getScore());
        } else {
            userInfo.put("spendScore", spendScore * (-1));
            userInfo.put("currentScore", hbzScoreDTO.getScore());
            userInfo.put("totalScore", hbzScoreDTO.getScore() + (spendScore * (-1)));
        }
        return userInfo;
    }

    public static Map<String, Object> mapUserRoleId(HbzUserDTO user) {
        Map<String, Object> userInfo = mapUser(user);
        HbzRoleService hbzRoleService = ApplicationContextHolder.getService(HbzRoleService.class);
        HbzRoleDTO query = new HbzRoleDTO();
        query.setStatus("1");
        query.setUserId(user.getId());
        List<HbzRoleDTO> roles = hbzRoleService.query(query);
        userInfo.put("roleIds", roles.stream().map(HbzRoleDTO::getId).collect(Collectors.toList()));
        return userInfo;
    }

    public static Map<String, Object> mapUserWithRegistry(HbzUserDTO user) {
        Map<String, Object> s = new HashMap<>();
        s.putAll(MapSpec.mapUser(user));
        HbzPersonDriverRegistryDTO driverRegistry = ApplicationContextHolder.getService(HbzPersonDriverRegistryService.class).find(user, true);
        HbzTransEnterpriseRegistryDTO trans = ApplicationContextHolder.getService(HbzTransEnterpriseRegistryService.class).findTransEnterpriseRegistry(user, true);
        if (driverRegistry != null) {
            Map<String, Object> PersonDriver = MapSpec.mapPersonDriverRegistry(driverRegistry);
            PersonDriver.remove("user");
            s.put("PersonDriver", PersonDriver);
        }
        if (trans != null) {
            Map<String, Object> TransEnterprise = MapSpec.mapTransEnterprise(trans);
            TransEnterprise.remove("user");
            s.put("TransEnterprise", TransEnterprise);
            s.put("orgName", trans.getOrganizationName());
        }
        return s;
    }

    public static Map<String, Object> mapFsl(HbzFslOrderDTO order) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("dealTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).map(order);
        return map;
    }

    public static Map<String, Object> mapCreditRecord(HbzCreditRecordDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("time", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapArea(HbzAreaDTO area) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format
                )).addIgnores(CommonConstants.commonIgnores).addIgnores("parent").map(area);
        HbzAreaDTO parent = area;
        StringBuilder fullName = new StringBuilder();
        while (parent != null) {
            map.put("level" + parent.getLevel() + "Code", parent.getAreaCode());
            map.put("level" + parent.getLevel() + "AreaCode", parent.getOutCode());
            map.put("level" + parent.getLevel() + "Name", parent.getAreaName());
            map.put("level" + parent.getLevel() + "id", parent.getId());
            if (fullName.length() > 0) fullName.insert(0, "-");
            fullName.insert(0, parent.getAreaName());
            parent = parent.getParent();
        }
        map.put("fullName", fullName);
        return map;
    }

    public static Map<String, Object> mapLtl(HbzLtlOrderDTO order) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("dealTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).map(order);
        return map;
    }

    public static Map<String, Object> mapRate(HbzRateDTO rate) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser),
                new PropertyMapper<>("order", MapSpec::mapTypeOrder)
        ).addIgnores(CommonConstants.commonIgnores).map(rate);
        return map;
    }

    public static Map<String, Object> mapFsl4display(HbzFslOrderDTO fsl) {
        return new Bean2Map(
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("commodityClass", CommodityClass::getName),
                new PropertyMapper<>("weightUnit", WeightUnit::getName),
                new PropertyMapper<>("volumeUnit", VolumeUnit::getName),
                new PropertyMapper<>("transType", TransType::getName),
                new PropertyMapper<>("settlementType", SettlementType::getName),
                new PropertyMapper<>("orderTrans", OrderTrans::getName),
                new PropertyMapper<>("orderType", OrderType::getName),
                new PropertyMapper<>("amount", new DecimalFormat("###0.00")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).map(fsl);
    }

    public static Map<String, Object> mapTypeOrder(HbzOrderDTO order) {
        HbzFslOrderService hbzFslOrderService = ApplicationContextHolder.getService(HbzFslOrderService.class);
        HbzLtlOrderService hbzLtlOrderService = ApplicationContextHolder.getService(HbzLtlOrderService.class);
        HbzBuyOrderServices hbzBuyOrderServices = ApplicationContextHolder.getService(HbzBuyOrderServices.class);

        HbzSendOrderService hbzSendOrderService = ApplicationContextHolder.getService(HbzSendOrderService.class);
        HbzExOrderService hbzExOrderService = ApplicationContextHolder.getService(HbzExOrderService.class);
        switch (order.getOrderType()) {
            case FSL: {
                HbzFslOrderDTO q = new HbzFslOrderDTO();
                q.setId(order.getId());
                return mapFsl(hbzFslOrderService.get(q));
            }
            case LTL: {
                HbzLtlOrderDTO q = new HbzLtlOrderDTO();
                q.setId(order.getId());
                return mapLtl(hbzLtlOrderService.get(q));
            }
            case BUY: {
                HbzBuyOrderDTO q = new HbzBuyOrderDTO();
                q.setId(order.getId());
                return MapSpec.mapBuyOrder(hbzBuyOrderServices.get(q));
            }
            case SEND: {
                HbzSendOrderDTO q = new HbzSendOrderDTO();
                q.setId(order.getId());
                return mapSendOrder(hbzSendOrderService.get(q));
            }
            case EX: {
                HbzExOrderDTO q = new HbzExOrderDTO();
                q.setId(order.getId());
                return maperEx(hbzExOrderService.get(q));
            }
            default: {
                return null;
            }
        }
    }

    public static Map<String, Object> mapLtl4display(HbzLtlOrderDTO ltl) {
        return new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("commodityClass", CommodityClass::getName),
                new PropertyMapper<>("weightUnit", WeightUnit::getName),
                new PropertyMapper<>("volumeUnit", VolumeUnit::getName),
                new PropertyMapper<>("transType", TransType::getName),
                new PropertyMapper<>("settlementType", SettlementType::getName),
                new PropertyMapper<>("orderTrans", OrderTrans::getName),
                new PropertyMapper<>("orderType", OrderType::getName),
                new PropertyMapper<>("amount", new DecimalFormat("###0.00")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).map(ltl);
    }

    @SneakyThrows
    public static Map<String, Object> mapDriverLineDTO(HbzDriverLineDTO driverLine) {
        HbzDriverLineService hbzDriverLineService = ApplicationContextHolder.getService((Class<HbzDriverLineService>) Class.forName("com.troy.keeper.hbz.service.HbzDriverLineService"));
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea)
        ).addIgnores("transSizes").addIgnores(CommonConstants.commonIgnores)
                .map(driverLine);
        List<Map<String, Object>> transSizes = hbzDriverLineService.queryTransSizes(driverLine).stream()
                .map(MapSpec::mapTransSize).collect(Collectors.toList());
        map.put("transSizes", transSizes);
        return map;
    }

    public static Map<String, Object> mapTransSize(HbzTransSizeDTO dto) {
        Map<String, Object> mapper = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(CommonConstants.commonIgnores).map(dto);
        return mapper;
    }

    public static Map<String, Object> mapLinkInfo(HbzLinkInfoDTO link) {
        Map<String, Object> mapper = new Bean2Map().addPropMapper(
                new PropertyMapper<>("area", MapSpec::mapArea),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).map(link);
        return mapper;
    }

    public static Map<String, Object> mapWareType(HbzWareTypeDTO dto) {
        return new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("parent", MapSpec::mapWareType)
        ).addIgnores(CommonConstants.commonIgnores).map(dto);
    }

    public static Map<String, Object> mapWareInfo(HbzWareInfoDTO dto) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("type", MapSpec::mapWareType),
                new PropertyMapper<>("brand", MapSpec::mapBrand)
        ).addIgnores(CommonConstants.commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapBrand(HbzBrandDTO dto) {
        return new Bean2Map().addPropMapper(
                new PropertyMapper<>("area", MapSpec::mapArea),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(CommonConstants.commonIgnores).map(dto);
    }

    public static Map<String, Object> mapProduct(HbzProductDTO dto) {
        return new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("ware", MapSpec::mapWareInfo)
        ).addIgnores(CommonConstants.commonIgnores).map(dto);
    }

    public static Map<String, Object> mapRecommendProduc(HbzRecommendProductDTO product) {
        return new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("product", MapSpec::mapProduct)
        ).addIgnores(CommonConstants.commonIgnores).map(product);
    }

    public static Map<String, Object> mapReimburse(HbzReimburseDTO dto) {
        Map<String, Object> m = new Bean2Map().addIgnores(CommonConstants.commonIgnores).
                addPropMapper(
                        new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd HH:mm")::format)
                ).map(dto);
        return m;
    }

    public static Map<String, Object> mapOrderRecord(HbzOrderRecordDTO dto) {
        Map<String, Object> m = new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("timeMillis", new TimeMillisFormat("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("order", MapSpec::mapTypeOrder),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(dto);
        m.put("createdDate", null);
        return m;
    }

    public static Map<String, Object> mapScoreOrder(HbzScoreOrderDTO order) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("area", MapSpec::mapArea),
                new PropertyMapper<>("scoreTime", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("product", MapSpec::mapProduct),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(CommonConstants.commonIgnores).map(order);
        return map;
    }

    public static Map<String, Object> mapBondGrade(HbzBondGradeDTO grade) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format)
        ).addIgnores(CommonConstants.commonIgnores).map(grade);
        return map;
    }

    public static Map<String, Object> mapExpressInvoice(HbzExpressInvoiceDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("sendTime", new TimeMillisFormat("yyyy-MM-dd")::format)
        ).addIgnores(CommonConstants.commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapOrganization(HbzPlatformOrganizationDTO dto) {
        Map<String, Object> map = new Bean2Map().addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapRefund(HbzRefundDTO dto) {
        Map<String, Object> map = new Bean2Map()
                .addPropMapper(
                        new PropertyMapper<>("createTime", new TimeMillisFormat("yyyy-MM-dd")::format),
                        new PropertyMapper<>("pay", MapSpec::mapPay),
                        new PropertyMapper<>("user", MapSpec::mapUser)
                )
                .addIgnores(CommonConstants.commonIgnores)
                .map(dto);
        if (dto.getRefundStatus() != null) {
            map.put("refundStatusValue", dto.getRefundStatus().getName());
        }
        return map;
    }

    public static Map<String, Object> mapOrg(HbzOrgDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("parent", MapSpec::mapOrg)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapOrgWithoutParent(HbzOrgDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(StringHelper.conact(commonIgnores, "parent")).map(dto);
        return map;
    }

    public static Map<String, Object> mapScoreChange(HbzScoreChangeDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("time", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapBond(HbzBondDTO dto) {
        Map<String, Object> map = new Bean2Map()
                .addPropMapper(
                        new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                        new PropertyMapper<>("user", MapSpec::mapUser),
                        new PropertyMapper<>("bondGrade", MapSpec::mapBondGrade)
                )
                .addIgnores(CommonConstants.commonIgnores)
                .map(dto);
        return map;
    }

    public static Map<String, Object> mapPay(HbzPayDTO dto) {
        Map<String, Object> map = new Bean2Map()
                .addPropMapper(new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format))
                .addIgnores(CommonConstants.commonIgnores)
                .map(dto);
        return map;
    }

}