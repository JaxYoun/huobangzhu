package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.IsUnload;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.dto.StartVehicleInformationDTO;
import com.troy.keeper.management.dto.VehicleInformationDTO;
import com.troy.keeper.management.repository.*;
import com.troy.keeper.management.service.StartVehicleInformationService;
import com.troy.keeper.system.domain.SmPostUser;
import com.troy.keeper.system.domain.SmUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 李奥
 * @date 2018/1/10.
 */

@Service
@Transactional
public class StartVehicleInformationServiceImpl  implements StartVehicleInformationService {

    @Autowired
    private StartVehicleInformationRepository startVehicleInformationRepository;

    @Autowired
    private StartVehicleRepository startVehicleRepository;



    //保存区域
    @Autowired
    private HbzAreasRepository hbzAreaRepository;

    @Autowired
    private VehicleInformationRepository vehicleInformationRepository;

    //修改收货库存  及状态
    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    //数据字典name 值
    @Autowired
    private UserInformationRepository userInformationRepository;




    //查询车辆信息中所有车辆的 车辆编号
    @Override
    public List<VehicleInformationDTO> findVehicleNumber() {
        List<VehicleInformationDTO> viDTO =new ArrayList<>();
        List<VehicleInformation>  list=  startVehicleInformationRepository.findVehicleNumber();
        for (int i = 0; i <list.size(); i++) {
            VehicleInformationDTO vi=new VehicleInformationDTO();

            VehicleInformation  vehicleInformation= list.get(i);

            BeanUtils.copyProperties(vehicleInformation,vi);

            viDTO.add(vi);

        }
        return viDTO;
    }







