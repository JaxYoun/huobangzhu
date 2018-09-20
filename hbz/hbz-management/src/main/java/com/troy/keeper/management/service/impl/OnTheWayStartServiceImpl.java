package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.IsUnload;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.management.dto.CommodityInformationDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.repository.*;
import com.troy.keeper.management.service.OnTheWayStartService;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.repository.SmUserRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2018/1/9.
 */
@Service
@Transactional
public class OnTheWayStartServiceImpl implements OnTheWayStartService {
    @Autowired
    private StartVehicleRepository startVehicleRepository;
    @Autowired
    private HbzAreasRepository hbzAreaRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private StartVehicleInformationRepository startVehicleInformationRepository;

    //修改收货库存  及状态
    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    @Autowired
    private VehicleInformationRepository vehicleInformationRepository;

    @Autowired
    private SmUserRepository smUserRepository;



    ////////////////////司机确认分页查询/////////////////////////////////
    //分页查询
    @Override
    public Page<StartVehicleDTO> driverConfirmed(StartVehicleDTO startVehicleDTO, Pageable pageable) {

        Page<StartVehicle> page=startVehicleRepository.findAll(new Specification<StartVehicle>() {
            @Override
            public Predicate toPredicate(Root<StartVehicle> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                if (StringUtils.isNotBlank(startVehicleDTO.getTrackingNumber())){
                    //物流编号
                    predicateList.add(criteriaBuilder.equal(root.get("trackingNumber"),startVehicleDTO.getTrackingNumber()));
                }
                if (StringUtils.isNotBlank(startVehicleDTO.getWaybillNumber())){
                    //运单编号
                    predicateList.add(criteriaBuilder.equal(root.get("waybillNumber"),startVehicleDTO.getWaybillNumber()));
                }
                if (StringUtils.isNotBlank(startVehicleDTO.getCommodityName())){
                    //货物名称
                    predicateList.add(criteriaBuilder.like(root.get("commodityName"),startVehicleDTO.getCommodityName()));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"),"1"));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }

    //司机确认装货
    @Override
    public Boolean updateStatus(StartVehicleDTO startVehicleDTO) {
        if (startVehicleDTO.getId() !=null){
            //修改货物的状态为0
            startVehicleRepository.updateStatus(startVehicleDTO.getId());
            //司机确认后 更新货物的状态
            StartVehicle  startVehicle= startVehicleRepository.findOne(startVehicleDTO.getId());
            startVehicle.setShippingStatus(ShippingStatus.DURING_SHIPPING);
            startVehicle.setStatus("0");
            startVehicleRepository.save(startVehicle);

            return true;
        }else {
            return false;
        }


    }


    private Page<StartVehicleDTO> converterDto(Page<StartVehicle> page) {
        return  page.map(new Converter<StartVehicle, StartVehicleDTO>() {
            @Override
            public StartVehicleDTO convert(StartVehicle startVehicle) {
                StartVehicleDTO  sv=new StartVehicleDTO();

                BeanUtils.copyProperties(startVehicle,sv);

                if (startVehicle.getVehicleInformation() !=null){
                    sv.setNumberPlate(startVehicle.getVehicleInformation().getNumberPlate());
                    sv.setVehicleType(startVehicle.getVehicleInformation().getVehicleType());
                    String  name= userInformationRepository.findUserClassification("VehicleType",startVehicle.getVehicleInformation().getVehicleType());
                    sv.setVehicleTypeValue(name);
                    sv.setDriverName(startVehicle.getVehicleInformation().getDriverName());
                    sv.setDriverTelephone(startVehicle.getVehicleInformation().getDriverTelephone());

                }
//                if ( startVehicle.getTransitState() !=null){
//                    sv.setTransitStateValue(startVehicle.getTransitState().getName());
//                }
                //发车时间
                if ( startVehicle.getReceiptDate() !=null){
                    FormatedDate logIn = new FormatedDate(startVehicle.getReceiptDate());
                    String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
                    sv.setReceiptDate(receiptDate);
                }
                //预计到达时间
                if ( startVehicle.getReceiptToDate() !=null){
                    FormatedDate logIn2 = new FormatedDate(startVehicle.getReceiptToDate());
                    String receiptToDate = logIn2.getFormat("yyyy-MM-dd HH:mm:ss");
                    sv.setReceiptDate(receiptToDate);
                }
//                //取货地址
                HbzArea currentLevelOriginArea= startVehicle.getOriginArea();
                LinkedList<Long> longList=new LinkedList<>();
                StringBuilder sb = new StringBuilder();
                while(currentLevelOriginArea.getLevel() >0 ){
                    sb.insert(0,currentLevelOriginArea.getAreaName() + " " );
                    switch(currentLevelOriginArea.getLevel().intValue()){
                        case 1:{
                            sv.setProvinceId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            sv.setStartCity(longList);
                        }break;
                        case 2:{
                            sv.setCityId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            sv.setStartCity(longList);
                        }break;
                        case 3:{
                            sv.setCountyId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            sv.setStartCity(longList);
                        }break;
                    }

                    currentLevelOriginArea = currentLevelOriginArea.getParent();
                }
                sv.setOriginArea(sb.toString());
//                StringBuffer sb = new StringBuffer();
//                if (startVehicle.getOriginArea() != null) {
//                    Integer level = startVehicle.getOriginArea().getLevel();
//                    if (level == 1) {//省
//                        sb.append(startVehicle.getOriginArea().getAreaName());
//                    } else if (level == 2) {//市
//                        HbzArea hbzAreaCity = startVehicle.getOriginArea();
//                        sb.append(hbzAreaCity.getAreaName());
//                        sb.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                        sv.setOriginArea(sb.toString());
//                    } else if (level == 3) {//区县
//                        HbzArea hbzAreaCounty = startVehicle.getOriginArea();
//                        HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                        HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                        sv.setOriginArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                    }
//                }


                //送货地址
                HbzArea currentLevelArea = startVehicle.getDestArea();
                LinkedList<Long> endList=new LinkedList<>();
                StringBuilder ss = new StringBuilder();
                while(currentLevelArea.getLevel() >0 ){
                    ss.insert(0,currentLevelArea.getAreaName() + " " );
                    switch(currentLevelArea.getLevel().intValue()){
                        case 1:{
                            sv.setProvinceToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            sv.setEndCity(endList);
                        }break;
                        case 2:{
                            sv.setCityToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            sv.setEndCity(endList);
                        }break;
                        case 3:{
                            sv.setCountyToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            sv.setEndCity(endList);
                        }break;
                    }

                    currentLevelArea = currentLevelArea.getParent();
                }
                sv.setDestArea(ss.toString());
//                StringBuffer sbf = new StringBuffer();
//                if (startVehicle.getDestArea() != null) {
//                    Integer level = startVehicle.getDestArea().getLevel();
//                    if (level == 1) {//省
//                        sbf.append(startVehicle.getDestArea().getAreaName());
//                    } else if (level == 2) {//市
//                        HbzArea hbzAreaCity = startVehicle.getDestArea();
//                        sbf.append(hbzAreaCity.getAreaName());
//                        sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                        sv.setDestArea(sbf.toString());
//                    } else if (level == 3) {//区县
//                        HbzArea hbzAreaCounty = startVehicle.getDestArea();
//                        HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                        HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                        sv.setDestArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                    }
//                }


                return sv;
            }
        });
    }

///////////////在途发车分页查询////////////////////////////
    @Override
    public Page<StartVehicleInformationDTO> findByCondition(StartVehicleInformationDTO startVehicleInformationDTO, Pageable pageable) {


        Page<StartVehicleInformation> page=startVehicleInformationRepository.findAll(new Specification<StartVehicleInformation>() {
            @Override
            public Predicate toPredicate(Root<StartVehicleInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (startVehicleInformationDTO.getSmallTime() !=null){
                    predicateList.add(criteriaBuilder.ge(root.get("receiptDate"),startVehicleInformationDTO.getSmallTime()));
                }
                if (startVehicleInformationDTO.getBigTime() !=null){
                    predicateList.add(criteriaBuilder.le(root.get("receiptDate"),startVehicleInformationDTO.getBigTime()+59999L));
                }
                //发出
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getOriginAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"),startVehicleInformationDTO.getOriginAreaCode()));
                }
//                //省
//                if (startVehicleInformationDTO.getProvinceId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(startVehicleInformationDTO.getProvinceId());
//                    predicateList.add(root.get("originArea").get("id").in(childIds));
//                }
//                //市
//                if (startVehicleInformationDTO.getCityId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(startVehicleInformationDTO.getCityId());
//                    predicateList.add(root.get("originArea").get("id").in(childIds));
//                }
//                //区
//                if (startVehicleInformationDTO.getCountyId() != null){
//                    predicateList.add(criteriaBuilder.equal(root.get("originArea").get("id"),startVehicleInformationDTO.getCountyId()));
//                }
                //到站
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getDestAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"),startVehicleInformationDTO.getDestAreaCode()));
                }
//                //省
//                if (startVehicleInformationDTO.getProvinceToId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(startVehicleInformationDTO.getProvinceToId());
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //市
//                if (startVehicleInformationDTO.getCityToId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(startVehicleInformationDTO.getCityToId());
//
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //区
//                if (startVehicleInformationDTO.getCountyToId() != null){
//
//                    predicateList.add(criteriaBuilder.equal(root.get("destArea").get("id"),startVehicleInformationDTO.getCountyToId()));
//                }

                if (startVehicleInformationDTO.getTransitState() !=null){
                    //在图状态
                    predicateList.add(criteriaBuilder.equal(root.get("transitState"),startVehicleInformationDTO.getTransitState()));
                }
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getStartNumber())){
                    //发车编号
                    predicateList.add(criteriaBuilder.equal(root.get("startNumber"),startVehicleInformationDTO.getStartNumber()));
                }
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getNumberPlate())){
                    //车牌号
                    predicateList.add(criteriaBuilder.equal(root.join("vehicleInformation").get("numberPlate"),startVehicleInformationDTO.getNumberPlate()));
                }
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getDriverName())){
                    //司机名称
                    predicateList.add(criteriaBuilder.like(root.join("vehicleInformation").get("driverName"),"%"+startVehicleInformationDTO.getDriverName()+"%"));
                }
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getDriverTelephone())){
                    //司机电话
                    predicateList.add(criteriaBuilder.equal(root.join("vehicleInformation").get("driverTelephone"),startVehicleInformationDTO.getDriverTelephone()));
                }
                if (StringUtils.isNotBlank(startVehicleInformationDTO.getVehicleType())){
                    //车辆类型
                    predicateList.add(criteriaBuilder.equal(root.join("vehicleInformation").get("vehicleType"),startVehicleInformationDTO.getVehicleType()));
                }


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return startVehicleInformationDTO(page);
    }



    private Page<StartVehicleInformationDTO> startVehicleInformationDTO(Page<StartVehicleInformation> page) {
        //判断机构新建权限
        Long currentUserId = SecurityUtils.getCurrentUserId();
        SmUser smUser = smUserRepository.findOne(currentUserId);

        return  page.map(
        sv->{
            StartVehicleInformationDTO svd = new StartVehicleInformationDTO();
            //重量
            Optional<Double> optional = sv.getStartVehicle().stream().map(a->{if(a.getInstalledWeight() == null) a.setInstalledWeight(0D);return a;}).map(StartVehicle::getInstalledWeight).reduce((a,b)->a+b);
            Double tota = optional.isPresent()?optional.get():0D;
            new Bean2Bean().copyProperties(sv,svd);
            svd.setInstalledWeight(tota);

            //体积
//            Double tota1 = sv.getStartVehicle().stream().map(a->{if(a.getInstalledVolume() == null) a.setInstalledVolume(0D);return a;}).map(StartVehicle::getInstalledVolume).reduce((a,b)->a+b).get();
            Optional<Double> optional1 = sv.getStartVehicle().stream().map(a->{if(a.getInstalledVolume() == null) a.setInstalledVolume(0D);return a;}).map(StartVehicle::getInstalledVolume).reduce((a,b)->a+b);
            Double tota1 = optional1.isPresent()?optional1.get():0D;
            new Bean2Bean().copyProperties(sv,svd);
            svd.setInstalledVolume(tota1);

            //车辆信息
            VehicleInformationDTO vif=new VehicleInformationDTO();
            BeanUtils.copyProperties(sv.getVehicleInformation(),vif);
            if (sv.getVehicleInformation() !=null){
                String nameVehicleType = userInformationRepository.findUserClassification("VehicleType", sv.getVehicleInformation().getVehicleType());
                vif.setVehicleTypeValue(nameVehicleType);
            }

            svd.setVehicleInformationDTO(vif);

            if ( sv.getReceiptDate() !=null){
                FormatedDate logIn = new FormatedDate(sv.getReceiptDate());
                String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm");
                svd.setReceiptDate(receiptDate);
            }

            if ( sv.getReceiptToDate() !=null){
                FormatedDate logInreceiptDateReceiptToDate = new FormatedDate(sv.getReceiptToDate());
                String receiptDateReceiptToDate = logInreceiptDateReceiptToDate.getFormat("yyyy-MM-dd HH:mm");
                svd.setReceiptToDate(receiptDateReceiptToDate);
            }
            //区域的code值
            if (sv.getOriginArea() !=null){
                svd.setOriginAreaCode(sv.getOriginArea().getOutCode());
            }
            if (sv.getDestArea() !=null){
                svd.setDestAreaCode(sv.getDestArea().getOutCode());
            }

            //取货地址
            HbzArea currentLevelOriginArea= sv.getOriginArea();
            LinkedList<Long> longList=new LinkedList<>();
            if (currentLevelOriginArea !=null) {
                StringBuilder sb = new StringBuilder();
                while (currentLevelOriginArea.getLevel() > 0) {
                    sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                    switch (currentLevelOriginArea.getLevel().intValue()) {
                        case 1: {
                            svd.setProvinceId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            svd.setStartCity(longList);
                        }
                        break;
                        case 2: {
                            svd.setCityId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            svd.setStartCity(longList);
                        }
                        break;
                        case 3: {
                            svd.setCountyId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            svd.setStartCity(longList);
                        }
                        break;
                    }

                    currentLevelOriginArea = currentLevelOriginArea.getParent();
                }
                svd.setOriginArea(sb.toString());
            }
            //送货地址
            HbzArea currentLevelArea = sv.getDestArea();
            LinkedList<Long> endList=new LinkedList<>();
            if (currentLevelArea !=null) {
                StringBuilder ss = new StringBuilder();
                while (currentLevelArea.getLevel() > 0) {
                    ss.insert(0, currentLevelArea.getAreaName() + " ");
                    switch (currentLevelArea.getLevel().intValue()) {
                        case 1: {
                            svd.setProvinceToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            svd.setEndCity(endList);
                        }
                        break;
                        case 2: {
                            svd.setCityToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            svd.setEndCity(endList);
                        }
                        break;
                        case 3: {
                            svd.setCountyToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            svd.setEndCity(endList);
                        }
                        break;
                    }

                    currentLevelArea = currentLevelArea.getParent();
                }
                svd.setDestArea(ss.toString());
            }

//            List<StartVehicleDTO> listSV=new ArrayList<>();
//            StartVehicleDTO  SVD=new StartVehicleDTO();
//            BeanUtils.copyProperties(sv.getStartVehicle(),SVD);
//            listSV.add(SVD);
//            svd.setStartVehicleDTOS(listSV);


            //状态的枚举值
            if (sv.getTransitState() !=null) {
                svd.setTransitStateValue(sv.getTransitState().getName());
            }

            if (sv.getShippingStatus()!=null){
                if(smUser.getOrgId().equals(sv.getSmOrgId()) && ShippingStatus.NEW.equals(sv.getShippingStatus())){
                    svd.setNewlyCreatedDisplay("1");//显示新增、删除操作
                }else{
                    svd.setNewlyCreatedDisplay("0");//不显示新增、删除操作
                }

                svd.setShippingStatusValue(sv.getShippingStatus().getName());
            }
            return svd;
        }
                );
    }


    //编辑时 修改装车 车的状态和备注
    @Override
    public Boolean updateStartVehicleInformation(StartVehicleInformationDTO startVehicleInformationDTO) {

        if (startVehicleInformationDTO.getId() !=null){
            startVehicleInformationRepository.updateStartVehicleInformation(startVehicleInformationDTO.getId(),startVehicleInformationDTO.getTransitState(),startVehicleInformationDTO.getRemarks());
            return true;
        }else {
            return  false;
        }


    }


    //通过车辆的id  查询车中的货物信息
    @Override
    public StartVehicleInformationDTO findAllStartVehicleInformation(StartVehicleInformationDTO startVehicleInformationDTO) {
        StartVehicleInformationDTO svi=new StartVehicleInformationDTO();
        //车辆的信息
        StartVehicleInformation  startVehicleInformation= startVehicleInformationRepository.selectAllStartVehicleInformation(startVehicleInformationDTO.getId());
        //车辆 下的货物集合
        List<StartVehicle>  listStartVehicle= startVehicleInformationRepository.selctStartVehicleIn(startVehicleInformationDTO.getId());
         BeanUtils.copyProperties(startVehicleInformation,svi);

         if (startVehicleInformation.getVehicleInformation() !=null){
             svi.setVehicleInformationId(startVehicleInformation.getVehicleInformation().getId());
         }

        if (startVehicleInformation.getTransitState() !=null){
            svi.setTransitStateValue(startVehicleInformation.getTransitState().getName());
        }
        //区域值
        if (startVehicleInformation.getOriginArea() !=null){
            svi.setOriginAreaCode(startVehicleInformation.getOriginArea().getOutCode());
        }
        if (startVehicleInformation.getDestArea() !=null){
            svi.setDestAreaCode(startVehicleInformation.getDestArea().getOutCode());
        }


        //取货地址
        HbzArea currentOriginArea= startVehicleInformation.getOriginArea();
        LinkedList<Long> longList1=new LinkedList<>();
        if (currentOriginArea !=null) {
            StringBuilder sb = new StringBuilder();
            while (currentOriginArea.getLevel() > 0) {
                sb.insert(0, currentOriginArea.getAreaName() + " ");
                switch (currentOriginArea.getLevel().intValue()) {
                    case 1: {
                        svi.setProvinceId(currentOriginArea.getId());
                        longList1.addFirst(currentOriginArea.getId());
                        svi.setStartCity(longList1);
                    }
                    break;
                    case 2: {
                        svi.setCityId(currentOriginArea.getId());
                        longList1.addFirst(currentOriginArea.getId());
                        svi.setStartCity(longList1);
                    }
                    break;
                    case 3: {
                        svi.setCountyId(currentOriginArea.getId());
                        longList1.addFirst(currentOriginArea.getId());
                        svi.setStartCity(longList1);
                    }
                    break;
                }

                currentOriginArea = currentOriginArea.getParent();
            }
            svi.setOriginArea(sb.toString());
        }

        //送货地址
        HbzArea currentDestArea = startVehicleInformation.getDestArea();
        LinkedList<Long> endList1=new LinkedList<>();
        if (currentDestArea !=null) {
            StringBuilder ss = new StringBuilder();
            while (currentDestArea.getLevel() > 0) {
                ss.insert(0, currentDestArea.getAreaName() + " ");
                switch (currentDestArea.getLevel().intValue()) {
                    case 1: {
                        svi.setProvinceToId(currentDestArea.getId());
                        endList1.addFirst(currentDestArea.getId());
                        svi.setEndCity(endList1);
                    }
                    break;
                    case 2: {
                        svi.setCityToId(currentDestArea.getId());
                        endList1.addFirst(currentDestArea.getId());
                        svi.setEndCity(endList1);
                    }
                    break;
                    case 3: {
                        svi.setCountyToId(currentDestArea.getId());
                        endList1.addFirst(currentDestArea.getId());
                        svi.setEndCity(endList1);
                    }
                    break;
                }

                currentDestArea = currentDestArea.getParent();
            }
            svi.setDestArea(ss.toString());
        }
        //日期转换
        if (startVehicleInformation.getReceiptDate() !=null){
            FormatedDate logIn = new FormatedDate(startVehicleInformation.getReceiptDate());
            String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
            svi.setReceiptDate(receiptDate);
        }
        if (startVehicleInformation.getReceiptToDate() !=null){
            FormatedDate logIn3 = new FormatedDate(startVehicleInformation.getReceiptToDate());
            String receiptToDate = logIn3.getFormat("yyyy-MM-dd HH:mm:ss");
            svi.setReceiptToDate(receiptToDate);
        }


         //车辆的基本信息
         if (startVehicleInformation.getVehicleInformation() !=null){

             VehicleInformationDTO  vi=new VehicleInformationDTO();
             BeanUtils.copyProperties(startVehicleInformation.getVehicleInformation(),vi);
             svi.setVehicleInformationDTO(vi);
         }
        //装车的货物列表
//        if (startVehicleInformation.getStartVehicle() !=null){
//            List<StartVehicle> listStartVehicle= listStartVehicle.getStartVehicle();
            List<StartVehicleDTO> startVehicleDTO=new ArrayList<>();
            for (int i = 0; i <listStartVehicle.size() ; i++) {
                StartVehicleDTO  sv=new StartVehicleDTO();
                StartVehicle   startVehicle= listStartVehicle.get(i);
                BeanUtils.copyProperties(startVehicle,sv);

                if (listStartVehicle.get(i).getCargoInformation() !=null){
                    sv.setCargoInformationId(listStartVehicle.get(i).getCargoInformation().getId());
                }
                if (startVehicle.getPackageUnit() !=null){
                    String packageUnitValue = userInformationRepository.findUserClassification("PackageUnit", startVehicle.getPackageUnit());
                    sv.setPackageUnitValue(packageUnitValue);
                }



                //取货地址
                HbzArea currentLevelOriginArea= startVehicle.getOriginArea();
                LinkedList<Long> longList=new LinkedList<>();
                if (currentLevelOriginArea !=null) {
                    StringBuilder sb = new StringBuilder();
                    while (currentLevelOriginArea.getLevel() > 0) {
                        sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                        switch (currentLevelOriginArea.getLevel().intValue()) {
                            case 1: {
                                sv.setProvinceId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                sv.setStartCity(longList);
                            }
                            break;
                            case 2: {
                                sv.setCityId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                sv.setStartCity(longList);
                            }
                            break;
                            case 3: {
                                sv.setCountyId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                sv.setStartCity(longList);
                            }
                            break;
                        }

                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    sv.setOriginArea(sb.toString());
                }

                //日期转换
                if (startVehicle.getReceiptDate() !=null){
                    FormatedDate logIn4 = new FormatedDate(startVehicle.getReceiptDate());
                    String receiptDate1 = logIn4.getFormat("yyyy-MM-dd HH:mm:ss");
                    sv.setReceiptDate(receiptDate1);
                }
                if (startVehicle.getReceiptToDate() !=null){
                    FormatedDate logIn5 = new FormatedDate(startVehicle.getReceiptToDate());
                    String receiptToDate2 = logIn5.getFormat("yyyy-MM-dd HH:mm:ss");
                    sv.setReceiptToDate(receiptToDate2);
                }
                String  name= userInformationRepository.findUserClassification("PackagingStatus",startVehicle.getPackagingStatus());
                sv.setPackagingStatusName(name);
                String  name2= userInformationRepository.findUserClassification("PaymentMethod",startVehicle.getPaymentMethod());
                sv.setPaymentMethodName(name2);
                if (startVehicle.getServiceMethodType() !=null){
                    sv.setServiceMethodTypeValue(startVehicle.getServiceMethodType().getName());
                }
                if (startVehicle.getShippingStatus() !=null){
                    sv.setShippingStatusValue(startVehicle.getShippingStatus().getName());
                }

                //送货地址
                HbzArea currentLevelArea = startVehicle.getDestArea();
                LinkedList<Long> endList=new LinkedList<>();
                if (currentLevelArea !=null) {
                    StringBuilder ss = new StringBuilder();
                    while (currentLevelArea.getLevel() > 0) {
                        ss.insert(0, currentLevelArea.getAreaName() + " ");
                        switch (currentLevelArea.getLevel().intValue()) {
                            case 1: {
                                sv.setProvinceToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                sv.setEndCity(endList);
                            }
                            break;
                            case 2: {
                                sv.setCityToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                sv.setEndCity(endList);
                            }
                            break;
                            case 3: {
                                sv.setCountyToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                sv.setEndCity(endList);
                            }
                            break;
                        }

                        currentLevelArea = currentLevelArea.getParent();
                    }
                    sv.setDestArea(ss.toString());
                }
                startVehicleDTO.add(sv);
            }
           svi.setStartVehicleDTOS(startVehicleDTO);
//        }
        return svi;
    }

    //在途发车中途添加发货信息
    @Override
    public Boolean inserAletelStartVehicleInformation(StartVehicleInformationDTO startVehicleInformationDTO) {

        StartVehicleInformation  startVehicleInformation=  startVehicleInformationRepository.findOne(startVehicleInformationDTO.getId());

        //通过传入的车辆id 查询所有中途旧的货物id
         List<StartVehicle> startVehicleList= startVehicleInformation.getStartVehicle();
         List<Long> oldId=new ArrayList<>();
        for (int i = 0; i <startVehicleList.size() ; i++) {
          Long  cargoInformationId=  startVehicleList.get(i).getCargoInformation().getId();
            oldId.add(cargoInformationId);
        }
        //获取页面传过来的货物id
        List<StartVehicleDTO> startVehicleDTOS=  startVehicleInformationDTO.getStartVehicleDTOS()
                .stream().filter(sv->!oldId.contains(sv.getCargoInformationId())).collect(Collectors.toList());

      /**
        List<Long> parameter =new ArrayList<>();
        for (int i = 0; i < startVehicleDTOS.size(); i++) {
           Long  cargoInformationId2 = startVehicleDTOS.get(i).getCargoInformationId();
            parameter.add(cargoInformationId2);
        }
       */

        //新增加的货物id





        if (startVehicleDTOS==null){
            startVehicleInformationRepository.save(startVehicleInformation);
            return true;
        }

        //全部运走的货物id
        List<Long>  zId=new ArrayList<>();
//        Map<Long,Integer> map2=new HashMap<>();
        //部分运走的id 和数量
        Map<Long,Integer> map=new HashMap<>();

           if (startVehicleInformationDTO.getStartVehicleDTOS() !=null){

               for (int i = 0; i < startVehicleDTOS.size(); i++) {
                   StartVehicle startVehicle=new StartVehicle();
                   StartVehicleDTO  svDto=  startVehicleDTOS.get(i);
                    //通过传过来的货物信息id 查询货物 复制到 装车货物表中
                   CargoInformation cargoInformation=cargoInformationRepository.findOne(svDto.getCargoInformationId());

                   //查出收货表中的库存
                   Integer  inventoryQuantity= cargoInformation.getInventoryQuantity();

                   //如果只是部分装车的话  要更新 收货信息表中的库存量以及货物的状态
                   //获取页面传过来的运单数量
                   Integer  waybillQuantity=startVehicleInformationDTO.getStartVehicleDTOS().get(i).getWaybillQuantity();
                   //比较两个差额
                   Integer  number=inventoryQuantity-waybillQuantity;


                   //全部运走
                   if (inventoryQuantity==waybillQuantity){
                       BeanUtils.copyProperties(cargoInformation,startVehicle,"id");
                       Long  zhengcheId=  svDto.getCargoInformationId();
                       zId.add(zhengcheId);
//                       map2.put(zhengcheId,number);
                       startVehicle.setWaybillQuantity(waybillQuantity);
                       //发车的发车编号
                       startVehicle.setStartNumber(startVehicleInformation.getStartNumber());
                       //添加时 所有的货物状态 都为 1
                       startVehicle.setStatus("1");
                       //保存货物的区域 到站----发站
                       startVehicle.setOriginArea(startVehicleInformation.getOriginArea());
                       startVehicle.setDestArea(startVehicleInformation.getDestArea());
                       //设置此批量货物为全部装车
                       startVehicle.setShippingStatus(ShippingStatus.ALL_START);
                       //是否卸货 添加时都是未卸货
                       startVehicle.setIsUnload(IsUnload.NOTUNLOADED);
                       //装车表中的发车时间
                       if (startVehicleInformation.getReceiptDate() !=null){
                           startVehicle.setReceiptDate(startVehicleInformation.getReceiptDate());
                       }
                       //设置货物的重量
                       startVehicle.setSingleWeight(waybillQuantity*cargoInformation.getSingleWeight());
                       //设置 装车的货物总体积
                       startVehicle.setSingleVolume(waybillQuantity*cargoInformation.getSingleVolume());

                       if (cargoInformation.getFeeSchedule() !=null){
                           //设置支付方式
                           startVehicle.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                       }
                       if (cargoInformation.getReceiverUser() !=null) {
                           //设置接单用户的公司名称
                           startVehicle.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                           //设置接单公司电话
                           startVehicle.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                       }
                       //设置车辆编号
                       startVehicle.setVehicleNumber(startVehicleInformation.getVehicleNumber());

                       //装车表中的 货物预计到达时间
                       if (startVehicleInformation.getReceiptToDate() !=null){
                           startVehicle.setReceiptToDate(startVehicleInformation.getReceiptToDate());
                       }
                       //装车 表中保存一个 车辆信息
                       //保存装车的  车辆信息
                       if (startVehicleInformation.getVehicleInformation() !=null && startVehicleInformation.getVehicleInformation().getId() != null) {
                           VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformation.getVehicleInformation().getId());
                           startVehicle.setVehicleInformation(vi);
                       }
                       //添加到原来的集合中 ==新增的数据
                       startVehicleInformation.getStartVehicle().add(startVehicle);
                   }

                   //部分运走
                   if (inventoryQuantity !=waybillQuantity && number>0){
                       BeanUtils.copyProperties(cargoInformation,startVehicle,"id");
                       Long  bufenId= svDto.getCargoInformationId();
                       map.put(bufenId,number);
                       startVehicle.setWaybillQuantity(waybillQuantity);
                       //发车的发车编号
                       startVehicle.setStartNumber(startVehicleInformation.getStartNumber());
                       //添加时 所有的货物状态 都为 1
                       startVehicle.setStatus("1");
                       //保存货物的区域 到站----发站
                       startVehicle.setOriginArea(startVehicleInformation.getOriginArea());
                       startVehicle.setDestArea(startVehicleInformation.getDestArea());
                       //设置此批量货物为部分装车
                       startVehicle.setShippingStatus(ShippingStatus.SECTION_START);
                       //是否卸货 添加时都是未卸货
                       startVehicle.setIsUnload(IsUnload.NOTUNLOADED);
                       //装车表中的发车时间
                       if (startVehicleInformation.getReceiptDate() !=null){
                           startVehicle.setReceiptDate(startVehicleInformation.getReceiptDate());
                       }
                       //设置货物的重量
                       startVehicle.setSingleWeight(waybillQuantity*cargoInformation.getSingleWeight());
                       //设置 装车的货物总体积
                       startVehicle.setSingleVolume(waybillQuantity*cargoInformation.getSingleVolume());

                       if (cargoInformation.getFeeSchedule() !=null){
                           //设置支付方式
                           startVehicle.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                       }
                       if (cargoInformation.getReceiverUser() !=null) {
                           //设置接单用户的公司名称
                           startVehicle.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                           //设置接单公司电话
                           startVehicle.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                       }
                       //设置车辆编号
                       startVehicle.setVehicleNumber(startVehicleInformation.getVehicleNumber());

                       //装车表中的 货物预计到达时间
                       if (startVehicleInformation.getReceiptToDate() !=null){
                           startVehicle.setReceiptToDate(startVehicleInformation.getReceiptToDate());
                       }
                       //装车 表中保存一个 车辆信息
                       //保存装车的  车辆信息
                       if (startVehicleInformation.getVehicleInformation() !=null && startVehicleInformation.getVehicleInformation().getId() != null) {
                           VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformation.getVehicleInformation().getId());
                           startVehicle.setVehicleInformation(vi);
                       }
                       startVehicleInformation.getStartVehicle().add(startVehicle);

                   }

               }
               startVehicleInformationRepository.save(startVehicleInformation);

                //修改全部装车的收货信息的状态为 全部发车
               for (int j = 0; j <zId.size() ; j++) {
                   //装车成功后 修改此货物信息的 物流状态
                   startVehicleInformationRepository.updateshippingStatus(zId.get(j), ShippingStatus.ALL_START);
                   startVehicleInformationRepository.updateInventoryQuantity(zId.get(j),0);

               }
               //修改部分装车的 收货信息状态  及库存
               for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                   startVehicleInformationRepository.updateshippingStatus(entry.getKey(), ShippingStatus.SECTION_START);
                   startVehicleInformationRepository.updateInventoryQuantity(entry.getKey(),entry.getValue());
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

               }
               return true;
           }else {
               return false;
           }

    }


    //卸货  前端传过来一个 货物的id 的 集合
