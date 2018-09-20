package com.troy.keeper.hbz.service;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.paramter.ExcelReadParamter;
import com.troy.keeper.excelimport.service.ExcelDataHandle;
import com.troy.keeper.excelimport.service.ExcelImportService;

public interface ExcelOrderImportService { //extends ExcelImportService {

    ResponseDTO<Object> import2003ExcelWithEnumProperty(ExcelReadParamter excelReadParamter, ExcelDataHandle excelDataHandle) throws Exception;

//    void mapToDtoThenToObject(ExcelConfigParamter excelConfigParamter);

}
