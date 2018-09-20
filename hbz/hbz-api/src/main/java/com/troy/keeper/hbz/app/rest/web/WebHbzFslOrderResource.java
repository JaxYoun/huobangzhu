package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.paramter.ExcelReadParamter;
import com.troy.keeper.excelimport.service.ExcelImportService;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzFslMapDTO;
import com.troy.keeper.hbz.app.rest.web.fsl_and_ltl_facade.FslFacade;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzFslOrderDTO;
import com.troy.keeper.hbz.helper.*;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzFslOrderService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 接收、处理 web端 专线整车 货源请求
 */
@Slf4j
@RestController
@RequestMapping("/api/web/flsOrder")
public class WebHbzFslOrderResource extends BaseResource {

    @Autowired
    private HbzAreaService hbzAreaService;

    @Autowired
    private HbzFslOrderService hbzFslOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private FslFacade fslFacade;

    /**
     * 货主 创建专线整车货源
     *
     * @param fslOrderCreateDTO
     * @return
     */
    @Label("Web - 货主 - 建整车专线单")
    @PostMapping("/createFlsOrder")
    public ResponseDTO createFlsOrder(@RequestBody HbzFslMapDTO fslOrderCreateDTO) {
        return this.fslFacade.createFslOrder(fslOrderCreateDTO);
    }

    /**
     * 货货源
     *
     * @param fslMapDTO
     * @ret主 发布专线整车urn
     */
    @Label("Web - 货主 - 发布整车专线")
    @PostMapping("/publishFlsOrder")
    public ResponseDTO publishFlsOrder(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.confirmFslOrder(fslMapDTO);
    }

    /**
     * 价格计算
     *
     * @param mapDTO
     * @return
     */
    @Label("Web - 货主 - 价格计算")
    @RequestMapping("/computePrice")
    public ResponseDTO computePrice(@RequestBody HbzFslMapDTO mapDTO) {
        return this.fslFacade.computePrice(mapDTO);
    }

    /**
     * 货主 抢单 同意
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 货主 - 货主同意抢单")
    @RequestMapping(value = "/ownerAgree", method = RequestMethod.POST)
    public ResponseDTO ownerAgree(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.agreeFslOrder(fslMapDTO);
    }

    /**
     * 货主 抢单 拒绝
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 货主 - 货主拒绝")
    @RequestMapping(value = "/ownerRefuse", method = RequestMethod.POST)
    public ResponseDTO ownerRefuse(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.refuseFslOrder(fslMapDTO);
    }

    /**
     * 货主 获取 当前用户创建 专线整车货源分页列表
     *
     * @param query
     * @return
     */
    @Label("Web - 货主 - 得到我的订单创建")
    @PostMapping("/getMyFlsOrderByPage")
    public ResponseDTO getMyFlsOrderByPage(@RequestBody HbzFslMapDTO query) {
        return this.fslFacade.queryFslOrderPage(query);
    }

    /**
     * 车主 获取 当前用户已接 整车专线货源分页列表
     *
     * @param query
     * @return
     */
    @Label("Web - 车主 - 得到我接运的订单")
    @PostMapping("/getMyTakenFlsOrderByPage")
    public ResponseDTO getMyTakenFlsOrderByPage(@RequestBody HbzFslMapDTO query) {
        return this.fslFacade.queryTaskFslOrderPage(query);
    }