//    @Override
//    public Boolean unloadProduct(List<Long>  list) {
//
//        list.stream().map(startVehicleRepository::findOne).map(a->{a.setIsUnload(IsUnload.ALLUNLOADING); return a;}).forEach(startVehicleRepository::save);

//        list.forEach(id->{
//            StartVehicle vehicle = startVehicleRepository.findOne(id);
//            vehicle.setIsUnload(IsUnload.ALLUNLOADING);
//            startVehicleRepository.save(vehicle);
//        });

//        return true;
//    }

//卸货
    public  Boolean unloadProduct(StartVehicleInformationDTO startVehicleInformationDTO){

       if (startVehicleInformationDTO.getStartVehicleDTOS() !=null && startVehicleInformationDTO.getStartVehicleDTOS().size() > 0 ){
            //修改货物状态为 卸货
           List<StartVehicleDTO> startVehicleDTOList= startVehicleInformationDTO.getStartVehicleDTOS();
           for (int i = 0; i <startVehicleDTOList.size() ; i++) {
             Long  svDTOId=  startVehicleDTOList.get(i).getId();
               startVehicleRepository.updateIsUnload(svDTOId);

           }

           StartVehicleInformation sv=startVehicleInformationRepository.findOne(startVehicleInformationDTO.getId());
           List<StartVehicle> startVehicleList= sv.getStartVehicle();
           List<IsUnload>  list=new ArrayList<>();
           int count=0;
           if (startVehicleList !=null){
               for (int i = 0; i <startVehicleList.size() ; i++) {
                   IsUnload isUnload=  startVehicleList.get(i).getIsUnload();
                   if (IsUnload.ALLUNLOADING.equals(isUnload)) {
                       count++;
                   }
               }
           }
           if(count < startVehicleList.size()) {  //未卸完
               startVehicleInformationRepository.updatepartunloading(startVehicleInformationDTO.getId());
           }else if(count == startVehicleList.size()) {  //卸货完毕
               startVehicleInformationRepository.updateShippingStatus(startVehicleInformationDTO.getId());
           }else if (count==0){  //运输中
               startVehicleInformationRepository.startCar(startVehicleInformationDTO.getId());
           }

           return  true;
       }else {
           return false;
       }

    }


    //发车单导出
    public List<StartVehicleInformationDTO> startVehicleInformationExport(StartVehicleInformationDTO startVehicleInformationDTO){

            List<StartVehicleInformation> page=startVehicleInformationRepository.findAll(new Specification<StartVehicleInformation>() {
                @Override
                public Predicate toPredicate(Root<StartVehicleInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                    List<Predicate> predicateList = new ArrayList<>();
                    if (startVehicleInformationDTO.getSmallTime() !=null){
                        predicateList.add(criteriaBuilder.ge(root.get("receiptDate"),startVehicleInformationDTO.getSmallTime()));
                    }
                    if (startVehicleInformationDTO.getBigTime() !=null){
                        predicateList.add(criteriaBuilder.le(root.get("receiptDate"),startVehicleInformationDTO.getBigTime()+59999L));
                    }
                    //发出
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getOriginAreaCode())){
                        predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"),startVehicleInformationDTO.getOriginAreaCode()));
                    }
//
                    //到站
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getDestAreaCode())){
                        predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"),startVehicleInformationDTO.getDestAreaCode()));
                    }
