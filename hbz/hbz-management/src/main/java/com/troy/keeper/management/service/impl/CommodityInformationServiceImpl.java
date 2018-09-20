package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.po.CommodityInformation;
import com.troy.keeper.hbz.po.VehicleInformation;
import com.troy.keeper.management.dto.CommodityInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.repository.CommodityInformationRepository;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.service.CommodityInformationService;
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
 * @date 2017/12/24.
 */
@Service
@Transactional
public class CommodityInformationServiceImpl implements CommodityInformationService {
    @Autowired
    private CommodityInformationRepository commodityInformationRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;



    //分页查询
    @Override
    public Page<CommodityInformationDTO> findByCondition(CommodityInformationDTO commodityInformationDTO, Pageable pageable) {
        commodityInformationDTO.setDataStatus("1");


        Page<CommodityInformation> page=commodityInformationRepository.findAll(new Specification<CommodityInformation>() {
            @Override
            public Predicate toPredicate(Root<CommodityInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(commodityInformationDTO.getCommodityNumber())){
                //货物编号
                predicateList.add(criteriaBuilder.equal(root.get("commodityNumber"),commodityInformationDTO.getCommodityNumber()));
            }
            if (StringUtils.isNotBlank(commodityInformationDTO.getCommodityName())){
                //货物名称
                predicateList.add(criteriaBuilder.like(root.get("commodityName"),"%"+commodityInformationDTO.getCommodityName()+"%"));
            }
            if (StringUtils.isNotBlank(commodityInformationDTO.getJianpin())){
                predicateList.add(criteriaBuilder.like(root.get("jianpin"),"%"+commodityInformationDTO.getJianpin()+"%"));
            }
                predicateList.add(criteriaBuilder.equal(root.get("status"),commodityInformationDTO.getDataStatus()));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }



    private Page<CommodityInformationDTO> converterDto(Page<CommodityInformation> page) {
        return  page.map(new Converter<CommodityInformation, CommodityInformationDTO>() {
            @Override
            public CommodityInformationDTO convert(CommodityInformation commodityInformation) {

                CommodityInformationDTO ci=new CommodityInformationDTO();
                BeanUtils.copyProperties(commodityInformation,ci);

                String  name= userInformationRepository.findUserClassification("PackageUnit",commodityInformation.getPackageUnit());
                ci.setPackageUnitValue(name);
                return ci;
            }
        });
    }
    //新增货物信息
    @Override
    public Boolean addCommodityInformation(CommodityInformationDTO commodityInformationDTO) {
        CommodityInformation ci=new CommodityInformation();
        BeanUtils.copyProperties(commodityInformationDTO,ci);
        //新增时自动设置物流编号
        String str= commodityInformationRepository.commodityNumber();
        if (str==null){
            ci.setCommodityNumber("00001");
        }else {
            String  commodityNumber=String.valueOf(Integer.parseInt(str)+1);

            if (commodityNumber.length() == 1) {
                ci.setCommodityNumber("0000" + commodityNumber);
            } else if (commodityNumber.length() == 2) {
                ci.setCommodityNumber("000" + commodityNumber);
            } else if (commodityNumber.length() == 3) {
                ci.setCommodityNumber("00" + commodityNumber);
            } else if (commodityNumber.length() == 4) {
                ci.setCommodityNumber("0" + commodityNumber);
            } else {
                ci.setCommodityNumber(commodityNumber);
            }
        }
        ci.setStatus("1");
        commodityInformationRepository.save(ci);

        return true;
    }

    //新增货物名称重复
    @Override
    public Boolean commodityName(CommodityInformationDTO commodityInformationDTO) {
       Long   s=  commodityInformationRepository.commodityName(commodityInformationDTO.getCommodityName());
         if (s>0){
             return   false;
         }
        return true;
    }
    ////新增货物条码校验
    @Override
    public Boolean barcode(CommodityInformationDTO commodityInformationDTO) {
        Long   s=  commodityInformationRepository.barcode(commodityInformationDTO.getBarcode());
        if (s>0){
            return   false;
        }
        return true;
    }


    //修改货物信息
    @Override
    public Boolean updateCommodityInformation(CommodityInformationDTO commodityInformationDTO) {
        CommodityInformation ci=new CommodityInformation();
        BeanUtils.copyProperties(commodityInformationDTO,ci);
        ci.setStatus("1");
        commodityInformationRepository.save(ci);

        return true;
    }
     //修改货物名称不能重复
    @Override
    public Boolean updateCommodityName(CommodityInformationDTO commodityInformationDTO) {
       Long  s=   commodityInformationRepository.updateCommodityName(commodityInformationDTO.getCommodityName(),commodityInformationDTO.getId());
        if (s>0){
            return  false;
        }
        return true;
    }
    //修改货物条码不能重复
    @Override
    public Boolean updateBarcode(CommodityInformationDTO commodityInformationDTO) {
        Long  s=   commodityInformationRepository.updateBarcode(commodityInformationDTO.getBarcode(),commodityInformationDTO.getId());
        if (s>0){
            return  false;
        }
        return true;
    }

    //删除货物信息
    @Override
    public Boolean deletaCommodityInformation(CommodityInformationDTO commodityInformationDTO) {
        if (commodityInformationDTO.getId() !=null){
            commodityInformationRepository.deleteCommodityInformation(commodityInformationDTO.getId());
        }

        return true;
    }


}