    /**
     * 车主 确认装货
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 车主 - 确认装货")
    @PostMapping("/confirmPickupFlsOrder")
    public ResponseDTO confirmPickupFlsOrder(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.takeFslOrder(fslMapDTO);
    }

    /**
     * 车主 月结 同意指派的订单
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 车主 - 同意接运")
    @RequestMapping(value = "/driverAgree", method = RequestMethod.POST)
    public ResponseDTO driverAgree(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.agreeDrvFslOrder(fslMapDTO);
    }

    /**
     * 车主 抢单 整车
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 车主 - 取货")
    @RequestMapping(value = "/take", method = RequestMethod.POST)
    public ResponseDTO take(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.carryFslOrder(fslMapDTO);
    }

    /**
     * 车主 月结 拒绝指派的订单
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 车主 - 拒绝接单")
    @RequestMapping(value = "/driverRefuse", method = RequestMethod.POST)
    public ResponseDTO driverRefuse(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.refuseDrvFslOrder(fslMapDTO);
    }

    /**
     * 车主 确认签收
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 车主 - 确认签收")
    @PostMapping("/signFlsOrderArrival")
    public ResponseDTO signFlsOrderArrival(HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.comppleteFslOrder(fslMapDTO);
    }

    /**
     * 货主 确认送达
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 货主 - 确认送达")
    @PostMapping("/confirmFlsOrderArrival")
    public ResponseDTO confirmFlsOrderArrival(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.receiveFslOrder(fslMapDTO);
    }

    /**
     * 车主 确认收款
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 车主 - 确认收货")
    @PostMapping("/confirmGetMoney")
    public ResponseDTO confirmGetMoney(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.endOrder(fslMapDTO);
    }

    /**
     * 货主或车主 获取整车专线详情
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 货主、车主 - 查看订单详情")
    @PostMapping("/getFlsOrderDetail")
    public ResponseDTO getFlsOrderDetail(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.getFslOrder(fslMapDTO);
    }

    /**
     * 货主 回填整车专线详情 修改用
     *
     * @param fslMapDTO
     * @return
     */
    @Label("Web - 货主、车主 - 查看订单详情")
    @RequestMapping(value = "/getFlsOrderDetailForUpdate", method = RequestMethod.POST)
    public ResponseDTO getFlsOrderDetailForUpdate(@RequestBody HbzFslMapDTO fslMapDTO) {
        return this.fslFacade.loadFslOrder(fslMapDTO);
    }

