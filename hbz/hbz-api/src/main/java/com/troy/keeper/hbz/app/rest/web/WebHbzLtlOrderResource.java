package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.paramter.ExcelReadParamter;
import com.troy.keeper.excelimport.service.ExcelImportService;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzLtlMapDTO;
import com.troy.keeper.hbz.app.rest.web.fsl_and_ltl_facade.LtlFacade;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzLtlOrderDTO;
import com.troy.keeper.hbz.helper.*;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzLtlOrderService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.type.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 专线零担
 */
@Slf4j
@RestController
@RequestMapping("/api/web/ltlOrder")
public class WebHbzLtlOrderResource {

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzLtlOrderService hbzLtlOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private LtlFacade ltlFacade;

    /**
     * 货主 创建专线零担货源
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/createLtlOrder")
    public ResponseDTO createLtlOrder(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.createLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 货主 发布专线零担货源
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/publishLtlOrder")
    public ResponseDTO publishLtlOrder(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.confirmLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 计算零担价格
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/computePrice")
    public ResponseDTO computePrice(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.computePrice(hbzLtlMapDTO);
    }

    /**
     * 货主 获取 专线零担货源 分页列表
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/getLtlOrderByPage")
    public ResponseDTO getMyLtlOrderByPage(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.queryLtlOrderPage(hbzLtlMapDTO);
    }

    /**
     * 货主或车主 获取专线零担货源 详情
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/getLtlOrderDetail")
    public ResponseDTO getLtlOrderDetail(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.getLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 货主 获取专线零担货源 详情 更新回填用
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/getLtlOrderDetailForUpdate")
    public ResponseDTO getLtlOrderDetailForUpdate(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.loadLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 获取已接专线零担 列表
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/getMyTakenLtlOrderByPage")
    public ResponseDTO getMyTakenLtlOrderByPage(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.queryTaskLtlOrderPage(hbzLtlMapDTO);
    }

    /**
     * 车主 确认装货
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/confirmPickupLtlOrder")
    public ResponseDTO confirmPickupLtlOrder(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.takeLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 确认签收
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/signLtlOrderArrival")
    public ResponseDTO signLtlOrderArrival(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.comppleteLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 货主 确认签收
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/conformArrival")
    public ResponseDTO conformArrival(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.receiveLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 确认收款
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/confirmGetMoney")
    public ResponseDTO confirmGetMoney(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.endOrder(hbzLtlMapDTO);
    }

    /**
     * 货主 抢单 同意零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/ownerAgree")
    public ResponseDTO ownerAgree(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.agreeLlsOrder(hbzLtlMapDTO);
    }

    /**
     * 货主 抢单 拒绝零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/ownerRefuse")
    public ResponseDTO ownerRefuse(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.refuseLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 月结 同意指派零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/driverAgree")
    public ResponseDTO driverAgree(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.agreeDrvLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 抢单 零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/take")
    public ResponseDTO take(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.caLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 月结 拒绝指派零担
     *
     * @param hbzLtlMapDTO
     * @return
     */
    @PostMapping("/driverRefuse")
    public ResponseDTO driverRefuse(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        return this.ltlFacade.refuseDrvLtlOrder(hbzLtlMapDTO);
    }

