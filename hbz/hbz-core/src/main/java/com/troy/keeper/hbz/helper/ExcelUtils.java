package com.troy.keeper.hbz.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.troy.keeper.hbz.annotation.ExcelCell;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.vo.excel.ExOrderExcelVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：静态文件工具类，主要处理静态文件的下载等业务
 * @DateTime：2017/12/14 11:38
 */
@Slf4j
public final class ExcelUtils {

    /**
     * 从Resource目录下载静态文件，（如：Excel模板）
     *
     * @param response
     * @param fileName
     */
    public static final void downloadStaticFileFromResourceFolder(HttpServletResponse response, String fileName) {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        InputStream inputStream = ExcelUtils.class.getClassLoader().getResourceAsStream("/" + fileName);
        OutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                    log.info(fileName);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 通过反射将要导出的字段及表头封装为接口要求的ArrayList，供Excel导出接口用
     *
     * @param clazz
     * @return
     */
    public static final JSONArray getFiledAndTittleFromClazz(Class<?> clazz, String... ignoreArr) {
        List<String> ignoreList = ignoreArr == null ? null : Arrays.asList(ignoreArr);

        Field[] filedArr = clazz.getFields();
        Field[] filedArr0 = clazz.getDeclaredFields();

        List<Field> fieldList = Arrays.asList(filedArr).stream().collect(Collectors.toList());
        List<Field> fieldList0 = Arrays.asList(filedArr0).stream().collect(Collectors.toList());

        JSONArray jsonArray = new JSONArray();
        fieldList0.addAll(fieldList);
        fieldList0.stream()
                .distinct()
                .filter(it -> it.isAnnotationPresent(ExcelCell.class) && (ignoreList == null || !ignoreList.contains(it.getName())))
                .sorted(Comparator.comparing(it -> it.getAnnotation(ExcelCell.class).sortNo()))
                .map(it -> {
                    System.out.println(it.getName());
                    JSONObject jsonObject = new JSONObject();
                    ExcelCell excelCell = it.getAnnotation(ExcelCell.class);
                    if ("".equals(excelCell.name())) {
                        jsonObject.put("key", it.getName());
                        jsonObject.put("value", excelCell.title());
                    } else {
                        jsonObject.put("key", excelCell.name());
                        jsonObject.put("value", excelCell.title());
                    }
                    return jsonObject;
                }).forEach(jsonArray::add);
        return jsonArray;
    }

    public static void main(String[] args) {
//        Field[] kk = ExOrderExcelVO.class.getDeclaredFields();
//        for (Field it : kk) {
//            System.out.println(it.getName());
//        }
//
//        Field[] jj = ExOrderExcelVO.class.getFields();
//        for (Field it : kk) {
//            System.out.println(it.getName() + "=============");
//        }
//        Arrays.asList(jj).stream().sorted(Comparator.comparing(it -> it.getAnnotation(ExcelCell.class).sortNo())).forEach(it -> System.out.println(it.getName()));

        /*int kk = 1;
        switch (kk) {
            case 1:
            case 2: {
                System.out.println("2");
                break;
            }
        }*/
        System.out.println(OrderType.valueOf("FSL").getName());



    }

}