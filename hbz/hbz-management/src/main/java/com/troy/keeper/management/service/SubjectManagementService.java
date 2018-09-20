package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.SubjectManagementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2018/1/21.
 */
public interface SubjectManagementService   {


    //分页
    public Page<SubjectManagementDTO> findByCondition(SubjectManagementDTO subjectManagementDTO, Pageable pageable);

    //新建科目管理
    public Boolean saveSubjectManagement(SubjectManagementDTO subjectManagementDTO);

   //编辑科目信息
    public Boolean updateSubject(SubjectManagementDTO subjectManagementDTO);

    //删除科目信息
    public  Boolean deleteSubject(SubjectManagementDTO subjectManagementDTO);

}
