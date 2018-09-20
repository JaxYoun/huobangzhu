package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.HbzVehicleInformationDTO;
import com.troy.keeper.hbz.po.HbzTransSize;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.po.HbzVehicleInformation;
import com.troy.keeper.hbz.repository.HbzTransSizeRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.repository.HbzVehicleInformationRepository;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.HbzVehicleInformationService;
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
import java.util.stream.Collectors;

/**
 * @Autohor: hecj
 * @Description: 车辆管理ServiceImpl
 * @Date: Created in 14:57  2018/1/30.
 * @Midified By:
 */
@Service
@Transactional
public class HbzVehicleInformationServiceImpl implements HbzVehicleInformationService {

    @Autowired
    private HbzTransSizeRepository hbzTransSizeRepository;

    @Autowired
    private HbzVehicleInformationRepository hbzVehicleInformationRepository;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzUserRepository hbzUserRepository;

    @Override
    public Page<HbzVehicleInformationDTO> queryVehicleInformations(HbzVehicleInformationDTO hbzVehicleInformationDTO, Pageable pageable) {
        Page<HbzVehicleInformation> pageList = hbzVehicleInformationRepository.findAll(new Specification<HbzVehicleInformation>() {
            @Override
            public Predicate toPredicate(Root<HbzVehicleInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                Long id = hbzUserService.currentUser().getId();
                if (null != id && !"".equals(id)) {
                    predicateList.add(criteriaBuilder.equal(root.get("user").get("id"), id));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), "1"));
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
        Page<HbzVehicleInformationDTO> hbzVehicleInformation = pageList.map(new Converter<HbzVehicleInformation, HbzVehicleInformationDTO>() {
            @Override
            public HbzVehicleInformationDTO convert(HbzVehicleInformation hbzVehicleInformation) {
                HbzVehicleInformationDTO vehicleInformationDTO = new HbzVehicleInformationDTO();
                BeanUtils.copyProperties(hbzVehicleInformation, vehicleInformationDTO);
                vehicleInformationDTO.setTransTypeName(hbzVehicleInformation.getTransType().getName());
                List<Double> doubleSizes = new ArrayList<>();
                List<HbzTransSize> transSizes = hbzVehicleInformation.getTransSizes();
                for (HbzTransSize hbzTransSize : transSizes) {
                    doubleSizes.add(hbzTransSize.getTransSize());
                }
                vehicleInformationDTO.setTransSizes(doubleSizes);
                //HbzUser user = hbzUserRepository.findOne(hbzUserService.currentUser().getId());
                //List<Role> roles = new ArrayList<>();
                //List<HbzRole> hbzRoleList = user.getRoles();
                //for(HbzRole hbzRole:hbzRoleList){
                //    roles.add(hbzRole.getRole());
                //}
                //vehicleInformationDTO.setRoleTypes(roles);
                return vehicleInformationDTO;
            }
        });
        return hbzVehicleInformation;
    }

    @Override
    public boolean addVehicleInformation(HbzVehicleInformationDTO vehicleInformationDTO) {
        HbzVehicleInformation vehicleInformation = new HbzVehicleInformation();
        BeanUtils.copyProperties(vehicleInformationDTO, vehicleInformation, "user", "transSizes");
        HbzUser user = hbzUserRepository.findOne(hbzUserService.currentUser().getId());
        vehicleInformation.setUser(user);
        vehicleInformation.setStatus("1");
        vehicleInformation.setTransSizes(vehicleInformationDTO.getTransSizes().stream().map(hbzTransSizeRepository::findByTransSize).collect(Collectors.toList()));
        return hbzVehicleInformationRepository.save(vehicleInformation) != null;
    }

    @Override
    public boolean updateVehicleInformation(HbzVehicleInformationDTO vehicleInformationDTO) {
        HbzVehicleInformation vehicleInformation = hbzVehicleInformationRepository.findOne(vehicleInformationDTO.getId());
        if (null != vehicleInformation) {
            BeanUtils.copyProperties(vehicleInformationDTO, vehicleInformation, "user", "transSizes");
            HbzUser user = hbzUserRepository.findOne(hbzUserService.currentUser().getId());
            vehicleInformation.setUser(user);
            vehicleInformation.setTransSizes(vehicleInformationDTO.getTransSizes().stream().map(hbzTransSizeRepository::findByTransSize).collect(Collectors.toList()));
            return hbzVehicleInformationRepository.save(vehicleInformation) != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteVehicleInformation(HbzVehicleInformationDTO vehicleInformationDTO) {
        HbzVehicleInformation vehicleInformation = hbzVehicleInformationRepository.findOne(vehicleInformationDTO.getId());
        vehicleInformation.setStatus("0");
        if (hbzVehicleInformationRepository.save(vehicleInformation) != null)
            return true;
        else
            return false;
    }
}
