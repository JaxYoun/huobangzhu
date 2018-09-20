package com.troy.keeper.management.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.Bean2Map;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.type.*;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.service.SmUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.troy.keeper.management.utils.CommonConstants.commonIgnores;

/**
 * Created by leecheng on 2017/11/27.
 */
@Slf4j
public class MapSpec {

    public static Map<String, Object> mapOrder(HbzOrderDTO order) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea)
        ).map(order);
    }

    public static Map<String, Object> mapOrderRecord(HbzOrderRecordDTO dto) {
        Map<String, Object> m = new Bean2Map().addIgnores(CommonConstants.commonIgnores).addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("timeMillis", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("order", MapSpec::mapTypeOrder),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(dto);
        if (dto.getOrderTrans() != null) {
            m.put("orderTransValue", dto.getOrderTrans().getName());
        }
        return m;
    }

    public static Map<String, Object> mapSendOrder(HbzSendOrderDTO order) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format)
        ).map(order);
    }

    public static Map<String, Object> mapTaker(HbzTakerInfoDTO taker) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUserWithRegistry),
                new PropertyMapper<>("agent", MapSpec::mapUser)
        ).addIgnores(commonIgnores).addIgnores("order").map(taker);
        Map<String, Object> order = mapTypeOrder(taker.getOrder());
        if (order != null) {
            order.remove("user");
            map.put("order", order);
        }
        return map;
    }

    public static Map<String, Object> mapEnterpriseConsignor(HbzEnterpriseConsignorRegistryDTO transEnterpriseRegistryDTO) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(transEnterpriseRegistryDTO);
    }

    public static Map<String, Object> mapPersonDriverRegistry(HbzPersonDriverRegistryDTO personDriverRegistry) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("drivingValidity", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(personDriverRegistry);
    }

    public static Map<String, Object> mapTransEnterprise(HbzTransEnterpriseRegistryDTO transEnterpriseRegistryDTO) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(transEnterpriseRegistryDTO);
    }

    public static Map<String, Object> mapPersonConsignor(HbzPersonConsignorRegistryDTO personConsignorRegistryDTO) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(personConsignorRegistryDTO);
    }

    public static Map<String, Object> mapA(HbzDeliveryBoyRegistryDTO deliveryBoyRegistryDTO) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).map(deliveryBoyRegistryDTO);
    }

    public static Map<String, Object> mapRole(HbzRoleDTO r) {
        return new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(commonIgnores).map(r);
    }

    public static Map<String, Object> mapBuyOrder(HbzBuyOrderDTO order) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format)
        ).map(order);
    }

    public static Map<String, Object> mapTender(HbzTenderDTO t) {
        return new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("order", MapSpec::mapOrder)
        ).map(t);
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

    public static Map<String, Object> maperEx(HbzExOrderDTO order) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea)
        ).addIgnores(
                commonIgnores
        ).map(order);
        return map;
    }

    public static Map<String, Object> mapUser(HbzUserDTO user) {
        Map<String, Object> userInfo = new Bean2Map().addIgnores(commonIgnores).addIgnores(
                "firstName",
                "lastName",
                "email",
                "activated",
                "langKey",
                //"imageUrl",
                "activationKey",
                "resetKey",
                "resetDate", "password").addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("org", MapSpec::mapOrg),
                new PropertyMapper<>("ent", MapSpec::mapOrg)
        ).map(user);
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
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(order);
        if (order.getRelatedPictures() != null && order.getRelatedPictures().length() > 0) {
            try {
                JsonArray imA = new Gson().fromJson(order.getRelatedPictures(), JsonArray.class);
                List<String> urls = new LinkedList<>();
                imA.forEach(im -> {
                    String t = im.getAsString();
                    String url = "/api/binary/s/" + t;
                    urls.add(url);
                });
                map.put("relatedPictures", urls);
            } catch (Exception e) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                e.printStackTrace(ps);
                ps.flush();
                log.debug("发生异常[{}]", new String(baos.toByteArray()));
            }
        }
        return map;
    }

    public static Map<String, Object> mapArea(HbzAreaDTO area) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format
                )).addIgnores(commonIgnores).addIgnores("parent").map(area);
        HbzAreaDTO parent = area;
        while (parent != null) {
            map.put("level" + parent.getLevel() + "Code", parent.getAreaCode());
            map.put("level" + parent.getLevel() + "AreaCode", parent.getOutCode());
            map.put("level" + parent.getLevel() + "Name", parent.getAreaName());
            map.put("level" + parent.getLevel() + "id", parent.getId());
            parent = parent.getParent();
        }
        return map;
    }

    public static Map<String, Object> mapLtl(HbzLtlOrderDTO order) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("destlimit", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("orderTakeStart", new TimeMillisFormater("yyyy-MM-dd HH:mm")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("agent", MapSpec::mapUser),
                new PropertyMapper<>("createUser", MapSpec::mapUser),
                new PropertyMapper<>("takeUser", MapSpec::mapUser),
                new PropertyMapper<>("dealUser", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(order);
        if (order.getRelatedPictures() != null && order.getRelatedPictures().length() > 0) {
            try {
                JsonArray imA = new Gson().fromJson(order.getRelatedPictures(), JsonArray.class);
                List<String> urls = new LinkedList<>();
                imA.forEach(im -> {
                    String t = im.getAsString();
                    String url = "/api/binary/s/" + t;
                    urls.add(url);
                });
                map.put("relatedPictures", urls);
            } catch (Exception e) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                e.printStackTrace(ps);
                ps.flush();
                log.debug("发生异常[{}]", new String(baos.toByteArray()));
            }
        }
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
        ).addIgnores(commonIgnores).map(fsl);
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
        ).addIgnores(commonIgnores).map(ltl);
    }

    @SneakyThrows
    public static Map<String, Object> mapDriverLineDTO(HbzDriverLineDTO driverLine) {
        HbzDriverLineService hbzDriverLineService = ApplicationContextHolder.getService((Class<HbzDriverLineService>) Class.forName("com.troy.keeper.hbz.service.HbzDriverLineService"));
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea)
        ).addIgnores("transSizes").addIgnores(commonIgnores)
                .map(driverLine);
        List<Map<String, Object>> transSizes = hbzDriverLineService.queryTransSizes(driverLine).stream()
                .map(MapSpec::mapTransSize).collect(Collectors.toList());
        map.put("transSizes", transSizes);
        return map;
    }

    public static Map<String, Object> mapTransSize(HbzTransSizeDTO dto) {
        Map<String, Object> mapper = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(commonIgnores).map(dto);
        return mapper;
    }

    public static Map<String, Object> mapLinkInfo(HbzLinkInfoDTO link) {
        Map<String, Object> mapper = new Bean2Map().addPropMapper(
                new PropertyMapper<>("area", MapSpec::mapArea),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(link);
        return mapper;
    }

    public static Map<String, Object> mapWareType(HbzWareTypeDTO dto) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("parent", MapSpec::mapWareType)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    @SneakyThrows
    public static Map<String, Object> mapWareInfo(HbzWareInfoDTO dto) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("type", MapSpec::mapWareType),
                new PropertyMapper<>("brand", MapSpec::mapBrand)
        ).addIgnores(commonIgnores).map(dto);

        SmUserService smUserService = ApplicationContextHolder.getService(SmUserService.class);
        SmUserDTO smUserQuery = new SmUserDTO();
        smUserQuery.setId(dto.getCreatedBy());
        Map<String, Object> createdBy = smUserService.get(smUserQuery);

        smUserQuery.setId(dto.getLastUpdatedBy());
        Map<String, Object> lastUpdatedBy = smUserService.get(smUserQuery);

        map.put("createdBy", createdBy.get("userName"));
        map.put("lastUpdatedBy", lastUpdatedBy.get("userName"));
        map.put("lastUpdatedDate", new TimeMillisFormater("yyyy-MM-dd").format(dto.getLastUpdatedDate()));

        return map;
    }

    public static Map<String, Object> mapBrand(HbzBrandDTO dto) {
        return new Bean2Map().addPropMapper(
                new PropertyMapper<>("area", MapSpec::mapArea),
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(commonIgnores).map(dto);
    }

    @SneakyThrows
    public static Map<String, Object> mapProduct(HbzProductDTO dto) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("ware", MapSpec::mapWareInfo)
        ).addIgnores(commonIgnores).map(dto);

        SmUserService smUserService = ApplicationContextHolder.getService(SmUserService.class);
        SmUserDTO smUserQuery = new SmUserDTO();
        smUserQuery.setId(dto.getCreatedBy());
        Map<String, Object> createdBy = smUserService.get(smUserQuery);

        smUserQuery.setId(dto.getLastUpdatedBy());
        Map<String, Object> lastUpdatedBy = smUserService.get(smUserQuery);

        map.put("createdBy", createdBy.get("userName"));
        map.put("lastUpdatedBy", lastUpdatedBy.get("userName"));
        map.put("lastUpdatedDate", new TimeMillisFormater("yyyy-MM-dd").format(dto.getLastUpdatedDate()));

        HbzRecommendProductDTO rq = new HbzRecommendProductDTO();
        rq.setStatus("1");
        rq.setProductId(dto.getId());
        Long count = ApplicationContextHolder.getService(HbzRecommendProductService.class).count(rq);
        map.put("recount", count);

        return map;
    }

    @SneakyThrows
    public static Map<String, Object> mapRecommendProduc(HbzRecommendProductDTO product) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("product", MapSpec::mapProduct)
        ).addIgnores(commonIgnores).map(product);


        SmUserService smUserService = ApplicationContextHolder.getService(SmUserService.class);
        SmUserDTO smUserQuery = new SmUserDTO();
        smUserQuery.setId(product.getCreatedBy());
        Map<String, Object> createdBy = smUserService.get(smUserQuery);

        smUserQuery.setId(product.getLastUpdatedBy());
        Map<String, Object> lastUpdatedBy = smUserService.get(smUserQuery);

        map.put("createdBy", createdBy.get("userName"));
        map.put("lastUpdatedBy", lastUpdatedBy.get("userName"));
        map.put("lastUpdatedDate", new TimeMillisFormater("yyyy-MM-dd").format(product.getLastUpdatedDate()));

        return map;
    }

    public static Map<String, Object> mapScoreOrder(HbzScoreOrderDTO order) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("area", MapSpec::mapArea),
                new PropertyMapper<>("product", MapSpec::mapProduct),
                new PropertyMapper<>("scoreTime", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(order);
        HbzExpressInvoiceService hbzExpressInvoiceService = ApplicationContextHolder.getService(HbzExpressInvoiceService.class);
        HbzExpressInvoiceDTO queryInvoice = new HbzExpressInvoiceDTO();
        queryInvoice.setStatus("1");
        queryInvoice.setOrderNo(order.getOrderNo());
        List<HbzExpressInvoiceDTO> invoices = hbzExpressInvoiceService.query(queryInvoice);
        if (invoices != null && invoices.size() > 0) {
            map.put("sendTime", new TimeMillisFormat("yyyy-MM-dd").format(invoices.get(0).getSendTime()));
        }
        return map;
    }

    public static Map<String, Object> mapExpressInvoice(HbzExpressInvoiceDTO dto) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("sendTime", new TimeMillisFormat("yyyy-MM-dd")::format)
        ).addIgnores(commonIgnores).map(dto);
        map.put("expressCompanyTypeValue", dto.getExpressCompanyType().getName());
        return map;
    }

    public static Map<String, Object> mapMenu(HbzMenuDTO dto) {
        Map<String, Object> map = new Bean2Map().addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("parent", MapSpec::mapMenu)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapAuth(HbzAuthDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapRate(HbzRateDTO rate) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser),
                new PropertyMapper<>("order", MapSpec::mapTypeOrder)
        ).addIgnores(commonIgnores).map(rate);
        return map;
    }

    public static Map<String, Object> mapResource(HbzURLDTO d) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format)
        ).addIgnores(commonIgnores).map(d);
        return map;
    }

    public static Map<String, Object> mapSource(HbzSourceDTO src) {
        Map<String, Object> map = new Bean2Map(new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format)).addIgnores(commonIgnores).map(src);
        return map;
    }

    public static Map<String, Object> mapOrganization(HbzPlatformOrganizationDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("addressArea", MapSpec::mapArea)
        ).addIgnores(commonIgnores).map(dto);
        HbzPlatformOrganizationService hbzPlatformOrganizationService = ApplicationContextHolder.getService(HbzPlatformOrganizationService.class);
        List<HbzAreaDTO> areas = hbzPlatformOrganizationService.getAreas(hbzPlatformOrganizationService.findById(dto.getId()));
        map.put("serviceAreas", areas.stream().map(MapSpec::mapArea).collect(Collectors.toList()));
        return map;
    }

    public static Map<String, Object> mapOrg(HbzOrgDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormater("yyyy-MM-dd")::format),
                new PropertyMapper<>("parent", MapSpec::mapOrg)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapAssignWork(HbzAssignWorkDTO dto) {
        Map<String, Object> map = new Bean2Map().addIgnores(commonIgnores).addPropMapper(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("originArea", MapSpec::mapArea),
                new PropertyMapper<>("destArea", MapSpec::mapArea),
                new PropertyMapper<>("platformOrganization", MapSpec::mapOrganization),
                new PropertyMapper<>("assignTime", new TimeMillisFormat("yyyy-MM-dd")::format)
        ).map(dto);
        return map;
    }

    public static Map<String, Object> mapFormula(HbzFormulaDTO dto) {
        Map<String, Object> map = new Bean2Map(new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format)).addIgnores(commonIgnores).addPropMapper().map(dto);
        return map;
    }

    public static Map<String, Object> mapCreditRecord(HbzCreditRecordDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("time", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(dto);
        if (dto.getAdjustType() != null) {
            HbzTypeValService hbzTypeValService = ApplicationContextHolder.getService(HbzTypeValService.class);
            HbzTypeValDTO tv = hbzTypeValService.getByTypeAndValAndLanguage("CREDIT_SCORE_ADJUEST_TYPE", dto.getAdjustType(), "zh_CN");
            if (tv != null) {
                map.put("adjustTypeTypeName", tv.getName());
            }
        }
        return map;
    }

    public static Map<String, Object> mapScoreChange(HbzScoreChangeDTO dto) {
        Map<String, Object> map = new Bean2Map(
                new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("time", new TimeMillisFormat("yyyy-MM-dd")::format),
                new PropertyMapper<>("user", MapSpec::mapUser)
        ).addIgnores(commonIgnores).map(dto);
        return map;
    }

    public static Map<String, Object> mapBill(HbzBillDTO dto) {
        Map<String, Object> map = new Bean2Map()
                .addPropMapper(
                        new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
                        new PropertyMapper<>("date", new TimeMillisFormat("yyyy-MM-dd")::format)
                )
                .addIgnores(CommonConstants.commonIgnores)
                .map(dto);
        if (dto.getBillType() != null) {
            map.put("billTypeVal", dto.getBillType().getName());
        }
        if (dto.getPayType() != null) {
            map.put("payTypeVal", dto.getPayType().getName());
        }
        return map;
    }

    public static Map<String, Object> mapPay(HbzPayDTO dto) {
        Map<String, Object> map = new Bean2Map()
                .addPropMapper(new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format))
                .addIgnores(CommonConstants.commonIgnores)
                .map(dto);
        if (dto.getBusinessType() != null) {
            map.put("businessTypeValue", dto.getBusinessType().getName());
        }
        if (dto.getPayType() != null) {
            map.put("payTypeValue", dto.getPayType().getName());
        }
        if (dto.getPayProgress() != null) {
            map.put("payProgressValue", dto.getPayProgress().getName());
        }
        HbzRefundService hbzRefundService = ApplicationContextHolder.getService(HbzRefundService.class);
        HbzRefundDTO refundQuery = new HbzRefundDTO();
        refundQuery.setStatus("1");
        refundQuery.setRefundStatuses(Arrays.asList(RefundStatus.SUCCESS, RefundStatus.SUBMIT, RefundStatus.REFUNDING, RefundStatus.NEW));
        refundQuery.setPayId(dto.getId());
        refundQuery.setTradeNo(dto.getTradeNo());
        Long count = hbzRefundService.count(refundQuery);
        map.put("refundCount", count);
        return map;
    }

    public static Map<String, Object> mapRefund(HbzRefundDTO dto) {
        Map<String, Object> map = new Bean2Map()
                .addPropMapper(
                        new PropertyMapper<>("createdDate", new TimeMillisFormat("yyyy-MM-dd")::format),
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

}
