package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.dto.ExprotCargoInformationDTO;
import com.troy.keeper.management.dto.HbzAssignWorkDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 李奥   收货管理
 * @date 2017/12/29.
 */
public interface CargoInformationService {

    //分页
    public Page<CargoInformationDTO> findByCondition(CargoInformationDTO cargoInformationDTO, Pageable pageable);

    //不分页查询
    public List<CargoInformationDTO> notTablePage(CargoInformationDTO cargoInformationDTO);



    //新增收货管理
    public Boolean  addCargoInformation(CargoInformationDTO cargoInformationDTO);

//    //点击编辑信息
//    public CargoInformationDTO findcargoInformation(CargoInformationDTO cargoInformationDTO);

    //修改货物信息
    public  Boolean updateCargoInformation(CargoInformationDTO cargoInformationDTO);

   //删除收货信息
    public Boolean  deleteCargoInformation(CargoInformationDTO cargoInformationDTO);

    //平台指运订单分页查询
    public Page<HbzAssignWorkDTO> findHbzAssignWorkMapDTO(HbzAssignWorkDTO hbzAssignWorkDTO, Pageable pageable);

    //点击接单查询该单子的详细信息
    public HbzAssignWorkDTO findHbzAssignWorkInfomation(HbzAssignWorkDTO hbzAssignWorkDTO);

    //保存平台指定的订单
    public Boolean  saveHbzAssignWork(CargoInformationDTO cargoInformationDTO);

    //根据登陆用户获取当前登陆人的网点
    public  Long smOrgId();

    //收货导出
    public List<ExprotCargoInformationDTO>  cargoInformationExport(ExprotCargoInformationDTO exprotCargoInformationDTO);

   //新增收货托运客户接口  分页查询
   public Page<UserInformationDTO> userTable(UserInformationDTO userInformationDTO, Pageable pageable);
    //新增收货时 收货方条件的过滤
    public Page<UserInformationDTO> receivinguserTable(UserInformationDTO userInformationDTO, Pageable pageable);




}