    /**
     * 车主 抢单 分页条件查询可接专线整车订单
     *
     * @return
     */
    @Label("Web - 货主 - 查询自己建的订单")
    @PostMapping("/getAvailableFslOrderByBage")
    public ResponseDTO getAvailableFslOrderByBage(@RequestBody HbzFslMapDTO fslMapDTO) {
        HbzFslOrderDTO hbzFslOrderDTO = new HbzFslOrderDTO();
        hbzFslOrderDTO.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
        hbzFslOrderDTO.setStatus(Const.STATUS_ENABLED);

        BeanHelper.copyProperties(fslMapDTO, hbzFslOrderDTO,
                new PropertyMapper<String, Long>("orderTakeStart", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartLT", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartLE", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartGT", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse),
                new PropertyMapper<String, Long>("orderTakeStartGE", new TimeMillisFormater(Const.YYYY_MM_DD_HH_MM)::parse));
        Page<HbzFslOrderDTO> list = hbzFslOrderService.queryPage(hbzFslOrderDTO, hbzFslOrderDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.map(MapSpec::mapFsl));
    }

    /**
     * 下载Excel模板
     *
     * @param response
     */
    @Label("Web - 货主 - 下载导入模板")
    @GetMapping("/downloadHbzFslOrderTemplate")
    public void downloadHbzFslOrderTemplate(HttpServletResponse response) {
        String fileName = "hbzFslOrderTemplate.xls";
        ExcelUtils.downloadStaticFileFromResourceFolder(response, fileName);
    }

    /**
     * 专线整车货源Excel导入
     *
     * @return
     */
    @Label("Web - 货主 - 导入订单")
    @PostMapping("/importHbzFslOrder")
    public ResponseDTO importHbzFslOrder(@RequestParam("file") MultipartFile file) {
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
//            InputStream excelFile = file.getInputStream();
//            File file1 = new File("D://hbz_jar/hbzFslOrderTemplate.xls");
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
            //excelReadParamter.setDateFormat(new SimpleDateFormat(DateUtils.yyyy_MM_dd_HH_mm));
            excelReadParamter.setHumpMode(0);
            responseDTO = this.excelImportService.getExcelDataBy2003(excelReadParamter);
            if (responseDTO.getCode().equals("200")) {
                List<Map<String, String>> rowList = ((ExcelConfigParamter) responseDTO.getData()).getExcelList();
                originCount = rowList.size();
                finalCount = rowList.size();

                AtomicInteger rowNum = new AtomicInteger(startRowIndex + 1);
                rowList.stream().map(it -> {
                    HbzFslOrderDTO hbzFslOrderDTO = null;
                    try {
                        try {
                            hbzFslOrderDTO = HbzFslOrderDTO.class.newInstance();
                            String transTypeChineseName = it.get("transTypex");
                            String originAreaChineseName = it.get("originAreax");
                            String destAreaChineseName = it.get("destAreax");
                            String orderTakeStart = it.get("orderTakeStartx");
                            String linkTelephone = it.get("linkTelephone");
                            String destTelephone = it.get("destTelephone");

                            String weightUnitx = it.get("weightUnitx");
                            String volumeUnitx = it.get("volumeUnitx");

                            String destlimit = it.get("destlimitx");  //送达时间

                            if (!Pattern.matches(Const.REGEX_MOBILE, linkTelephone)) {
                                throw new RuntimeException("第" + rowNum + "行，发货人手机号格式不正确!");
                            }
                            if (!Pattern.matches(Const.REGEX_MOBILE, destTelephone)) {
                                throw new RuntimeException("第" + rowNum + "行，收货人手机号格式不正确!");
                            }
//                            TransType transType = EnumUtils.getTransTypByChineseName(transTypeChineseName);
                            TransType transType = null;
                            try {
                                transType = (TransType) EnumUtils.getEnumInstanceByClassNameAndChineseName(TransType.class, transTypeChineseName);
                            } catch (Exception e) {
                                throw new IllegalArgumentException("第" + rowNum + "行，" + e.getMessage());
                            }
                            HbzAreaDTO originAreaDTO = hbzAreaService.findCountyByAreaName(originAreaChineseName);
                            HbzAreaDTO destAreaDTO = hbzAreaService.findCountyByAreaName(destAreaChineseName);

                            if (originAreaDTO == null) {
                                throw new IllegalArgumentException("第" + rowNum + "行，取货地址不正确!");
                            }
                            if (destAreaDTO == null) {
                                throw new IllegalArgumentException("第" + rowNum + "行，目的地址不正确!");
                            }

                            //校验取货时间
                            Long longOrderTakeStart = null;
                            if (!Pattern.matches(Const.REGEX_YYYY_MM_DD_HH_MM, orderTakeStart)) {
                                throw new IllegalArgumentException("第" + rowNum + "行，送达时间不正确!");
                            } else {
                                try {
                                    longOrderTakeStart = DateUtils.stringToLong(orderTakeStart, DateUtils.yyyy_MM_dd_HH_mm);
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("第" + rowNum + "行，取货时间不正确!");
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

                            BeanUtils.populate((Object) hbzFslOrderDTO, it);
                            hbzFslOrderDTO.setCreateUser(hbzUserService.currentUser());
                            hbzFslOrderDTO.setStatus(Const.STATUS_ENABLED);
                            hbzFslOrderDTO.setOrderNo(hbzOrderService.createNewOrderNo(OrderType.FSL));
                            hbzFslOrderDTO.setOrderTrans(OrderTrans.NEW);
                            hbzFslOrderDTO.setOrderType(OrderType.FSL);
                            hbzFslOrderDTO.setTransType(transType);
                            hbzFslOrderDTO.setOriginArea(originAreaDTO);
                            hbzFslOrderDTO.setDestArea(destAreaDTO);
                            hbzFslOrderDTO.setOrderTakeStart(longOrderTakeStart);
                            hbzFslOrderDTO.setDestlimit(longDestlimit);
                            hbzFslOrderDTO.setWeightUnit(weightUnit);
                            hbzFslOrderDTO.setVolumeUnit(volumeUnit);

                            this.hbzFslOrderService.insertOne(hbzFslOrderDTO);
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
                    return hbzFslOrderDTO;
                }).count();
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, responseDTO.getMsg(), "通过了基础验证");
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

    /**
     * 根据验证结果，将数据列表二分，同时记录第一条出错的行号
     *
     * @param rowList
     * @param rowNumber
     * @return
     */
    private Map<String, List<Map<String, String>>> binarizeRowList(List<Map<String, String>> rowList, int rowNumber) {
        Map<String, List<Map<String, String>>> binResultMap = new HashMap<>();
        List<Map<String, String>> correctList = new ArrayList<>();
        List<Map<String, String>> incorrectList = new ArrayList<>();
        List<Map<String, String>> firstErrorRowNumber = new ArrayList<>(1);

        int counter = 0;
        for (Map<String, String> ele : rowList) {
            counter++;
            if (checkRow(ele)) {
                correctList.add(ele);
            } else {
                incorrectList.add(ele);
                if (binResultMap.get("firstErrorRowNumber") != null) {
                    Map<String, String> kk = new HashMap<>();
                    kk.put("num", (rowNumber + counter) + "");
                    firstErrorRowNumber.add(kk);
                    binResultMap.put("firstErrorRowNumber", firstErrorRowNumber);
                }
            }
        }

        binResultMap.put("correctList", correctList);
        if (incorrectList.size() > 0) {
            binResultMap.put("incorrectList", incorrectList);
        }
        return binResultMap;
    }

    /**
     * 验证单条数据
     *
     * @param row
     * @return
     */
    public boolean checkRow(Map<String, String> row) {
        boolean bool = true;

        //预估重量
        String commodityWeight = row.get("commodityWeight");
        try {
            Double.parseDouble(commodityWeight);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            row.put("commodityWeight", commodityWeight + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }

        //预估体积
        String commodityVolume = row.get("commodityVolume");
        try {
            Double.parseDouble(commodityVolume);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            row.put("commodityVolume", commodityVolume + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }

        //接运时间
        String orderTakeStartx = row.get("orderTakeStartx");
        try {
            DateUtils.stringToLong(orderTakeStartx, DateUtils.yyyy_MM_dd_HH_mm);
        } catch (Exception e) {
            e.printStackTrace();
            row.put("orderTakeStartx", orderTakeStartx + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }

        //最低载重
        String maxLoad = row.get("maxLoad");
        try {
            Double.parseDouble(maxLoad);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            row.put("maxLoad", maxLoad + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }

        //单价
        String unitPrice = row.get("unitPrice");
        try {
            Double.parseDouble(unitPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            row.put("unitPrice", unitPrice + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }

        //总价
        String amount = row.get("amount");
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            row.put("amount", amount + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }

        //联系电话
        String linkTelephone = row.get("linkTelephone");
        if (!Pattern.matches(Const.REGEX_MOBILE, linkTelephone)) {
            row.put("linkTelephone", linkTelephone + "(格式不正确!)");
            if (bool) {
                bool = false;
            }
        }
        return bool;
    }

    /**
     * 专线整车货源Excel导入
     *
     * @return
     */
    @Label("Web - 货主 - 货源excel导入")
    @Deprecated
    @PostMapping("/neoImportHbzFslOrder")
    public ResponseDTO neoImportHbzFslOrder() {  // (@RequestParam("file") MultipartFile file) {
        int originCount;
        int finalCount;

        ResponseDTO<Object> msg = new ResponseDTO();
        ResponseDTO<Object> responseDTO = new ResponseDTO();
        try {
            //读取前台传来文件
            //InputStream excelFile = file.getInputStream();

            File file1 = new File("D://hbz_jar/hbzFslOrderTemplate.xls");
            String name = file1.getName();
            InputStream excelFile = new FileInputStream(file1);

            //配置ExcelRead的参数
            ExcelReadParamter excelReadParamter = new ExcelReadParamter();
            excelReadParamter.setExcelFile(excelFile);
            excelReadParamter.setSheetNum(0);  //sheet号
            excelReadParamter.setDataBaseNameRow(0);  //实体全类名
            excelReadParamter.setDataColumnNameRow(1);  //实体属性对应数据库字段名
            excelReadParamter.setDataTypeRow(2);  //属性字段数据类型
            excelReadParamter.setDataRequiredRow(3);  //是否必填
            excelReadParamter.setDataStartRow(7);  //真实数据起始行
            excelReadParamter.setDateFormat(new SimpleDateFormat(DateUtils.yyyy_MM_dd_HH_mm));
            excelReadParamter.setHumpMode(0);

            responseDTO = this.excelImportService.getExcelDataBy2003(excelReadParamter);
            if (responseDTO.getCode().equals("200")) {
                List<Map<String, String>> rowList = ((ExcelConfigParamter) responseDTO.getData()).getExcelList();
                originCount = rowList.size();
                finalCount = rowList.size();

                Map<String, List<Map<String, String>>> binaryListMap = binarizeRowList(rowList, 7 + 1);
                List<Map<String, String>> incorrectList = binaryListMap.get("incorrectList");
                List<Map<String, String>> correctList = binaryListMap.get("correctList");
                List<Map<String, String>> firstErrorRowNumber = binaryListMap.get("firstErrorRowNumber");

                if (incorrectList != null && incorrectList.size() > 0 && firstErrorRowNumber != null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "第" + firstErrorRowNumber.get(0).get("num") + "行数据填写错误！", incorrectList.get(0));
                }

                correctList.stream().map(it -> {
                    HbzFslOrderDTO hbzFslOrderDTO = null;
                    try {
                        try {
                            hbzFslOrderDTO = HbzFslOrderDTO.class.newInstance();
                            String transTypeChineseName = it.get("transTypex");
                            String originAreaChineseName = it.get("originAreax");
                            String destAreaChineseName = it.get("destAreax");
                            String orderTakeStart = it.get("orderTakeStartx");

                            TransType transType = EnumUtils.getTransTypByChineseName(transTypeChineseName);
                            HbzAreaDTO originAreaDTO = hbzAreaService.findCountyByAreaName(originAreaChineseName);
                            HbzAreaDTO destAreaDTO = hbzAreaService.findCountyByAreaName(destAreaChineseName);

                            if (originAreaDTO == null) {
                                throw new RuntimeException("取货地址不正确!");
                            }
                            if (destAreaDTO == null) {
                                throw new RuntimeException("目的地址不正确!");
                            }

                            Long longOrderTakeStart = null;
                            try {
                                longOrderTakeStart = DateUtils.stringToLong(orderTakeStart, DateUtils.yyyy_MM_dd_HH_mm);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                            BeanUtils.populate((Object) hbzFslOrderDTO, it);

                            hbzFslOrderDTO.setStatus(Const.STATUS_ENABLED);
                            hbzFslOrderDTO.setOrderNo(hbzOrderService.createNewOrderNo(OrderType.FSL));
                            hbzFslOrderDTO.setOrderTrans(OrderTrans.NEW);
                            hbzFslOrderDTO.setOrderType(OrderType.FSL);
                            hbzFslOrderDTO.setTransType(transType);
                            hbzFslOrderDTO.setOriginArea(originAreaDTO);
                            hbzFslOrderDTO.setDestArea(destAreaDTO);
                            hbzFslOrderDTO.setOrderTakeStart(longOrderTakeStart);

                            this.hbzFslOrderService.insertOne(hbzFslOrderDTO);
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
                    return hbzFslOrderDTO;
                }).count();
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, responseDTO.getMsg(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseDTO(Const.STATUS_ERROR, e.getMessage(), null);
        }
        return new ResponseDTO(Const.STATUS_OK, "导入成功!", "输入：" + originCount + "条，" + "成功导入：" + finalCount + "条，" + "重复：" + (originCount - finalCount) + "条！");
    }
}