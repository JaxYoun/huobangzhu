package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.export.ExcelHandleUtil;
import com.troy.keeper.hbz.app.dto.HbzOrderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.ExcelUtils;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.type.AddressTypeEnum;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.vo.excel.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：Excel批量导出
 * @DateTime：2018/1/4 23:25
 */
@Slf4j
@RestController
@RequestMapping("/api/web/export")
public class WebExcelExportResource {

    private static final String[] directCityOutCodeArr = {"110000", "310100", "120000", "500000"};
    private static final List<String> directCityOutCodeList = Arrays.asList(directCityOutCodeArr);

    @Autowired
    private HbzFslOrderService hbzFslOrderService;

    @Autowired
    private HbzLtlOrderService hbzLtlOrderService;

    @Autowired
    private HbzBuyOrderServices hbzBuyOrderServices;

    @Autowired
    private HbzSendOrderService hbzSendOrderService;

    @Autowired
    private HbzExOrderService hbzExOrderService;

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzUserService hbzUserService;

    @Deprecated
    @PostMapping("/exportExcelTest")
    public void exportExcelTest(HttpServletResponse response, HbzOrderMapDTO hbzOrderMapDTO) {
        System.out.println(hbzOrderMapDTO.getCreateDateStart());
        /*List<HbzDriverLineVO> resultList = new ArrayList<>();
        resultList.add(new HbzDriverLineVO("成都市高新区", "广州市白云区", "重卡", "34吨", "12米", "123万"));
        ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(HbzDriverLineVO.class), resultList, response, "123.xls");*/
    }

    /**
     * 1.订单类型：必填
     * 2.创建时间段：
     * 3.专线、快递：可选到市或县L
     * 4.配送：可选到区县或县
     * 5.递单状态：
     * <p>
     * 货主-导出我的发运订单，包括【5】种类型
     *
     * @param response
     */
    @PostMapping("/exportMyCreatedOrder")
    public void exportMyCreatedOrder(HttpServletResponse response, @RequestBody HbzOrderMapDTO hbzOrderMapDTO) {
        hbzOrderMapDTO.setCreateUserId(hbzUserService.currentUser().getId());
        exportOrderByOrderType(response, hbzOrderMapDTO, ExportRoleEnum.MY_CREATE);
    }

    /**
     * 车主-收运订单导出-包括【4】种类型
     *
     * @param response
     * @param hbzOrderMapDTO
     */
    @PostMapping("/exportMyTakenOrder")
    public void exportMyTakenOrder(HttpServletResponse response, @RequestBody HbzOrderMapDTO hbzOrderMapDTO) {
        hbzOrderMapDTO.setTakeUserId(hbzUserService.currentUser().getId());
        exportOrderByOrderType(response, hbzOrderMapDTO, ExportRoleEnum.MY_TAKEN);
    }

