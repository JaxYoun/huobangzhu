package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.po.CargoInformation;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.StartVehicle;
import com.troy.keeper.hbz.po.VehicleInformation;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.repository.*;
import com.troy.keeper.management.service.StartVehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/3.
 */
@Service
@Transactional
public class StartVehicleServiceImpl implements StartVehicleService {

    @Autowired
    private StartVehicleRepository startVehicleRepository;

    //数据字典name 值
    @Autowired
    private UserInformationRepository userInformationRepository;

    //修改收货库存  及状态
    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    //保存区域
    @Autowired
    private HbzAreasRepository hbzAreaRepository;








    //查询车辆信息中所有车辆的 车辆编号
    @Override
    public List<StartVehicleDTO> findVehicleNumber() {
        List<StartVehicleDTO> st=new ArrayList<>();

        List<String>   list=  startVehicleRepository.findVehicleNumber();
            if (list !=null){
                for (int i = 0; i <list.size(); i++) {
                    StartVehicleDTO svDTO=new StartVehicleDTO();
                    String vehicleNumber= list.get(i);
                     svDTO.setVehicleNumber(vehicleNumber);
                    st.add(svDTO);
                }
                return st;
            }else {
//                svDTO.setVehicleNumber("");
                return st;

        }
    }


    //根据车辆编号查询车辆信息
    @Override
    public VehicleInformationDTO finddVehicleInformation(VehicleInformationDTO vehicleInformationDTO) {
        if (vehicleInformationDTO.getVehicleNumber() !=null){
            VehicleInformationDTO  VI=new VehicleInformationDTO();
            VehicleInformation vehicleInformation= startVehicleRepository.findVehicleInformation(vehicleInformationDTO.getVehicleNumber());

            BeanUtils.copyProperties(vehicleInformation,VI);
            return VI;
        }


        return null;
    }




//    //批量添加  全部装车
//    @Override
//    public Boolean addBatchStartVehicle(List<StartVehicleDTO> startVehicleDTO) {
//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
//        String  str=sdf.format(d);
//        String  a= str.replace("-","").replace(" ","").replace(":","");
//
//      if (startVehicleDTO !=null) {
//
//          for (int i = 0; i < startVehicleDTO.size(); i++) {
//              StartVehicleDTO startVehicleDTO1 = startVehicleDTO.get(i);
//
//              Long receiptDate= Long.valueOf(startVehicleDTO1.getReceiptDate());
//
//              StartVehicle startVehicle = new StartVehicle();
//              BeanUtils.copyProperties(startVehicleDTO1, startVehicle);
//
//
//              //保存发站的区域id
//              if (startVehicleDTO1.getOriginAreaId() !=null){
//                  HbzArea  hbzArea= hbzAreaRepository.findOne(startVehicleDTO1.getOriginAreaId());
//                  startVehicle.setOriginArea(hbzArea);
//              }
//              //保存到站的区域id
//              if (startVehicleDTO1.getDestAreaId() !=null){
//                  HbzArea  hbzArea= hbzAreaRepository.findOne(startVehicleDTO1.getDestAreaId());
//                  startVehicle.setDestArea(hbzArea);
//              }
//              //保存此货物的车辆信息
//              if (startVehicleDTO1.getVehicleInformationId() !=null){
//                  VehicleInformation vehicleInformation= startVehicleRepository.findVehicleInformationDate(startVehicleDTO1.getVehicleInformationId());
//                  startVehicle.setVehicleInformation(vehicleInformation);
//              }
//              //发车的发车编号
//              startVehicle.setStartNumber(a);
//              //在途状态
////              startVehicle.setTransitState(TransitState.NORMAL);
//              startVehicle.setStatus("1");
//
//
//
//
//
//
//              //是否卸货  1--代表是  0----代表否
////              startVehicle.setCargoStatus("0");
//              startVehicle.setReceiptDate(receiptDate);
//              //设置此批量货物为全部装车
//              startVehicle.setShippingStatus(ShippingStatus.ALL_START);
//
//              startVehicleRepository.save(startVehicle);
//
//              //装车成功后 修改此货物信息的 物流状态
//              startVehicleRepository.updateshippingStatus(startVehicleDTO1.getCargoInformationId(), ShippingStatus.ALL_START);
//          }
//          return true;
//      }else {
//          return false;
//
//      }
//
//
//    }
//
//
//    //部分货物信息 装车
//    @Override
//    public Boolean addAleteStartVehicle(StartVehicleDTO startVehicleDTO) {
//
//        StartVehicle sv=new StartVehicle();
//        BeanUtils.copyProperties(startVehicleDTO,sv);
//        //是否卸货  1--代表是  0----代表否
////        sv.setCargoStatus("0");
//
//        //重量
//        sv.setSingleWeight(startVehicleDTO.getWaybillQuantity()*startVehicleDTO.getSingleWeight());
//         sv.setReceiptDate(Long.valueOf(startVehicleDTO.getReceiptDate()));
//
//       //保存发站的区域id
//        if (startVehicleDTO.getOriginAreaId() !=null){
//            HbzArea  hbzArea= hbzAreaRepository.findOne(startVehicleDTO.getOriginAreaId());
//            sv.setOriginArea(hbzArea);
//        }
//        //保存到站的区域id
//        if (startVehicleDTO.getDestAreaId() !=null){
//            HbzArea  hbzArea= hbzAreaRepository.findOne(startVehicleDTO.getDestAreaId());
//            sv.setDestArea(hbzArea);
//        }
//       //保存此货物的车辆信息
//        if (startVehicleDTO.getVehicleInformationId() !=null){
//            VehicleInformation vehicleInformation= startVehicleRepository.findVehicleInformationDate(startVehicleDTO.getVehicleInformationId());
//            sv.setVehicleInformation(vehicleInformation);
//        }
//
//        sv.setStatus("1");
//
//
//        //在途状态
////        sv.setTransitState(TransitState.NORMAL);
//        //更新收货信息表中的库存数量
//        CargoInformation cargoInformation=cargoInformationRepository.findOne(startVehicleDTO.getCargoInformationId());
//          //查出收货表中的库存
//          Integer  InventoryQuantity= cargoInformation.getInventoryQuantity();
//          //获取到前端传过来的运单数量
//         Integer  waybillQuantity=startVehicleDTO.getWaybillQuantity();
//         //当传过来的运单数量和收货表中的库存量 有差值时 更新收货表中的库存量为差值并且更新货物状态为部分装车
//        Integer value=InventoryQuantity-waybillQuantity;
//        if (value==0){
//            //更新收货信息 的库存量
//            startVehicleRepository.updateInventoryQuantity(startVehicleDTO.getCargoInformationId(),value);
//            //更新货物的运输状态
//            startVehicleRepository.updateshippingStatus(startVehicleDTO.getCargoInformationId(), ShippingStatus.ALL_START);
//            sv.setShippingStatus(ShippingStatus.ALL_START);
//        }else if (value>0){
//            //更新收货信息 的库存量
//            startVehicleRepository.updateInventoryQuantity(startVehicleDTO.getCargoInformationId(),value);
//            //更新货物的运输状态
//            startVehicleRepository.updateshippingStatus(startVehicleDTO.getCargoInformationId(), ShippingStatus.SECTION_START);
//            sv.setShippingStatus(ShippingStatus.SECTION_START);
//        }else {
//            return false;
//        }
//
//        startVehicleRepository.save(sv);
//
//
//        return true;
//    }




}
