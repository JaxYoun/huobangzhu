package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.po.VehicleInformation;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.repository.VehicleInformationRepository;
import com.troy.keeper.management.service.VehicleInformationService;
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
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
@Service
@Transactional
public class VehicleInformationServiceImpl implements VehicleInformationService {
    @Autowired
    private VehicleInformationRepository vehicleInformationRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;


    //分页查询
    @Override
    public Page<VehicleInformationDTO> findByCondition(VehicleInformationDTO vehicleInformationDTO, Pageable pageable) {

        vehicleInformationDTO.setDataStatus("1");

        Page<VehicleInformation> page=vehicleInformationRepository.findAll(new Specification<VehicleInformation>() {
            @Override
            public Predicate toPredicate(Root<VehicleInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

            if (StringUtils.isNotBlank(vehicleInformationDTO.getNumberPlate())){
                //车牌号
                predicateList.add(criteriaBuilder.equal(root.get("numberPlate"),vehicleInformationDTO.getNumberPlate()));
            }
            if (StringUtils.isNotBlank(vehicleInformationDTO.getOwnersName())){
                //车主姓名
                predicateList.add(criteriaBuilder.like(root.get("ownersName"),vehicleInformationDTO.getOwnersName()));
            }
            if (StringUtils.isNotBlank(vehicleInformationDTO.getDriverName())){
                //司机姓名
                predicateList.add(criteriaBuilder.like(root.get("driverName"),vehicleInformationDTO.getDriverName()));
            }
            //状态为1的
                predicateList.add(criteriaBuilder.equal(root.get("status"),vehicleInformationDTO.getDataStatus()));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }




    private Page<VehicleInformationDTO> converterDto(Page<VehicleInformation> page) {
        return  page.map(new Converter<VehicleInformation, VehicleInformationDTO>() {
            @Override
            public VehicleInformationDTO convert(VehicleInformation vehicleInformation) {

                VehicleInformationDTO ui=new VehicleInformationDTO();
                BeanUtils.copyProperties(vehicleInformation,ui);

                //下拉选  取数据字典的name值
                String  name= userInformationRepository.findUserClassification("VehicleType",vehicleInformation.getVehicleType());
                ui.setVehicleTypeValue(name);
                return ui;
            }
        });
    }

    //新增车辆信息
    @Override
    public Boolean addVehicleInformation(VehicleInformationDTO vehicleInformationDTO) {

        VehicleInformation vi=new VehicleInformation();
        BeanUtils.copyProperties(vehicleInformationDTO,vi);
        //新增时自动设置物流编号
        String str= vehicleInformationRepository.vehicleNumber();
        String  vehicleNumber=String.valueOf(Integer.parseInt(str)+1);
        if (str==null){
            vi.setVehicleNumber("00001");
        }else {

            if (vehicleNumber.length() == 1) {
                vi.setVehicleNumber("0000" + vehicleNumber);
            } else if (vehicleNumber.length() == 2) {
                vi.setVehicleNumber("000" + vehicleNumber);
            } else if (vehicleNumber.length() == 3) {
                vi.setVehicleNumber("00" + vehicleNumber);
            } else if (vehicleNumber.length() == 4) {
                vi.setVehicleNumber("0" + vehicleNumber);
            } else {
                vi.setVehicleNumber(vehicleNumber);
            }
        }
        vi.setStatus("1");
        vehicleInformationRepository.save(vi);
        return true;
    }

    //新增时车牌号 重复校验
    @Override
    public Boolean numberPlate(VehicleInformationDTO vehicleInformationDTO) {
      if (vehicleInformationDTO.getNumberPlate() !=null){
          Long  numberPlate=  vehicleInformationRepository.numberPlate(vehicleInformationDTO.getNumberPlate());
          if (numberPlate >0){
              return false;
          }
      }
        return   true;
    }

    //新增车编号号校验重复
//    @Override
//    public Boolean vehicleNumber(VehicleInformationDTO vehicleInformationDTO) {
//        if (vehicleInformationDTO.getVehicleNumber() !=null){
//            Long  s=  vehicleInformationRepository.vehicleNumber(vehicleInformationDTO.getVehicleNumber());
//              if (s>0){
//                  return  false;
//              }
//        }
//        return true;
//    }
    //车主的电话号码重复校验
    @Override
    public Boolean ownersTelephone(VehicleInformationDTO vehicleInformationDTO) {
        if (vehicleInformationDTO.getOwnersTelephone() !=null){
            Long  s=  vehicleInformationRepository.ownersTelephone(vehicleInformationDTO.getOwnersTelephone());
            if (s>0){
                return  false;
            }
        }
        return true;
    }

    //车主证件号
    @Override
    public Boolean ownerNumber(VehicleInformationDTO vehicleInformationDTO) {
        if (vehicleInformationDTO.getOwnerNumber() !=null){
            Long  s=  vehicleInformationRepository.ownerNumber(vehicleInformationDTO.getOwnerNumber());
            if (s>0){
                return  false;
            }
        }
        return true;
    }

    //司机电话号
    @Override
    public Boolean driverTelephone(VehicleInformationDTO vehicleInformationDTO) {
        if (vehicleInformationDTO.getDriverTelephone() !=null){
            Long  s=  vehicleInformationRepository.driverTelephone(vehicleInformationDTO.getDriverTelephone());
            if (s>0){
                return  false;
            }
        }
        return true;
    }
    //司机证件号
    @Override
    public Boolean driverNumber(VehicleInformationDTO vehicleInformationDTO) {
        if (vehicleInformationDTO.getDriverNumber() !=null){
            Long  s=  vehicleInformationRepository.driverNumber(vehicleInformationDTO.getDriverNumber());
            if (s>0){
                return  false;
            }
        }
        return true;
    }




    //修改车辆信息
    @Override
    public Boolean updateVehicleInformation(VehicleInformationDTO vehicleInformationDTO) {

        VehicleInformation vi=new VehicleInformation();
            VehicleInformation vehicleInformation = vehicleInformationRepository.findOne(vehicleInformationDTO.getId());
            BeanUtils.copyProperties(vehicleInformationDTO, vi);
            vi.setVehicleNumber(vehicleInformation.getVehicleNumber());
            vi.setStatus("1");
            vehicleInformationRepository.save(vi);
        return true;
    }
    //修改车牌号重复校验
    @Override
    public Boolean updateNumberPlate(VehicleInformationDTO vehicleInformationDTO) {
      Long  s=  vehicleInformationRepository.updateNumberPlate(vehicleInformationDTO.getNumberPlate(),vehicleInformationDTO.getId());
      if (s>0){
          return false;
      }
        return true;
    }

//    //修改编号重复
//    @Override
//    public Boolean updateVehicleNumber(VehicleInformationDTO vehicleInformationDTO) {
//        Long  s=  vehicleInformationRepository.updateVehicleNumber(vehicleInformationDTO.getVehicleNumber(),vehicleInformationDTO.getId());
//        if (s>0){
//            return false;
//        }
//        return true;
//    }
    //修改车主电话
    @Override
    public Boolean updateOwnersTelephone(VehicleInformationDTO vehicleInformationDTO) {
        Long  s=  vehicleInformationRepository.updateOwnersTelephone(vehicleInformationDTO.getOwnersTelephone(),vehicleInformationDTO.getId());
        if (s>0){
            return false;
        }
        return true;
    }
    //修改车主证件号
    @Override
    public Boolean updateOwnerNumber(VehicleInformationDTO vehicleInformationDTO) {
        Long  s=  vehicleInformationRepository.updateOwnerNumber(vehicleInformationDTO.getOwnerNumber(),vehicleInformationDTO.getId());
        if (s>0){
            return false;
        }
        return true;
    }
    //修改司机电话
    @Override
    public Boolean updateDriverTelephone(VehicleInformationDTO vehicleInformationDTO) {
        Long  s=  vehicleInformationRepository.updateDriverTelephone(vehicleInformationDTO.getDriverTelephone(),vehicleInformationDTO.getId());
        if (s>0){
            return false;
        }
        return true;
    }
    //修改司机证件号
    @Override
    public Boolean updateDriverNumber(VehicleInformationDTO vehicleInformationDTO) {
        Long  s=  vehicleInformationRepository.updateDriverNumber(vehicleInformationDTO.getDriverNumber(),vehicleInformationDTO.getId());
        if (s>0){
            return false;
        }
        return true;
    }


    //删除车辆信息
    @Override
    public Boolean deleteVehicleInformation(VehicleInformationDTO vehicleInformationDTO) {

              vehicleInformationRepository.deleteVehicleInformation(vehicleInformationDTO.getId());

        return true;
    }


}