    /**
     * 导出方法
     *
     * @param response
     * @param hbzOrderMapDTO
     * @param exportRole
     */
    private void exportOrderByOrderType(HttpServletResponse response, HbzOrderMapDTO hbzOrderMapDTO, ExportRoleEnum exportRole) {
        OrderType orderType = hbzOrderMapDTO.getOrderType();
        if (orderType != null && orderType instanceof OrderType) {
            hbzOrderMapDTO.setStatus(Const.STATUS_ENABLED);

            //创建起始日期
            String createDateStart = hbzOrderMapDTO.getCreateDateStart();
            if (StringUtils.isNotBlank(createDateStart)) {
                try {
                    Long createDateLongGE = DateUtils.stringToLong(createDateStart, DateUtils.yyyy_MM_dd_HH_mm);
                    hbzOrderMapDTO.setCreateDateGE(createDateLongGE);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            //创建截至日期
            String createDateEnd = hbzOrderMapDTO.getCreateDateEnd();
            if (StringUtils.isNotBlank(createDateStart)) {
                try {
                    Long createDateLongLE = DateUtils.stringToLong(createDateEnd, DateUtils.yyyy_MM_dd_HH_mm);
                    hbzOrderMapDTO.setCreateDateLE(createDateLongLE);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            //发货区域
            String originAreaOutCode = hbzOrderMapDTO.getOriginAreaCode();
            if (StringUtils.isNotBlank(originAreaOutCode)) {
                HbzAreaDTO originAreaDto = hbzAreaService.findByOutCode(originAreaOutCode);
                if (originAreaDto.getLevel() == 3 || (originAreaDto.getLevel() == 2 && directCityOutCodeList.contains(originAreaDto.getParent().getOutCode()))) {  //普通区县和直辖县
                    hbzOrderMapDTO.setOriginAreaId(originAreaDto.getId());
                } else if (originAreaDto.getLevel() == 2 || (originAreaDto.getLevel() == 1 && directCityOutCodeList.contains(originAreaDto.getOutCode()))) {  //普通市和直辖市
                    List<HbzAreaDTO> sons = this.hbzAreaService.getAreaByParent(originAreaDto);
                    hbzOrderMapDTO.setOriginAreaIdList(sons.stream().map(HbzAreaDTO::getId).collect(Collectors.toList()));
                    hbzOrderMapDTO.setOriginAreaCode(null);
                }
            }

            //送货区域
            String destAreaOutCode = hbzOrderMapDTO.getDestAreaCode();
            if (StringUtils.isNotBlank(destAreaOutCode)) {
                HbzAreaDTO destAreaDto = hbzAreaService.findByOutCode(destAreaOutCode);
                if (destAreaDto.getLevel() == 3 || (destAreaDto.getLevel() == 2 && directCityOutCodeList.contains(destAreaDto.getParent().getOutCode()))) {  //普通区县和直辖县
                    hbzOrderMapDTO.setDestAreaId(destAreaDto.getId());
                } else if (destAreaDto.getLevel() == 2 || (destAreaDto.getLevel() == 1 && directCityOutCodeList.contains(destAreaDto.getOutCode()))) {  //普通市和直辖市
                    hbzOrderMapDTO.setDestAreaIdList(this.hbzAreaService.getAreaByParent(destAreaDto).stream().map(HbzAreaDTO::getId).collect(Collectors.toList()));
                    hbzOrderMapDTO.setDestAreaCode(null);
                }
            }

            /**
             * 分类导出
             */
            switch (orderType) {
                case FSL: {
                    HbzFslOrderDTO hbzFslOrderDTO = new HbzFslOrderDTO();
                    BeanHelper.copyProperties(hbzOrderMapDTO, hbzFslOrderDTO);
                    List<HbzFslOrderDTO> hbzFslOrderDTOList = hbzFslOrderService.query(hbzFslOrderDTO);
                    List<FslOrderExcelVO> fslOrderExcelVOList = hbzFslOrderDTOList.stream().map(it -> this.convertFslDtoToExcelVo(it)).collect(Collectors.toList());

                    ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(FslOrderExcelVO.class, this.getIgnoreArrByOrderTypeAndExportRole(orderType, exportRole)), fslOrderExcelVOList, response, generateExcelFileName(exportRole.getName(), orderType));
                    break;
                }
                case LTL: {
                    HbzLtlOrderDTO hbzLtlOrderDTO = new HbzLtlOrderDTO();
                    BeanHelper.copyProperties(hbzOrderMapDTO, hbzLtlOrderDTO);
                    List<HbzLtlOrderDTO> hbzLtlOrderDTOList = hbzLtlOrderService.query(hbzLtlOrderDTO);
                    List<LtlOrderExcelVO> ltlOrderExcelVOList = hbzLtlOrderDTOList.stream().map(it -> this.convertLtlDtoToExcelVo(it)).collect(Collectors.toList());
                    ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(LtlOrderExcelVO.class, this.getIgnoreArrByOrderTypeAndExportRole(orderType, exportRole)), ltlOrderExcelVOList, response, generateExcelFileName(exportRole.getName(), orderType));
                    break;
                }
                case BUY: {
                    HbzBuyOrderDTO hbzBuyOrderDTO = new HbzBuyOrderDTO();
                    BeanHelper.copyProperties(hbzOrderMapDTO, hbzBuyOrderDTO);
                    List<HbzBuyOrderDTO> hbzBuyOrderDTOList = hbzBuyOrderServices.query(hbzBuyOrderDTO);
                    List<BuyOrderExcelVO> buyOrderExcelVOList = hbzBuyOrderDTOList.stream().map(it -> this.convertBuyDtoToExcelVo(it)).collect(Collectors.toList());

                    String[] ignoreArr = {"originProvince", "originCity", "originCounty", "originInfo"};
                    ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(BuyOrderExcelVO.class, ignoreArr), buyOrderExcelVOList, response, generateExcelFileName(exportRole.getName(), orderType));
                    break;
                }
                case SEND: {
                    HbzSendOrderDTO hbzSendOrderDTO = new HbzSendOrderDTO();
                    BeanHelper.copyProperties(hbzOrderMapDTO, hbzSendOrderDTO);
                    List<HbzSendOrderDTO> hbzSendOrderDTOList = this.hbzSendOrderService.query(hbzSendOrderDTO);
                    List<SendOrderExcelVO> sendOrderExcelVOList = hbzSendOrderDTOList.stream().map(it -> this.convertSendDtoToExcelVo(it)).collect(Collectors.toList());
                    ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(SendOrderExcelVO.class), sendOrderExcelVOList, response, generateExcelFileName(exportRole.getName(), orderType));
                    break;
                }
                case EX: {
                    HbzExOrderDTO hbzExOrderDTO = new HbzExOrderDTO();
                    BeanHelper.copyProperties(hbzOrderMapDTO, hbzExOrderDTO);
                    List<HbzExOrderDTO> hbzExdOrderDTOList = this.hbzExOrderService.query(hbzExOrderDTO);
                    List<ExOrderExcelVO> exOrderExcelVOList = hbzExdOrderDTOList.stream().map(it -> this.convertExDtoToExcelVo(it)).collect(Collectors.toList());
                    ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(ExOrderExcelVO.class), exOrderExcelVOList, response, generateExcelFileName(exportRole.getName(), orderType));
                    break;
                }
                default: {
                }
            }
        } else {
            log.error("订单类型不正确！");
        }
    }

    /**
     * 根据订单类型和到处人角色配置忽略列表
     *
     * @param orderType
     * @param exportRole
     * @return
     */
    private String[] getIgnoreArrByOrderTypeAndExportRole(OrderType orderType, ExportRoleEnum exportRole) {
        String[] ignoreArr = null;
        switch (exportRole) {
            case MY_CREATE: {
                switch (orderType) {
                    case FSL:
                    case LTL: {
                        ignoreArr = new String[4];
                        ignoreArr[0] = "originInfo";
                        ignoreArr[1] = "destInfo";
                        ignoreArr[2] = "agent";
                        ignoreArr[3] = "agentTime";
                        break;
                    }
                    case BUY: {
                        ignoreArr = new String[6];
                        ignoreArr[0] = "originProvince";
                        ignoreArr[1] = "originCity";
                        ignoreArr[2] = "originCounty";
                        ignoreArr[3] = "originInfo";
                        ignoreArr[4] = "agent";
                        ignoreArr[5] = "agentTime";
                        break;
                    }
                    case SEND:
                    case EX: {
                        ignoreArr = new String[2];
                        ignoreArr[0] = "agent";
                        ignoreArr[1] = "agentTime";
                        break;
                    }
                    default: {
                    }
                }
                break;
            }
            case MY_TAKEN: {
                switch (orderType) {
                    case FSL:
                    case LTL: {
                        ignoreArr = new String[2];
                        ignoreArr[0] = "originInfo";
                        ignoreArr[1] = "destInfo";
                        break;
                    }
                    case BUY: {
                        ignoreArr = new String[4];
                        ignoreArr[0] = "originProvince";
                        ignoreArr[1] = "originCity";
                        ignoreArr[2] = "originCounty";
                        ignoreArr[3] = "originInfo";
                        break;
                    }
                    case SEND: {
                        break;
                    }
                    case EX: {
                        break;
                    }
                    default: {
                    }
                }
                break;
            }
            default: {
            }
        }
        return ignoreArr;
    }

    /**
     * Excel文件名生成
     *
     * @param orderType
     * @return
     * @throws IllegalArgumentException
     */
    private static String generateExcelFileName(String exportType, OrderType orderType) throws IllegalArgumentException {
        StringBuilder stringBuilder;
        if (orderType != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            stringBuilder = new StringBuilder();
            stringBuilder.append(LocalDateTime.now().format(dateTimeFormatter).toString());
            stringBuilder.append("_");
            stringBuilder.append(exportType);
            stringBuilder.append(orderType.getName());
            stringBuilder.append(".xls");
        } else {
            throw new IllegalArgumentException("请输入正确的订单类型！");
        }
        return stringBuilder.toString();
    }

    /**
     * 整车DTO转换为ExcelVO
     *
     * @param hbzFslOrderDTO
     * @return
     */
    private FslOrderExcelVO convertFslDtoToExcelVo(HbzFslOrderDTO hbzFslOrderDTO) {
        FslOrderExcelVO fslOrderExcelVO = new FslOrderExcelVO();
        BeanUtils.copyProperties(hbzFslOrderDTO, fslOrderExcelVO);
        convertBaseOrderDtoToExcelVo(hbzFslOrderDTO, fslOrderExcelVO);

        if (hbzFslOrderDTO.getCommodityType() != null) {
            fslOrderExcelVO.setCommodityType(hbzFslOrderDTO.getCommodityType().getName());
        }
        if (hbzFslOrderDTO.getWeightUnit() != null) {
            fslOrderExcelVO.setWeightUnit(hbzFslOrderDTO.getWeightUnit().getName());
        }
        if (hbzFslOrderDTO.getVolumeUnit() != null) {
            fslOrderExcelVO.setVolumeUnit(hbzFslOrderDTO.getVolumeUnit().getName());
        }
        if (hbzFslOrderDTO.getTransType() != null) {
            fslOrderExcelVO.setTransType(hbzFslOrderDTO.getTransType().getName());
        }
        if (hbzFslOrderDTO.getOrderTakeStart() != null) {
            fslOrderExcelVO.setOrderTakeStart(DateUtils.longToNoSecondString(hbzFslOrderDTO.getOrderTakeStart()));
        }
        if (hbzFslOrderDTO.getDestlimit() != null) {
            fslOrderExcelVO.setDestlimit(DateUtils.longToNoSecondString(hbzFslOrderDTO.getDestlimit()));
        }
//        fslOrderExcelVO.setOriginAddress(getFullNameForCountArea(hbzFslOrderDTO.getOriginArea(), hbzFslOrderDTO.getOriginAddress()));
//        fslOrderExcelVO.setDestAddress(getFullNameForCountArea(hbzFslOrderDTO.getDestArea(), hbzFslOrderDTO.getDestAddress()));
        return fslOrderExcelVO;
    }

    /**
     * 零担DTO转换为ExcelVO
     *
     * @param hbzLtlOrderDTO
     * @return
     */
    private LtlOrderExcelVO convertLtlDtoToExcelVo(HbzLtlOrderDTO hbzLtlOrderDTO) {
        LtlOrderExcelVO ltlOrderExcelVO = new LtlOrderExcelVO();
        BeanUtils.copyProperties(hbzLtlOrderDTO, ltlOrderExcelVO);
        convertBaseOrderDtoToExcelVo(hbzLtlOrderDTO, ltlOrderExcelVO);

        if (hbzLtlOrderDTO.getCommodityType() != null) {
            ltlOrderExcelVO.setCommodityType(hbzLtlOrderDTO.getCommodityType().getName());
        }
        if (hbzLtlOrderDTO.getWeightUnit() != null) {
            ltlOrderExcelVO.setWeightUnit(hbzLtlOrderDTO.getWeightUnit().getName());
        }
        if (hbzLtlOrderDTO.getVolumeUnit() != null) {
            ltlOrderExcelVO.setVolumeUnit(hbzLtlOrderDTO.getVolumeUnit().getName());
        }
        if (hbzLtlOrderDTO.getTransType() != null) {
            ltlOrderExcelVO.setTransType(hbzLtlOrderDTO.getTransType().getName());
        }
        if (hbzLtlOrderDTO.getOrderTakeStart() != null) {
            ltlOrderExcelVO.setOrderTakeStart(DateUtils.longToNoSecondString(hbzLtlOrderDTO.getOrderTakeStart()));
        }
        if (hbzLtlOrderDTO.getDestlimit() != null) {
            ltlOrderExcelVO.setDestlimit(DateUtils.longToNoSecondString(hbzLtlOrderDTO.getDestlimit()));
        }

//        ltlOrderExcelVO.setOriginAddress(getFullNameForCountArea(hbzLtlOrderDTO.getOriginArea(), hbzLtlOrderDTO.getOriginAddress()));
//        ltlOrderExcelVO.setDestAddress(getFullNameForCountArea(hbzLtlOrderDTO.getDestArea(), hbzLtlOrderDTO.getDestAddress()));
        return ltlOrderExcelVO;
    }

    /**
     * 帮我买DTO转换为ExcelVO
     *
     * @param hbzBuyOrderDTO
     * @return
     */
    private BuyOrderExcelVO convertBuyDtoToExcelVo(HbzBuyOrderDTO hbzBuyOrderDTO) {
        BuyOrderExcelVO buyOrderExcelVO = new BuyOrderExcelVO();
        BeanUtils.copyProperties(hbzBuyOrderDTO, buyOrderExcelVO);
        convertBaseOrderDtoToExcelVo(hbzBuyOrderDTO, buyOrderExcelVO);

        if (hbzBuyOrderDTO.getTimeLimit() != null) {
            buyOrderExcelVO.setTimeLimit(hbzBuyOrderDTO.getTimeLimit().getName());
        }
        if (hbzBuyOrderDTO.getStartTime() != null) {
            buyOrderExcelVO.setStartTime(DateUtils.longToNoSecondString(hbzBuyOrderDTO.getStartTime()));
        }
        if (hbzBuyOrderDTO.getEndTime() != null) {
            buyOrderExcelVO.setEndTime(DateUtils.longToNoSecondString(hbzBuyOrderDTO.getEndTime()));
        }
        return buyOrderExcelVO;
    }

    /**
     * 帮我送DTO转换为ExcelVO
     *
     * @param hbzSendOrderDTO
     * @return
     */
    private SendOrderExcelVO convertSendDtoToExcelVo(HbzSendOrderDTO hbzSendOrderDTO) {
        SendOrderExcelVO sendOrderExcelVO = new SendOrderExcelVO();
        BeanUtils.copyProperties(hbzSendOrderDTO, sendOrderExcelVO);
        convertBaseOrderDtoToExcelVo(hbzSendOrderDTO, sendOrderExcelVO);

        if (hbzSendOrderDTO.getTimeLimit() != null) {
            sendOrderExcelVO.setTimeLimit(hbzSendOrderDTO.getTimeLimit().getName());
        }
        if (hbzSendOrderDTO.getStartTime() != null) {
            sendOrderExcelVO.setStartTime(DateUtils.longToNoSecondString(hbzSendOrderDTO.getStartTime()));
        }
        if (hbzSendOrderDTO.getEndTime() != null) {
            sendOrderExcelVO.setEndTime(DateUtils.longToNoSecondString(hbzSendOrderDTO.getEndTime()));
        }
        if (hbzSendOrderDTO.getOrderTakeTime() != null) {
            sendOrderExcelVO.setOrderTakeTime(DateUtils.longToNoSecondString(hbzSendOrderDTO.getOrderTakeTime()));
        }
        if (hbzSendOrderDTO.getTakeLimit() != null) {
            String takeLimit;
            switch (hbzSendOrderDTO.getTakeLimit()) {
                case 1: {
                    takeLimit = "不限";
                    break;
                }
                case 2: {
                    takeLimit = "在指定时间之前";
                    break;
                }
                case 3: {
                    takeLimit = "在指定时间之后";
                    break;
                }
                default: {
                    takeLimit = "其他";
                    break;
                }
            }
            sendOrderExcelVO.setTakeLimit(takeLimit);
        }
        return sendOrderExcelVO;
    }

    /**
     * 快递DTO转换为ExcelVO
     *
     * @param hbzExOrderDTO
     * @return
     */
    private ExOrderExcelVO convertExDtoToExcelVo(HbzExOrderDTO hbzExOrderDTO) {
        ExOrderExcelVO exOrderExcelVO = new ExOrderExcelVO();
        BeanUtils.copyProperties(hbzExOrderDTO, exOrderExcelVO);
        convertBaseOrderDtoToExcelVo(hbzExOrderDTO, exOrderExcelVO);

        if (hbzExOrderDTO.getOrderTakeTime() != null) {
            exOrderExcelVO.setOrderTakeTime(DateUtils.longToNoSecondString(hbzExOrderDTO.getOrderTakeTime()));
        }
        if (hbzExOrderDTO.getCommodityClass() != null) {
            exOrderExcelVO.setCommodityClass(hbzExOrderDTO.getCommodityClass().getName());
        }
        return exOrderExcelVO;
    }

    /**
     * 基础订单转为ExcelVO
     *
     * @param hbzOrderDTO
     * @param baseOrderExcelVO
     * @return
     */
    private BaseOrderExcelVO convertBaseOrderDtoToExcelVo(HbzOrderDTO hbzOrderDTO, BaseOrderExcelVO baseOrderExcelVO) {
        if (hbzOrderDTO.getSettlementType() != null) {
            baseOrderExcelVO.setSettlementType(hbzOrderDTO.getSettlementType().getName());
        }
        if (hbzOrderDTO.getOfflineProcess() != null) {
            String offlineProcess;
            switch (hbzOrderDTO.getOfflineProcess()) {
                case 0: {
                    offlineProcess = "否";
                    break;
                }
                case 1: {
                    offlineProcess = "是";
                    break;
                }
                default: {
                    offlineProcess = "否";
                    break;
                }
            }
            baseOrderExcelVO.setOfflineProcess(offlineProcess);
        }
        if (hbzOrderDTO.getOrderTrans() != null) {
            baseOrderExcelVO.setOrderTrans(hbzOrderDTO.getOrderTrans().getName());
        }
        if (hbzOrderDTO.getCreateTime() != null) {
            baseOrderExcelVO.setCreateTime(DateUtils.longToNoSecondString(hbzOrderDTO.getCreateTime()));
        }
        if (hbzOrderDTO.getCreateUser() != null) {
            baseOrderExcelVO.setCreateUser(hbzOrderDTO.getCreateUser().getTelephone());
        }
        if (hbzOrderDTO.getTakeTime() != null) {
            baseOrderExcelVO.setTakeTime(DateUtils.longToNoSecondString(hbzOrderDTO.getTakeTime()));
        }
        if (hbzOrderDTO.getDealTime() != null) {
            baseOrderExcelVO.setDealTime(DateUtils.longToNoSecondString(hbzOrderDTO.getDealTime()));
        }
        if (hbzOrderDTO.getDealUser() != null) {
            baseOrderExcelVO.setDealUser(hbzOrderDTO.getDealUser().getTelephone());
        }
        if (hbzOrderDTO.getTakeUser() != null) {
            baseOrderExcelVO.setTakeUser(hbzOrderDTO.getTakeUser().getTelephone());
        }
        if (hbzOrderDTO.getAgent() != null) {
            baseOrderExcelVO.setAgent(hbzOrderDTO.getAgent().getTelephone());
        }
        if (hbzOrderDTO.getAgentTime() != null) {
            baseOrderExcelVO.setAgentTime(DateUtils.longToNoSecondString(hbzOrderDTO.getAgentTime()));
        }
        /*if (hbzOrderDTO.getPayType() != null) {
            baseOrderExcelVO.setPayType(hbzOrderDTO.getPayType().getName());
        }*/
        //发货省市县
        if (hbzOrderDTO.getOriginArea() != null) {
            writeProvinceCityCounty(hbzOrderDTO.getOriginArea(), baseOrderExcelVO, AddressTypeEnum.ORIGIN);
        }
        //收货省市县
        if (hbzOrderDTO.getDestArea() != null) {
            writeProvinceCityCounty(hbzOrderDTO.getDestArea(), baseOrderExcelVO, AddressTypeEnum.DEST);
        }
        return baseOrderExcelVO;
    }

    /**
     * 分单元格写入省、市、县
     *
     * @param hbzAreaDTO
     * @param baseOrderExcelVO
     * @param addressTypeEnum
     */
    private void writeProvinceCityCounty(HbzAreaDTO hbzAreaDTO, BaseOrderExcelVO baseOrderExcelVO, AddressTypeEnum addressTypeEnum) {
        if (hbzAreaDTO == null || hbzAreaDTO.getLevel() == null || baseOrderExcelVO == null || addressTypeEnum == null) {
            return;
        }
        switch (addressTypeEnum) {
            case ORIGIN: {
                switch (hbzAreaDTO.getLevel()) {
                    case 3: {
                        baseOrderExcelVO.setOriginCounty(hbzAreaDTO.getAreaName());
                        baseOrderExcelVO.setOriginCity(hbzAreaDTO.getParent().getAreaName());
                        baseOrderExcelVO.setOriginProvince(hbzAreaDTO.getParent().getParent().getAreaName());
                        break;
                    }
                    case 2: {
                        baseOrderExcelVO.setOriginCounty(hbzAreaDTO.getAreaName());
                        baseOrderExcelVO.setOriginCity(hbzAreaDTO.getParent().getAreaName());
                        break;
                    }
                    default: {
                    }
                }
                break;
            }
            case DEST: {
                switch (hbzAreaDTO.getLevel()) {
                    case 3: {
                        baseOrderExcelVO.setDestCounty(hbzAreaDTO.getAreaName());
                        baseOrderExcelVO.setDestCity(hbzAreaDTO.getParent().getAreaName());
                        baseOrderExcelVO.setDestProvince(hbzAreaDTO.getParent().getParent().getAreaName());
                        break;
                    }
                    case 2: {
                        baseOrderExcelVO.setDestCounty(hbzAreaDTO.getAreaName());
                        baseOrderExcelVO.setDestCity(hbzAreaDTO.getParent().getAreaName());
                        break;
                    }
                    default: {
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 地址拼接方法
     *
     * @param hbzAreaDTO
     * @param addressDetail
     * @return
     */
    @Deprecated
    private String getFullNameForCountArea(HbzAreaDTO hbzAreaDTO, String addressDetail) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(contactAreaName(hbzAreaDTO));
        stringBuilder.append(" ");
        stringBuilder.append(addressDetail);
        return stringBuilder.toString();
    }

    /**
     * 根据区县获取行政区域全名
     *
     * @param county 区县级对象
     * @return
     */
    @Deprecated
    private String contactAreaName(HbzAreaDTO county) {
        if (county == null || county.getLevel() == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] nameArr = null;
        switch (county.getLevel()) {
            case 3: {
                nameArr = new String[3];
                nameArr[2] = county.getAreaName();
                nameArr[1] = county.getParent().getAreaName();
                nameArr[0] = county.getParent().getParent().getAreaName();
                break;
            }
            case 2: {
                nameArr = new String[2];
                nameArr[1] = county.getAreaName();
                nameArr[0] = county.getParent().getAreaName();
                break;
            }
            default: {
                break;
            }
        }

        int len = nameArr.length;
        for (int i = 0; i < len; i++) {
            if (i < len - 1) {
                stringBuilder.append(nameArr[i] + "-");
            } else {
                stringBuilder.append(nameArr[i]);
            }
        }
        return stringBuilder.toString();
    }

}

enum ExportRoleEnum {

    MY_CREATE("发运"),
    MY_TAKEN("收运");

    String name;

    ExportRoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}