    /**
     * 车主 抢单 分页条件查询可接专线整车订单
     *
     * @return
     */
    @PostMapping("/getAvailableLtlOrderByPage")
    public ResponseDTO getAvailableFslOrderByBage(@RequestBody HbzLtlMapDTO hbzLtlMapDTO) {
        HbzLtlOrderDTO hbzLtlOrderDTO = new HbzLtlOrderDTO();
        hbzLtlOrderDTO.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
        hbzLtlOrderDTO.setStatus(Const.STATUS_ENABLED);
        BeanHelper.copyProperties(hbzLtlMapDTO, hbzLtlOrderDTO,
                new PropertyMapper<String, Long>("orderTakeStart", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartLT", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartLE", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartGT", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartGE", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse));
        Page<HbzLtlOrderDTO> list = hbzLtlOrderService.queryPage(hbzLtlOrderDTO, hbzLtlOrderDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapLtl));
    }

    /**
     * 下载Excel模板
     *
     * @param response
     */
    @GetMapping("/downloadHbzLtlOrderTemplate")
    public void downloadHbzLtlOrderTemplate(HttpServletResponse response) {
        String fileName = "hbzLtlOrderTemplate.xls";
        ExcelUtils.downloadStaticFileFromResourceFolder(response, fileName);
    }

    /**
     * 专线零担货源Excel导入
     *
     * @return
     */
    @PostMapping("/importHbzLtlOrder")
    public ResponseDTO importHbzLtlOrder(@RequestParam("file") MultipartFile file) {
        int originCount;
        int finalCount;
        int startRowIndex = 8;

        if (file == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入文件！", null);
        }

        String fileName = file.getOriginalFilename();
        if (!fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase().equals("xls")) {
            return new ResponseDTO(Const.STATUS_ERROR, "只能通过文件.xls导入！", null);
        }

        ResponseDTO<Object> msg = new ResponseDTO();
        ResponseDTO<Object> responseDTO = new ResponseDTO();
        InputStream inputStream = null;
        try {
            //读取前台传来文件
//            File file1 = new File("D://hbz_jar/hbzLtlOrderTemplate.xls");
            inputStream = file.getInputStream();

            //配置ExcelRead的参数
            ExcelReadParamter excelReadParamter = new ExcelReadParamter();
            excelReadParamter.setExcelFile(inputStream);
            excelReadParamter.setSheetNum(0);  //sheet号
            excelReadParamter.setDataBaseNameRow(0);  //实体全类名
            excelReadParamter.setDataColumnNameRow(1);  //实体属性对应数据库字段名
            excelReadParamter.setDataTypeRow(2);  //属性字段数据类型
            excelReadParamter.setDataRequiredRow(3);  //是否必填
            excelReadParamter.setDataStartRow(startRowIndex);  //业务数据起始行
//            excelReadParamter.setDateFormat(new SimpleDateFormat(DateUtils.yyyy_MM_dd_HH_mm));
            excelReadParamter.setHumpMode(0);

            responseDTO = this.excelImportService.getExcelDataBy2003(excelReadParamter);

            if (responseDTO.getCode().equals("200")) {
                List<Map<String, String>> rowList = ((ExcelConfigParamter) responseDTO.getData()).getExcelList();
                originCount = rowList.size();
                finalCount = rowList.size();

                AtomicInteger rowNum = new AtomicInteger(startRowIndex + 1);
                rowList.stream().map(it -> {
                    HbzLtlOrderDTO hbzLtlOrderDTO = null;
                    try {
                        try {
                            hbzLtlOrderDTO = HbzLtlOrderDTO.class.newInstance();
                            String transTypeChineseName = it.get("transTypex");
                            String originAreaChineseName = it.get("originAreax");
                            String destAreaChineseName = it.get("destAreax");
                            String orderTakeStart = it.get("orderTakeStartx");
                            String linkTelephone = it.get("linkTelephone");
                            String destTelephone = it.get("destTelephone");

                            String weightUnitx = it.get("weightUnitx");
                            String volumeUnitx = it.get("volumeUnitx");

                            String destlimit = it.get("destlimitx");  //送达时间

//                            TransType transType = EnumUtils.getTransTypByChineseName(transTypeChineseName);

                            if (!Pattern.matches(Const.REGEX_MOBILE, linkTelephone)) {
                                throw new RuntimeException("第" + rowNum + "行，发货人手机号格式不正确!");
                            }

                            if (!Pattern.matches(Const.REGEX_MOBILE, destTelephone)) {
                                throw new RuntimeException("第" + rowNum + "行，收货人手机号格式不正确!");
                            }

                            TransType transType = null;
                            try {
                                transType = (TransType) EnumUtils.getEnumInstanceByClassNameAndChineseName(TransType.class, transTypeChineseName);
                            } catch (Exception e) {
                                throw new IllegalArgumentException("第" + rowNum + "行，" + e.getMessage());
                            }

                            HbzAreaDTO originAreaDTO = hbzAreaService.findCountyByAreaName(originAreaChineseName);
                            HbzAreaDTO destAreaDTO = hbzAreaService.findCountyByAreaName(destAreaChineseName);

                            if (originAreaDTO == null) {
                                throw new RuntimeException("第" + rowNum + "行，取货地址不正确!");
                            }
                            if (destAreaDTO == null) {
                                throw new RuntimeException("第" + rowNum + "行，目的地址不正确!");
                            }

                            //校验取货时间
                            Long longOrderTakeStart = null;
                            if (!Pattern.matches(Const.REGEX_YYYY_MM_DD_HH_MM, orderTakeStart)) {
                                throw new IllegalArgumentException("第" + rowNum + "行，取货时间不正确!");
                            } else {
                                try {
                                    longOrderTakeStart = DateUtils.stringToLong(orderTakeStart, DateUtils.yyyy_MM_dd_HH_mm);
                                } catch (Exception e) {
                                    throw new RuntimeException("第" + rowNum + "行，取货时间不正确!");
                                }
                            }

                            //校验送达时间
                            Long longDestlimit = null;
                            if (!Pattern.matches(Const.REGEX_YYYY_MM_DD_HH_MM, destlimit)) {
                                throw new IllegalArgumentException("第" + rowNum + "行，送达时间不正确!");
                            } else {
                                try {
                                    longDestlimit = DateUtils.stringToLong(destlimit, DateUtils.yyyy_MM_dd_HH_mm);
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("第" + rowNum + "行，送达时间不正确!");
                                }
                            }

                            WeightUnit weightUnit = null;
                            VolumeUnit volumeUnit = null;
                            try {
                                weightUnit = (WeightUnit) EnumUtils.getEnumInstanceByClassNameAndChineseName(WeightUnit.class, weightUnitx);
                                volumeUnit = (VolumeUnit) EnumUtils.getEnumInstanceByClassNameAndChineseName(VolumeUnit.class, volumeUnitx);
                            } catch (Exception e) {
                                throw new IllegalArgumentException("第" + rowNum + "行，" + e.getMessage());
                            }

                            BeanUtils.populate((Object) hbzLtlOrderDTO, it);

                            hbzLtlOrderDTO.setCreateUser(hbzUserService.currentUser());
                            hbzLtlOrderDTO.setStatus(Const.STATUS_ENABLED);
                            hbzLtlOrderDTO.setOrderNo(hbzOrderService.createNewOrderNo(OrderType.LTL));
                            hbzLtlOrderDTO.setOrderTrans(OrderTrans.NEW);
                            hbzLtlOrderDTO.setOrderType(OrderType.LTL);
                            hbzLtlOrderDTO.setTransType(transType);
                            hbzLtlOrderDTO.setOriginArea(originAreaDTO);
                            hbzLtlOrderDTO.setDestArea(destAreaDTO);
                            hbzLtlOrderDTO.setOrderTakeStart(longOrderTakeStart);
                            hbzLtlOrderDTO.setDestlimit(longDestlimit);

                            hbzLtlOrderDTO.setWeightUnit(weightUnit);
                            hbzLtlOrderDTO.setVolumeUnit(volumeUnit);

                            this.hbzLtlOrderService.insertOne(hbzLtlOrderDTO);
                            rowNum.getAndIncrement();
                        } catch (InvocationTargetException e) {
                            log.error(e.getMessage(), e);
                        } catch (IllegalAccessException e) {
                            throw e;
                        }
                    } catch (InstantiationException e) {
                        log.error(e.getMessage(), e);
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                    return hbzLtlOrderDTO;
                }).count();
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, responseDTO.getMsg(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseDTO(Const.STATUS_ERROR, e.getMessage(), null);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return new ResponseDTO(Const.STATUS_OK, "导入成功!", "输入：" + originCount + "条，" + "成功导入：" + finalCount + "条！");
    }

}