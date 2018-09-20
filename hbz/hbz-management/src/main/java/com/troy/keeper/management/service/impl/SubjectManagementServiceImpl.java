package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.po.SubjectManagement;
import com.troy.keeper.hbz.po.UserInformation;
import com.troy.keeper.management.dto.SubjectManagementDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.repository.SubjectManagementRepository;
import com.troy.keeper.management.service.SubjectManagementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/21.
 */
@Service
@Transactional
public class SubjectManagementServiceImpl implements SubjectManagementService {

    @Autowired
    private SubjectManagementRepository subjectManagementRepository;


    //分页
    @Override
    public Page<SubjectManagementDTO> findByCondition(SubjectManagementDTO subjectManagementDTO, Pageable pageable) {


        Page<SubjectManagement> page=subjectManagementRepository.findAll(new Specification<SubjectManagement>() {
            @Override
            public Predicate toPredicate(Root<SubjectManagement> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                //科目编码
                if (StringUtils.isNotBlank(subjectManagementDTO.getSubjectCode())){
                    predicateList.add(criteriaBuilder.equal(root.get("subjectCode"),subjectManagementDTO.getSubjectCode()));
                }
                //科目名称
                if (StringUtils.isNotBlank(subjectManagementDTO.getSubjectName())){
                    predicateList.add(criteriaBuilder.like(root.get("subjectName"),subjectManagementDTO.getSubjectName()));
                }
                //科目类型  1---应收  0--应付
                if (StringUtils.isNotBlank(subjectManagementDTO.getSubjectType())){
                    predicateList.add(criteriaBuilder.equal(root.get("subjectType"),subjectManagementDTO.getSubjectType()));
                }
                //科目状态  1--可用  0--停用
                if (StringUtils.isNotBlank(subjectManagementDTO.getSubjectStatus())){
                    predicateList.add(criteriaBuilder.equal(root.get("subjectStatus"),subjectManagementDTO.getSubjectStatus()));
                }
                //查询没有被删除的数据
                predicateList.add(criteriaBuilder.equal(root.get("status"),"1"));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }




    private Page<SubjectManagementDTO> converterDto(Page<SubjectManagement> page) {
        return  page.map(new Converter<SubjectManagement, SubjectManagementDTO>() {
            @Override
            public SubjectManagementDTO convert(SubjectManagement subjectManagement) {

                SubjectManagementDTO sm=new SubjectManagementDTO();

                BeanUtils.copyProperties(subjectManagement,sm);


                return sm;
            }
        });
    }



    //新建科目管理
    @Override
    public Boolean saveSubjectManagement(SubjectManagementDTO subjectManagementDTO) {

        SubjectManagement sm=new SubjectManagement();
        BeanUtils.copyProperties(subjectManagementDTO,sm);
        sm.setStatus("1");
        //新增时自动设置科目编号
        String str= subjectManagementRepository.subjectCode();
        if (str==null){
            sm.setSubjectCode("00001");
        }else {
            String  setSubjectCode=String.valueOf(Integer.parseInt(str)+1);

            if (setSubjectCode.length() == 1) {
                sm.setSubjectCode("0000" + setSubjectCode);
            } else if (setSubjectCode.length() == 2) {
                sm.setSubjectCode("000" + setSubjectCode);
            } else if (setSubjectCode.length() == 3) {
                sm.setSubjectCode("00" + setSubjectCode);
            } else if (setSubjectCode.length() == 4) {
                sm.setSubjectCode("0" + setSubjectCode);
            } else {
                sm.setSubjectCode(setSubjectCode);
            }
        }


        subjectManagementRepository.save(sm);

        return true;
    }

    //编辑科目信息
    public Boolean updateSubject(SubjectManagementDTO subjectManagementDTO){

        if (subjectManagementDTO.getId() !=null){
            SubjectManagement sm=  subjectManagementRepository.findOne(subjectManagementDTO.getId());
            BeanUtils.copyProperties(subjectManagementDTO,sm, "id", "subjectCode");
//            sm.setSubjectCode(sm.getSubjectCode());
            subjectManagementRepository.save(sm);
            return  true;
        }else {
            return  false;
        }
    }


    //删除科目信息
    public  Boolean deleteSubject(SubjectManagementDTO subjectManagementDTO){
        if (subjectManagementDTO.getId() !=null){
          SubjectManagement sm=  subjectManagementRepository.findOne(subjectManagementDTO.getId());
          sm.setStatus("0");
          subjectManagementRepository.save(sm);
            return  true;
        }else {
            return false;
        }
    }






}