//

                    if (startVehicleInformationDTO.getTransitState() !=null){
                        //在图状态
                        predicateList.add(criteriaBuilder.equal(root.get("transitState"),startVehicleInformationDTO.getTransitState()));
                    }
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getStartNumber())){
                        //发车编号
                        predicateList.add(criteriaBuilder.equal(root.get("startNumber"),startVehicleInformationDTO.getStartNumber()));
                    }
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getNumberPlate())){
                        //车牌号
                        predicateList.add(criteriaBuilder.equal(root.join("vehicleInformation").get("numberPlate"),startVehicleInformationDTO.getNumberPlate()));
                    }
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getDriverName())){
                        //司机名称
                        predicateList.add(criteriaBuilder.like(root.join("vehicleInformation").get("driverName"),"%"+startVehicleInformationDTO.getDriverName()+"%"));
                    }
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getDriverTelephone())){
                        //司机电话
                        predicateList.add(criteriaBuilder.equal(root.join("vehicleInformation").get("driverTelephone"),startVehicleInformationDTO.getDriverTelephone()));
                    }
                    if (StringUtils.isNotBlank(startVehicleInformationDTO.getVehicleType())){
                        //车辆类型
                        predicateList.add(criteriaBuilder.equal(root.join("vehicleInformation").get("vehicleType"),startVehicleInformationDTO.getVehicleType()));
                    }


                    Predicate[] ps = new Predicate[predicateList.size()];
                    return criteriaBuilder.and(predicateList.toArray(ps));

                }
            });

            return exportDTO(page);
        }



        private List<StartVehicleInformationDTO> exportDTO(List<StartVehicleInformation> page) {
            List<StartVehicleInformationDTO> list=new ArrayList<>();
            for (int i = 0; i <page.size() ; i++) {
                StartVehicleInformationDTO sviDTO=new StartVehicleInformationDTO();
                StartVehicleInformation svi=  page.get(i);
                BeanUtils.copyProperties(svi,sviDTO);
                //司机信息
                if (svi.getVehicleInformation() !=null){
                    sviDTO.setNumberPlate(svi.getVehicleInformation().getNumberPlate());
                     String vehicleType= svi.getVehicleInformation().getVehicleType();
                    String  name= userInformationRepository.findUserClassification("VehicleType",vehicleType);
                    sviDTO.setVehicleType(name);
                    sviDTO.setDriverName(svi.getVehicleInformation().getDriverName());
                    sviDTO.setDriverTelephone(svi.getVehicleInformation().getDriverTelephone());
                }
                //发站城市
                HbzArea currentLevelOriginArea= svi.getOriginArea();
                StringBuilder sb = new StringBuilder();
                if (currentLevelOriginArea !=null) {
                    while (currentLevelOriginArea.getLevel() > 0) {
                        sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    sviDTO.setOriginArea(sb.toString());
                }
                //到站城市
                HbzArea currentLevelArea = svi.getDestArea();
                StringBuilder ss = new StringBuilder();
                if (currentLevelArea !=null) {
                    while (currentLevelArea.getLevel() > 0) {
                        ss.insert(0, currentLevelArea.getAreaName() + " ");
                        currentLevelArea = currentLevelArea.getParent();
                    }
                    sviDTO.setDestArea(ss.toString());
                }
                //发货日期
                if ( svi.getReceiptDate() !=null){
                    FormatedDate logIn = new FormatedDate(svi.getReceiptDate());
                    String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm");
                    sviDTO.setReceiptDate(receiptDate);
                }

                //到货时间
                if ( svi.getReceiptToDate() !=null){
                    FormatedDate logIn2 = new FormatedDate(svi.getReceiptToDate());
                    String receiptToDate = logIn2.getFormat("yyyy-MM-dd HH:mm");
                    sviDTO.setReceiptToDate(receiptToDate);
                }

                if (svi.getStartVehicle() !=null){
                List<StartVehicle> startVehicleList= svi.getStartVehicle();

                Double toalInstalledWeight=0D;
                Double toalInstalledVolume=0D;
                    for (int j = 0; j <startVehicleList.size() ; j++) {
                        toalInstalledWeight+= startVehicleList.get(j).getInstalledWeight();
                        toalInstalledVolume+= startVehicleList.get(j).getInstalledVolume();
//                        //该单子的货物数量
//                      Integer waybillQuantity=   startVehicleList.get(j).getWaybillQuantity();
//                      if (waybillQuantity==null){
//                          waybillQuantity=0;
//                      }
//                      //一个货物的重量
//                      Double  singleWeight=  startVehicleList.get(j).getSingleWeight();
//                      if (singleWeight==null){
//                          singleWeight=0D;
//                      }
//                      //一个货物的体积
//                      Double   singleVolume= startVehicleList.get(j).getSingleVolume();
//                      if (singleVolume==null){
//                          singleVolume=0D;
//                      }
                    }
                    //导出转换
                    sviDTO.setInstalledWeight(toalInstalledWeight);
                    sviDTO.setInstalledVolume(toalInstalledVolume);
                }

                if (svi.getShippingStatus() !=null){ //发车单状态
                    sviDTO.setShippingStatusValue(svi.getShippingStatus().getName());
                }
                if (svi.getTransitState() !=null){//车辆状态
                    sviDTO.setTransitStateValue(svi.getTransitState().getName());
                }

                sviDTO.setRemarks(svi.getRemarks());
                list.add(sviDTO);
            }

        return  list;
        }

    @Override
    public boolean cancleStartVehicle(StartVehicleDTO startVehicleDTO) {
        StartVehicle startVehicle = startVehicleRepository.findOne(startVehicleDTO.getId());
        CargoInformation cargoInformation = startVehicle.getCargoInformation();
        if(startVehicle.getWaybillQuantity().intValue() == startVehicleDTO.getWaybillQuantity().intValue()){
            startVehicleRepository.delete(startVehicle);
        }else{
            startVehicle.setWaybillQuantity(startVehicle.getWaybillQuantity() - startVehicleDTO.getWaybillQuantity());
            startVehicleRepository.save(startVehicle);
        }
        cargoInformation.setInventoryQuantity(cargoInformation.getInventoryQuantity() + startVehicleDTO.getWaybillQuantity());
        cargoInformationRepository.save(cargoInformation);
        if(cargoInformation.getAmount().intValue() == cargoInformation.getInventoryQuantity().intValue()){
            cargoInformation.setShippingStatus(ShippingStatus.NEW);
        }else{
            cargoInformation.setShippingStatus(ShippingStatus.SECTION_START);
        }
        cargoInformationRepository.save(cargoInformation);
        return true;
    }
}