    //批量添加 货物信息装车
    @Override
    public Boolean addBatchStartVehicle(StartVehicleInformationDTO startVehicleInformationDTO) {


        StartVehicleInformation svi = new StartVehicleInformation();
        BeanUtils.copyProperties(startVehicleInformationDTO, svi);
        svi.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
        svi.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
        svi.setTransitState(TransitState.NORMAL);
        svi.setShippingStatus(ShippingStatus.NEW);
        //增加组织机构id；
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        for (int i = 0; i <list1.size() ; i++) {
            Long   orgId= list1.get(i).getSmPost().getOrgId();
        //新增的时候 获取登陆用户的机构id
        svi.setSmOrgId(orgId);
        }
//        svi.setRemarks(s);

        //发车编码
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String str = sdf.format(d);
        String a = str.replace("-", "").replace(" ", "").replace(":", "");
        svi.setStartNumber(a);

        //保存发站的区域id  保存装车信息中的区域
        if (startVehicleInformationDTO.getOriginAreaCode() != null) {
            HbzArea   hbzArea = hbzAreaRepository.findByOutCode(startVehicleInformationDTO.getOriginAreaCode());
            svi.setOriginArea(hbzArea);
        }
        //保存到站的区域id
        if (startVehicleInformationDTO.getDestAreaCode() != null) {

            HbzArea  destArea = hbzAreaRepository.findByOutCode(startVehicleInformationDTO.getDestAreaCode());
            svi.setDestArea(destArea);

        }
        //全部运走的货物id
        List<Long>  zId=new ArrayList<>();
        //部分运走的id 和数量
        Map<Long,Integer> map=new HashMap<>();

        //保存货物信息
        if (startVehicleInformationDTO.getStartVehicleDTOS() != null) {

            List<StartVehicle> listStartVehicle = new ArrayList<>();

            //获取货物信息 的集合数据
            List<StartVehicleDTO> listStartVehicleDTO = startVehicleInformationDTO.getStartVehicleDTOS();
            for (int i = 0; i < listStartVehicleDTO.size(); i++) {
                StartVehicle sv = new StartVehicle();
                CargoInformation cargoInformation = cargoInformationRepository.findOne(listStartVehicleDTO.get(i).getCargoInformationId());
                //查出收货表中的库存
                if (cargoInformation != null) {


                    Integer inventoryQuantity = cargoInformation.getInventoryQuantity();

                    //如果只是部分装车的话  要更新 收货信息表中的库存量以及货物的状态
                    //获取页面传过来的运单数量
                    Integer waybillQuantity = startVehicleInformationDTO.getStartVehicleDTOS().get(i).getWaybillQuantity();
                    //比较两个差额
                    Integer number = inventoryQuantity - waybillQuantity;
                    //全部运走
                    if (inventoryQuantity.equals(waybillQuantity)) {
                        BeanUtils.copyProperties(cargoInformation, sv, "id");
                        Long zhengcheId = listStartVehicleDTO.get(i).getCargoInformationId();
                        zId.add(zhengcheId);
                        sv.setWaybillQuantity(waybillQuantity);
                        sv.setCargoInformation(cargoInformation);
                        //货物机构id
                        sv.setSmOrgId(cargoInformation.getSmOrgId());
                        //货物编码
                        sv.setCommodityNumber(cargoInformation.getCommodityNumber());
                        ///////////////////////////总运费
                        if (cargoInformation.getFeeSchedule() !=null){
                            sv.setFotalFee(cargoInformation.getFeeSchedule().getFotalFee());
                        }
                        ///////////////////////////总数
                        if (cargoInformation.getAmount() !=null){
                            sv.setAmount(cargoInformation.getAmount());
                        }
                        //发车的发车编号
                        sv.setStartNumber(a);
                        //在途状态
                        sv.setTransitState(TransitState.NORMAL);
                        //添加时 所有的货物状态 都为 1
                        sv.setStatus("1");
                        //保存货物的区域 到站----发站
                        sv.setOriginArea(cargoInformation.getOriginArea());
                        sv.setDestArea(cargoInformation.getDestArea());
                        //设置此批量货物为全部装车
                        sv.setShippingStatus(ShippingStatus.ALL_START);
                        //是否卸货 添加时都是未卸货
                        sv.setIsUnload(IsUnload.NOTUNLOADED);
                        //装车表中的发车时间
                        if (startVehicleInformationDTO.getReceiptDate() != null) {
                            sv.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
                        }
                        ////////////////////////////////////////////////////////
                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setSingleWeight(cargoInformation.getSingleWeight());
                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        sv.setSingleVolume(cargoInformation.getSingleVolume());

                        sv.setInstalledWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setInstalledVolume(waybillQuantity * cargoInformation.getSingleVolume());
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        if (cargoInformation.getFeeSchedule() != null) {
                            //设置支付方式
                            sv.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        }
                        if (cargoInformation.getReceiverUser() != null) {
                            //设置接单用户的公司名称
                            sv.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                            //设置接单公司电话
                            sv.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                        }
                        //设置车辆编号
                        sv.setVehicleNumber(startVehicleInformationDTO.getVehicleNumber());

                        //装车表中的 货物预计到达时间
                        if (startVehicleInformationDTO.getReceiptToDate() != null) {
                            sv.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
                        }
                        //装车 表中保存一个 车辆信息
                        //保存装车的  车辆信息
                        if (startVehicleInformationDTO.getVehicleInformationId() != null) {
                            VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformationDTO.getVehicleInformationId());
                            sv.setVehicleInformation(vi);
                        }
                        listStartVehicle.add(sv);
                    }

                    //部分运走
                    if (!inventoryQuantity.equals(waybillQuantity )&& number > 0){
                        BeanUtils.copyProperties(cargoInformation, sv, "id");
                        Long bufenId = listStartVehicleDTO.get(i).getCargoInformationId();
                        map.put(bufenId, number);
                        sv.setWaybillQuantity(waybillQuantity);
                        sv.setCargoInformation(cargoInformation);
                        //货物编码
                        sv.setCommodityNumber(cargoInformation.getCommodityNumber());
                        //发车的发车编号
                        sv.setStartNumber(a);
                        //在途状态
                        sv.setTransitState(TransitState.NORMAL);
                        //添加时 所有的货物状态 都为 1
                        sv.setStatus("1");
                        //货物机构id
                        sv.setSmOrgId(cargoInformation.getSmOrgId());
                        ///////////////////////////总运费
                        if (cargoInformation.getFeeSchedule() !=null){
                            sv.setFotalFee(cargoInformation.getFeeSchedule().getFotalFee());
                        }
                        ///////////////////////////总数
                        if (cargoInformation.getAmount() !=null){
                            sv.setAmount(cargoInformation.getAmount());
                        }
                        //保存货物的区域 到站----发站
                        sv.setOriginArea(cargoInformation.getOriginArea());
                        sv.setDestArea(cargoInformation.getDestArea());
                        //设置此批量货物为部分装车
                        sv.setShippingStatus(ShippingStatus.SECTION_START);
                        //是否卸货 添加时都是未卸货
                        sv.setIsUnload(IsUnload.NOTUNLOADED);
                        //装车表中的发车时间
                        if (startVehicleInformationDTO.getReceiptDate() != null) {
                            sv.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
                        }
//                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
//                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        ////////////////////////////////////////////////////////////////////////////
                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setSingleWeight(cargoInformation.getSingleWeight());
                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        sv.setSingleVolume(cargoInformation.getSingleVolume());

                        sv.setInstalledWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setInstalledVolume(waybillQuantity * cargoInformation.getSingleVolume());
////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        if (cargoInformation.getFeeSchedule() != null) {
                            //设置支付方式
                            sv.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        }
                        if (cargoInformation.getReceiverUser() != null) {
                            //设置接单用户的公司名称
                            sv.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                            //设置接单公司电话
                            sv.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                        }
                        //设置车辆编号
                        sv.setVehicleNumber(startVehicleInformationDTO.getVehicleNumber());

                        //装车表中的 货物预计到达时间
                        if (startVehicleInformationDTO.getReceiptToDate() != null) {
                            sv.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
                        }
                        //装车 表中保存一个 车辆信息
                        //保存装车的  车辆信息
                        if (startVehicleInformationDTO.getVehicleInformationId() != null) {
                            VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformationDTO.getVehicleInformationId());
                            sv.setVehicleInformation(vi);
                        }
                        listStartVehicle.add(sv);
                    }
                    //当页面的运输值大于 库存量时就保存
                    if (inventoryQuantity.intValue() < waybillQuantity.intValue()) {

                        return false;
                    }

                }

            }
            svi.setStartVehicle(listStartVehicle);
        }

        //保存装车的  车辆信息
        if (startVehicleInformationDTO.getVehicleInformationId() != null) {
            VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformationDTO.getVehicleInformationId());
            svi.setVehicleInformation(vi);
        }
        startVehicleInformationRepository.save(svi);

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
    }

    //发车单状态为新建时  编辑
    public Boolean addNewBatchStartVehicle(StartVehicleInformationDTO startVehicleInformationDTO){

        //全部运走的货物id
        List<Long>  zId=new ArrayList<>();
        //部分运走的id 和数量
        Map<Long,Integer> map=new HashMap<>();

        StartVehicleInformation svi= startVehicleInformationRepository.findOne(startVehicleInformationDTO.getId());

         List<StartVehicle> startVehicleList= svi.getStartVehicle();
        for (int i = 0; i < startVehicleList.size(); i++) {
            Long  startVehicleId=   startVehicleList.get(i).getId();
//            Integer  InventoryQuantity=  startVehicleList.get(i).getCargoInformation().getInventoryQuantity();
            Long   cargoInformationId=  startVehicleList.get(i).getCargoInformation().getId();
            Integer  InventoryQuantity= cargoInformationRepository.getCount(cargoInformationId);

            Integer WaybillQuantity= startVehicleList.get(i).getWaybillQuantity();
            cargoInformationRepository.updateInventoryQuantity(startVehicleList.get(i).getCargoInformation().getId(),InventoryQuantity+WaybillQuantity);
            startVehicleRepository.delete(startVehicleId);
        }

//        BeanUtils.copyProperties(startVehicleInformationDTO,svi);

        svi.setVehicleNumber(startVehicleInformationDTO.getVehicleNumber());
        if (startVehicleInformationDTO.getReceiptDate() !=null){
            svi.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
        }
        if (startVehicleInformationDTO.getReceiptToDate() !=null){
            svi.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
        }
        //保存发站的区域id  保存装车信息中的区域
        if (startVehicleInformationDTO.getOriginAreaCode() != null) {
            HbzArea   hbzArea = hbzAreaRepository.findByOutCode(startVehicleInformationDTO.getOriginAreaCode());
            svi.setOriginArea(hbzArea);
        }
        //保存到站的区域id
        if (startVehicleInformationDTO.getDestAreaCode() != null) {
            HbzArea  destArea = hbzAreaRepository.findByOutCode(startVehicleInformationDTO.getDestAreaCode());
            svi.setDestArea(destArea);
        }
        svi.setShippingStatus(ShippingStatus.NEW);
        //备注
        svi.setRemarks(startVehicleInformationDTO.getRemarks());
        //发车编号
        svi.setStartNumber(svi.getStartNumber());
        //车辆编号
        svi.setVehicleNumber(startVehicleInformationDTO.getVehicleNumber());
        svi.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
        svi.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
        svi.setTransitState(TransitState.NORMAL);


        //保存货物信息
        if (startVehicleInformationDTO.getStartVehicleDTOS() != null) {

            List<StartVehicle> listStartVehicle = new ArrayList<>();

            //获取货物信息 的集合数据
            List<StartVehicleDTO> listStartVehicleDTO = startVehicleInformationDTO.getStartVehicleDTOS();
            for (int i = 0; i < listStartVehicleDTO.size(); i++) {
                StartVehicle sv = new StartVehicle();
                CargoInformation cargoInformation = cargoInformationRepository.findOne(listStartVehicleDTO.get(i).getCargoInformationId());
                //查出收货表中的库存
                if (cargoInformation != null) {


                    Integer inventoryQuantity = cargoInformation.getInventoryQuantity();

                    //如果只是部分装车的话  要更新 收货信息表中的库存量以及货物的状态
                    //获取页面传过来的运单数量
                    Integer waybillQuantity = startVehicleInformationDTO.getStartVehicleDTOS().get(i).getWaybillQuantity();
                    //比较两个差额
                    Integer number = inventoryQuantity - waybillQuantity;
                    //全部运走
                    if (inventoryQuantity == waybillQuantity) {
                        BeanUtils.copyProperties(cargoInformation, sv, "id");
                        Long zhengcheId = listStartVehicleDTO.get(i).getCargoInformationId();
                        zId.add(zhengcheId);
                        sv.setWaybillQuantity(waybillQuantity);
                        sv.setCargoInformation(cargoInformation);
                        //货物编码
                        sv.setCommodityNumber(cargoInformation.getCommodityNumber());
                        //货物机构id
                        sv.setSmOrgId(cargoInformation.getSmOrgId());
                        ///////////////////////////总运费
                        if (cargoInformation.getFeeSchedule() !=null){
                            sv.setFotalFee(cargoInformation.getFeeSchedule().getFotalFee());
                        }
                        ///////////////////////////总数
                        if (cargoInformation.getAmount() !=null){
                            sv.setAmount(cargoInformation.getAmount());
                        }
                        //发车的发车编号
                        sv.setStartNumber(startVehicleInformationDTO.getStartNumber());
                        //在途状态
                        sv.setTransitState(TransitState.NORMAL);
                        //添加时 所有的货物状态 都为 1
                        sv.setStatus("1");
                        //保存货物的区域 到站----发站
                        sv.setOriginArea(cargoInformation.getOriginArea());
                        sv.setDestArea(cargoInformation.getDestArea());
                        //设置此批量货物为全部装车
                        sv.setShippingStatus(ShippingStatus.ALL_START);
                        //是否卸货 添加时都是未卸货
                        sv.setIsUnload(IsUnload.NOTUNLOADED);
                        //装车表中的发车时间
                        if (startVehicleInformationDTO.getReceiptDate() != null) {
                            sv.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
                        }
//                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
//                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        ////////////////////////////////////////////////////////////////////////////
                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setSingleWeight(cargoInformation.getSingleWeight());
                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        sv.setSingleVolume(cargoInformation.getSingleVolume());

                        sv.setInstalledWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setInstalledVolume(waybillQuantity * cargoInformation.getSingleVolume());
////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        if (cargoInformation.getFeeSchedule() != null) {
                            //设置支付方式
                            sv.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        }
                        if (cargoInformation.getReceiverUser() != null) {
                            //设置接单用户的公司名称
                            sv.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                            //设置接单公司电话
                            sv.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                        }
                        //设置车辆编号
                        sv.setVehicleNumber(startVehicleInformationDTO.getVehicleNumber());

                        //装车表中的 货物预计到达时间
                        if (startVehicleInformationDTO.getReceiptToDate() != null) {
                            sv.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
                        }
                        //装车 表中保存一个 车辆信息
                        //保存装车的  车辆信息
                        if (startVehicleInformationDTO.getVehicleInformationId() != null) {
                            VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformationDTO.getVehicleInformationId());
                            sv.setVehicleInformation(vi);
                        }
                        listStartVehicle.add(sv);
                    }

                    //部分运走
                    if (inventoryQuantity != waybillQuantity && number > 0) {
                        BeanUtils.copyProperties(cargoInformation, sv, "id");
                        Long bufenId = listStartVehicleDTO.get(i).getCargoInformationId();
                        map.put(bufenId, number);
                        sv.setWaybillQuantity(waybillQuantity);
                        sv.setCargoInformation(cargoInformation);
                        //发车的发车编号
                        sv.setStartNumber(startVehicleInformationDTO.getStartNumber());
                        //在途状态
                        sv.setTransitState(TransitState.NORMAL);
                        //添加时 所有的货物状态 都为 1
                        sv.setStatus("1");
                        //货物机构id
                        sv.setSmOrgId(cargoInformation.getSmOrgId());
                        ///////////////////////////总运费
                        if (cargoInformation.getFeeSchedule() !=null){
                            sv.setFotalFee(cargoInformation.getFeeSchedule().getFotalFee());
                        }
                        ///////////////////////////总数
                        if (cargoInformation.getAmount() !=null){
                            sv.setAmount(cargoInformation.getAmount());
                        }
                        //保存货物的区域 到站----发站
                        sv.setOriginArea(cargoInformation.getOriginArea());
                        sv.setDestArea(cargoInformation.getDestArea());
                        //设置此批量货物为部分装车
                        sv.setShippingStatus(ShippingStatus.SECTION_START);
                        //是否卸货 添加时都是未卸货
                        sv.setIsUnload(IsUnload.NOTUNLOADED);
                        //装车表中的发车时间
                        if (startVehicleInformationDTO.getReceiptDate() != null) {
                            sv.setReceiptDate(Long.valueOf(startVehicleInformationDTO.getReceiptDate()));
                        }
//                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
//                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        ////////////////////////////////////////////////////////////////////////////
                        //设置货物的重量
//                        sv.setSingleWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setSingleWeight(cargoInformation.getSingleWeight());
                        //设置 装车的货物总体积
//                        sv.setSingleVolume(waybillQuantity * cargoInformation.getSingleVolume());
                        sv.setSingleVolume(cargoInformation.getSingleVolume());

                        sv.setInstalledWeight(waybillQuantity * cargoInformation.getSingleWeight());
                        sv.setInstalledVolume(waybillQuantity * cargoInformation.getSingleVolume());
////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        if (cargoInformation.getFeeSchedule() != null) {
                            //设置支付方式
                            sv.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        }
                        if (cargoInformation.getReceiverUser() != null) {
                            //设置接单用户的公司名称
                            sv.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                            //设置接单公司电话
                            sv.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                        }
                        //设置车辆编号
                        sv.setVehicleNumber(startVehicleInformationDTO.getVehicleNumber());

                        //装车表中的 货物预计到达时间
                        if (startVehicleInformationDTO.getReceiptToDate() != null) {
                            sv.setReceiptToDate(Long.valueOf(startVehicleInformationDTO.getReceiptToDate()));
                        }
                        //装车 表中保存一个 车辆信息
                        //保存装车的  车辆信息
                        if (startVehicleInformationDTO.getVehicleInformationId() != null) {
                            VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformationDTO.getVehicleInformationId());
                            sv.setVehicleInformation(vi);
                        }
                        listStartVehicle.add(sv);
                    }
                    //当页面的运输值大于 库存量时就保存
                    if (inventoryQuantity < waybillQuantity) {

                        return false;
                    }

                }

            }
            svi.setStartVehicle(listStartVehicle);
        }

        //保存装车的  车辆信息
        if (startVehicleInformationDTO.getVehicleInformationId() != null) {
            VehicleInformation vi = vehicleInformationRepository.findOne(startVehicleInformationDTO.getVehicleInformationId());
            svi.setVehicleInformation(vi);
        }

        startVehicleInformationRepository.save(svi);

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






        return  true;
    }








    //发车单确认
    @Override
    public Boolean startCar(StartVehicleInformationDTO startVehicleInformationDTO) {
        if (startVehicleInformationDTO.getId() !=null){
            startVehicleInformationRepository.startCar(startVehicleInformationDTO.getId());
            return true;
        }else {
            return  false;
        }
    }

    //删除新建的车辆信息
    public Boolean deleteCar( StartVehicleInformationDTO startVehicleInformationDTO){
        if (startVehicleInformationDTO.getId() !=null){
            StartVehicleInformation svi=startVehicleInformationRepository.findOne(startVehicleInformationDTO.getId());
            List<StartVehicle> sv= svi.getStartVehicle();
            if (sv !=null) {

                for (int i = 0; i <sv.size(); i++) {
                    StartVehicle startVehicle= sv.get(i);
                    //获取运单的数量
                    Integer waybillQuantity= startVehicle.getWaybillQuantity();

                    Long cargoInformationId=startVehicle.getCargoInformation().getId();
                    //通过收货的id查询收货表中库存量和收货的总数量 并更新货物的运输状态
                    CargoInformation cif= cargoInformationRepository.findOne(cargoInformationId);
                    Integer amount= cif.getAmount();
                    Integer inventoryQuantity= cif.getInventoryQuantity();
                    //还原收货表中的 库存量级车辆运输状态
                    Integer allAcount=inventoryQuantity+waybillQuantity;

                    ShippingStatus shippingStatus=ShippingStatus.NEW;
                    if (allAcount==amount){
                        shippingStatus=ShippingStatus.NEW;
                    }else if (allAcount<amount){
                        shippingStatus=ShippingStatus.SECTION_START;
                    }
                    //更新收货表中的数据
                    cargoInformationRepository.inventoryQuantityShippingStatus(cargoInformationId,allAcount,shippingStatus);
                    startVehicleRepository.delete(startVehicle.getId());

                }

            }else {
                startVehicleInformationRepository.delete(startVehicleInformationDTO.getId());
            }
            startVehicleInformationRepository.delete(startVehicleInformationDTO.getId());

            return true;
        }else {

            return  false;
        }



    }





}
