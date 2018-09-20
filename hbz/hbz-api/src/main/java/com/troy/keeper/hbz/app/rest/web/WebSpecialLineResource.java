package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.export.ExcelHandleUtil;
import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.paramter.ExcelReadParamter;
import com.troy.keeper.excelimport.service.ExcelImportService;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzDriverLineMapDto;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzDriverLineDTO;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.EnumUtils;
import com.troy.keeper.hbz.helper.ExcelUtils;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzDriverLineService;
import com.troy.keeper.hbz.service.HbzTransSizeService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.type.TransType;
import com.troy.keeper.hbz.vo.HbzDriverLineVO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：专线运力
 * @DateTime：2017/12/8 15:13
 */
@Slf4j
@RestController
@RequestMapping("/api/web/driverLine")
public class WebSpecialLineResource {

    @Autowired
    HbzDriverLineService hbzDriverLineService;

    @Autowired
    HbzAreaService hbzAreaService;

    @Autowired
    HbzTransSizeService hbzTransSizeService;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    private ExcelImportService excelImportService;

    /**
     * 车主 创建运力专线
     *
     * @param mapDto
     * @return
     */
    @PostMapping("/createDriverLine")
    public ResponseDTO createDriverLine(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzDriverLineDTO driverLine = new HbzDriverLineDTO();
        new Bean2Bean().copyProperties(mapDto, driverLine);
        HbzAreaDTO origin = hbzAreaService.findByOutCode(mapDto.getOriginAreaCode());
        HbzAreaDTO dest = hbzAreaService.findByOutCode(mapDto.getDestAreaCode());
        driverLine.setStatus(Const.STATUS_ENABLED);
        driverLine.setOriginArea(origin);
        driverLine.setDestArea(dest);
        driverLine.setUser(user);
        driverLine.setUserId(user.getId());
        driverLine = hbzDriverLineService.save(driverLine);
        if (driverLine != null) {
            if (mapDto.getTransSizes() == null) {
                mapDto.setTransSizes(new ArrayList<>());
            }
            List<HbzTransSizeDTO> transSizes = mapDto.getTransSizes().stream().map(hbzTransSizeService::findByTransSize).collect(Collectors.toList());
            hbzDriverLineService.bindTransSizes(driverLine, transSizes);
            return new ResponseDTO(Const.STATUS_OK, "保存成功！");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败！", null);
    }

    /**
     * 车主 更新我的运力专线
     *
     * @param mapDto
     * @return
     */
    @PostMapping("/updateDriverLine")
    public ResponseDTO updateDriverLine(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (mapDto.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！");
        }
        HbzDriverLineDTO driverLine = new HbzDriverLineDTO();
        driverLine.setId(mapDto.getId());
        driverLine = hbzDriverLineService.get(driverLine);
        if (driverLine == null || driverLine.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "id无效！");
        }
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(mapDto, driverLine);
        HbzAreaDTO origin = hbzAreaService.findByOutCode(mapDto.getOriginAreaCode());
        HbzAreaDTO dest = hbzAreaService.findByOutCode(mapDto.getDestAreaCode());
        driverLine.setStatus(Const.STATUS_ENABLED);
        driverLine.setOriginArea(origin);
        driverLine.setDestArea(dest);
        driverLine.setUser(user);
        driverLine.setUserId(user.getId());
        driverLine = hbzDriverLineService.save(driverLine);
        if (driverLine != null) {
            if (mapDto.getTransSizes() == null) {
                mapDto.setTransSizes(new ArrayList<>());
            }
            List<HbzTransSizeDTO> transSizes = mapDto.getTransSizes().stream().map(hbzTransSizeService::findByTransSize).collect(Collectors.toList());
            hbzDriverLineService.bindTransSizes(driverLine, transSizes);
            return new ResponseDTO(Const.STATUS_OK, "保存成功！");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败！", null);
    }

    /**
     * 车主 删除我的运力专线
     *
     * @param mapDto
     * @return
     */
    @PostMapping("/deleteDriverLine")
    public ResponseDTO deleteDriverLine(@RequestBody HbzDriverLineMapDto mapDto) {
        if (mapDto.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！");
        }
        HbzDriverLineDTO driverLine = new HbzDriverLineDTO();
        driverLine.setId(mapDto.getId());
        hbzDriverLineService.delete(driverLine);
        return new ResponseDTO(Const.STATUS_OK, "修改成功");
    }

    /**
     * 车主 分页条件查询我发布的运力专线
     *
     * @param mapDto
     * @return
     */
    @PostMapping("/queryMyDriverLineByPage")
    public ResponseDTO queryMyDriverLineByPage(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzDriverLineDTO hbzDriverLineDTO = new HbzDriverLineDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(mapDto, hbzDriverLineDTO);
        hbzDriverLineDTO.setUser(user);
        hbzDriverLineDTO.setUserId(user.getId());
        hbzDriverLineDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzDriverLineDTO> rs = hbzDriverLineService.queryPage(hbzDriverLineDTO, hbzDriverLineDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功!", rs.map(MapSpec::mapDriverLineDTO));
    }

    /**
     * 货主 分页条件查询可用运力专线
     *
     * @param mapDto
     * @return
     */
    @PostMapping("/queryAvailableDriverLineByPage")
    public ResponseDTO queryAvailableDriverLineByPage(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzDriverLineDTO hbzDriverLineDTO = new HbzDriverLineDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(mapDto, hbzDriverLineDTO);
        hbzDriverLineDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzDriverLineDTO> rs = hbzDriverLineService.queryPage(hbzDriverLineDTO, hbzDriverLineDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功!", rs.map(MapSpec::mapDriverLineDTO));
    }

    /**
     * 获取运力专线详情
     *
     * @param hbzDriverLineMapDto
     * @return
     */
    @PostMapping("/getDriverLineDetail")
    public ResponseDTO getDriverLineDetail(@RequestBody HbzDriverLineMapDto hbzDriverLineMapDto) {
        Long id = hbzDriverLineMapDto.getId();
        if (id == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }
        HbzDriverLineDTO hbzDriverLineDTO = new HbzDriverLineDTO();
        hbzDriverLineDTO.setId(id);

        HbzDriverLineDTO hbzDriverLineDTOFromDb = hbzDriverLineService.queryDriverLineDetail(hbzDriverLineDTO);
        if (hbzDriverLineDTOFromDb != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功!", hbzDriverLineDTOFromDb);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "查询失败！");
        }
    }

    /**
     * 车主 下载专线Excel模板
     *
     * @param response
     */
    @GetMapping("/downloadDriverLineTemplate")
    public void downloadDriverLineTemplate(HttpServletResponse response) {
        String fileName = "hbzDriverLineTemplate.xls";
        ExcelUtils.downloadStaticFileFromResourceFolder(response, fileName);
    }

    /**
     * 车主 专线批量Excel导入
     *
     * @return
     */
    @PostMapping("/importDriverLine")
    public ResponseDTO importDriverLine(@RequestParam("file") MultipartFile file) {
        int originCount;
        int finalCount;
        int startRowIndex = 7;

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
                    HbzDriverLineDTO hbzDriverLineDTO = null;
                    try {
                        try {
                            hbzDriverLineDTO = HbzDriverLineDTO.class.newInstance();
                            String transTypeChineseName = it.get("transTypex");
                            String originAreaChineseName = it.get("originAreax");
                            String destAreaChineseName = it.get("destAreax");
                            String transSizex = it.get("transSizex");

                            //处理车辆类型
                            TransType transType = null;
                            try {
                                transType = (TransType) EnumUtils.getEnumInstanceByClassNameAndChineseName(TransType.class, transTypeChineseName);
                            } catch (Exception e) {
                                throw new IllegalArgumentException("第" + rowNum + "行，" + e.getMessage());
                            }

                            //处理发货地区和目的地区
                            HbzAreaDTO originAreaDTO = hbzAreaService.findCountyByAreaName(originAreaChineseName);
                            HbzAreaDTO destAreaDTO = hbzAreaService.findCountyByAreaName(destAreaChineseName);
                            if (originAreaDTO == null) {
                                throw new IllegalArgumentException("第" + rowNum + "行，始发地不正确!");
                            }
                            if (destAreaDTO == null) {
                                throw new IllegalArgumentException("第" + rowNum + "行，目的地不正确!");
                            }

                            //处理车辆长度
                            List<Double> transSizeList = Arrays.asList(transSizex.split("-")).stream().map(num -> {
                                Double temp = null;
                                try {
                                    temp = Double.parseDouble(num);
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("第" + rowNum + "行，车辆长度不正确!");
                                }
                                return temp;
                            }).collect(Collectors.toList());

                            BeanUtils.populate((Object) hbzDriverLineDTO, it);
                            hbzDriverLineDTO.setUser(hbzUserService.currentUser());
                            hbzDriverLineDTO.setStatus(Const.STATUS_ENABLED);
                            hbzDriverLineDTO.setTransType(transType);
                            hbzDriverLineDTO.setOriginAreaId(originAreaDTO.getId());
                            hbzDriverLineDTO.setDestAreaId(destAreaDTO.getId());
                            hbzDriverLineDTO.setTransSizes(transSizeList);

                            HbzDriverLineDTO hbzDriverLineDTOFromDB = this.hbzDriverLineService.importOne(hbzDriverLineDTO);
                            if (hbzDriverLineDTOFromDB != null) {
                                rowNum.getAndIncrement();
                            }
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
                    return hbzDriverLineDTO;
                }).count();
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, responseDTO.getMsg(), "仅仅通过了基础验证");
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
     * 批量导出运力专线
     *
     * @param hbzDriverLineMapDto
     * @param response
     */
    @PostMapping("/exportDriverLine")
    public void exportDriverLine(HbzDriverLineMapDto hbzDriverLineMapDto, HttpServletResponse response) {
        HbzUserDTO currentUser = hbzUserService.currentUser();
        HbzDriverLineDTO hbzDriverLineDTO = new HbzDriverLineDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzDriverLineMapDto, hbzDriverLineDTO);
        hbzDriverLineDTO.setUser(currentUser);
        hbzDriverLineDTO.setUserId(currentUser.getId());
        hbzDriverLineDTO.setStatus(Const.STATUS_ENABLED);

        List<HbzDriverLineDTO> resultList = hbzDriverLineService.query(hbzDriverLineDTO);

       /* List<HbzDriverLineVO> hbzDriverLineVOList = resultList.stream().map(it -> {
//            StringBuilder
            it.getDestArea(
                    it.getDestArea();
            ).getParent().getParent().getAreaName();
        });*/
    }

    /**
     * 递归获取行政区域名
     *
     * @param hbzAreaDTO
     * @return
     */
    public static HbzAreaDTO getChineseNameFromArea(HbzAreaDTO hbzAreaDTO) {
        if (hbzAreaDTO.getLevel() > 1) {
            return getChineseNameFromArea(hbzAreaDTO.getParent());
        } else {
            return hbzAreaDTO;
        }
    }

    /**
     * 根据区县获取行政区域全名
     *
     * @param county 区县级对象
     * @return
     */
    /*public static String getFullChineseNameFromArea(HbzAreaDTO county) {
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
            }
        }

        int leng = nameArr.length;
        for (int i = 0; i < leng; i++) {
            if (i < leng - 1) {
                stringBuilder.append(nameArr[i] + "-");
            } else {
                stringBuilder.append(nameArr[i]);
            }
        }
        return stringBuilder.toString();
    }*/

    public static void main(String[] args) {
        /*List<String> stringList = new ArrayList<>();
        stringList.add("12");
        stringList.add("23");

        String[] stringArr = new String[stringList.size()];
        stringList.toArray(stringArr);
        for (String it : stringArr) {
            System.out.println(it.concat("+++++"));
        }*/

        /*Function<String, String> k = it -> it.concat("333");
        System.out.println(k.apply("00000"));*/

        /*JSONArray jsonArray = StaticFileUtils.getFiledAndTittleFromClazz(HbzDriverLineVO.class);
        System.out.println(jsonArray.toString().toString());*/

        //中国
        HbzAreaDTO zhongGuo = new HbzAreaDTO();
        zhongGuo.setId(0L);
        zhongGuo.setLevel(0);
        zhongGuo.setParent(null);
        zhongGuo.setAreaName("中国");

        //四川
        HbzAreaDTO siChuan = new HbzAreaDTO();
        siChuan.setId(1L);
        siChuan.setLevel(1);
        siChuan.setParent(zhongGuo);
        siChuan.setAreaName("四川");

        //成都
        HbzAreaDTO chengDu = new HbzAreaDTO();
        chengDu.setId(2L);
        chengDu.setLevel(2);
        chengDu.setParent(siChuan);
        chengDu.setAreaName("成都");

        //双流
        HbzAreaDTO shuangLiu = new HbzAreaDTO();
        shuangLiu.setId(3L);
        shuangLiu.setLevel(3);
        shuangLiu.setParent(chengDu);
        shuangLiu.setAreaName("双流");

        //北京
        HbzAreaDTO beiJing = new HbzAreaDTO();
        beiJing.setId(4L);
        beiJing.setLevel(1);
        beiJing.setParent(zhongGuo);
        beiJing.setAreaName("北京");

        //海淀
        HbzAreaDTO haiDian = new HbzAreaDTO();
        haiDian.setId(5L);
        haiDian.setLevel(2);
        haiDian.setParent(beiJing);
        haiDian.setAreaName("海淀");

//        System.err.println(getFullChineseNameFromArea(shuangLiu) + "==============");

    }

    @PostMapping("/export/exportExcelTest")
    public void exportExcelTest(HttpServletResponse response) {
        List<HbzDriverLineVO> resultList = new ArrayList<>();
        resultList.add(new HbzDriverLineVO("成都市高新区", "广州市白云区", "重卡", "34吨", "12米", "123万"));
        ExcelHandleUtil.exportExcel(ExcelUtils.getFiledAndTittleFromClazz(HbzDriverLineVO.class), resultList, response, "123.xls");
    }

}
