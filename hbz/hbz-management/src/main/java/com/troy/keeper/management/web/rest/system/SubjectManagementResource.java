package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.SubjectManagementDTO;
import com.troy.keeper.management.service.SubjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2018/1/21.
 */
@RestController
public class SubjectManagementResource {

    @Autowired
    private SubjectManagementService subjectManagementService;

    //分页查询
    @RequestMapping("/api/manager/subjectManagementPage")
    public ResponseDTO subjectManagement(@RequestBody SubjectManagementDTO subjectManagementDTO, Pageable pageable){
        return new ResponseDTO("200", "科目管理分页查询",subjectManagementService.findByCondition(subjectManagementDTO,pageable));
    }

    //新增科目管理
    @RequestMapping("/api/manager/addSubjectManagement")
    public ResponseDTO addSubjectManagement(@RequestBody SubjectManagementDTO subjectManagementDTO){
        return new ResponseDTO("200", "新增科目管理",subjectManagementService.saveSubjectManagement(subjectManagementDTO));
    }

    //编辑科目信息
    @RequestMapping("/api/manager/updateSubjectManagement")
    public ResponseDTO updateSubjectManagement(@RequestBody SubjectManagementDTO subjectManagementDTO){
        Boolean str=subjectManagementService.updateSubject(subjectManagementDTO);
        if (str==true){

            return new ResponseDTO("200", "编辑科目信息",str);
        }else {
            return new ResponseDTO("401", "请选择需要编辑的数据",str);
        }
    }

    //删除科目信息
    @RequestMapping("/api/manager/deleteSubjectManagement")
    public ResponseDTO deleteSubjectManagement(@RequestBody SubjectManagementDTO subjectManagementDTO){
        Boolean str=subjectManagementService.deleteSubject(subjectManagementDTO);
        if (str==true){
            return new ResponseDTO("200", "删除科目信息",str);
        }else {
            return new ResponseDTO("401", "请选择需要删除的科目信息",str);
        }
    }


















}
