package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.*;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.type.RegistryCode;
import com.troy.keeper.hbz.type.RegistryProgress;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/26.
 */
@CrossOrigin
@CommonsLog
@RestController
@RequestMapping("/api/user/app/registry")
public class HbzUserRegistryResource {

    @Autowired
    private HbzPersonDriverRegistryService hbzPersonDriverRegistryService;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private HbzPersonConsignorRegistryService hbzPersonConsignorRegistryService;

    @Autowired
    HbzDeliveryBoyRegistrySerivce hbzDeliveryBoyRegistrySerivce;

    @Autowired
    HbzEnterpriseConsignorRegistryService hbzEnterpriseConsignService;

    @Autowired
    HbzTransEnterpriseRegistryService hbzTransEnterpriseRegistryService;

    @Autowired
    HbzUserRegistryService hbzUserRegistryService;

    /**
     * @param registryMapDTO
     * @return
     */
    @RequestMapping(value = {"/deliveryBoy/submit"}, method = RequestMethod.POST)
    public ResponseDTO submitDeliveryBoy(@RequestBody HbzDeliveryBoyRegistryMapDTO registryMapDTO) {
        //验证字段是否符合规范
        String[] errors = ValidationHelper.valid(registryMapDTO, "delivery_boy_registry");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", errors);
        }
        HbzUserDTO user = hbzUserService.currentUser();
        // 1、删除以前提交的该类型资质
        HbzDeliveryBoyRegistryDTO query = new HbzDeliveryBoyRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzDeliveryBoyRegistryDTO> result = hbzDeliveryBoyRegistrySerivce.query(query);
        result.forEach(hbzDeliveryBoyRegistrySerivce::delete);
        // 2、插入新的资质审核提交
        HbzDeliveryBoyRegistryDTO registry = new HbzDeliveryBoyRegistryDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::parse)).copyProperties(registryMapDTO, registry);
        registry.setUserId(user.getId());
        registry.setRegistryProgress(RegistryProgress.UN_REGISTER);
        registry.setStatus(Const.STATUS_ENABLED);
        registry = hbzDeliveryBoyRegistrySerivce.save(registry);
        if (registry != null) {
            return new ResponseDTO(Const.STATUS_OK, "资质提交成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 私人货主注册
     *
     * @param registryMapDTO
     * @return
     */
    @RequestMapping(value = {"/personConsignor/submit"}, method = RequestMethod.POST)
    public ResponseDTO submitPersonConsignor(@RequestBody HbzPersonConsignorRegistryMapDTO registryMapDTO) {
        //验证字段是否符合规范
        String[] errors = ValidationHelper.valid(registryMapDTO, "person_consignor_registry");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", errors);
        }
        HbzUserDTO user = hbzUserService.currentUser();
        // 1、删除以前提交的该类型资质
        HbzPersonConsignorRegistryDTO query = new HbzPersonConsignorRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzPersonConsignorRegistryDTO> result = hbzPersonConsignorRegistryService.query(query);
        result.forEach(hbzPersonConsignorRegistryService::delete);
        // 2、插入新的资质审核提交
        HbzPersonConsignorRegistryDTO registry = new HbzPersonConsignorRegistryDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::parse)).copyProperties(registryMapDTO, registry);
        registry.setUserId(user.getId());
        registry.setRegistryProgress(RegistryProgress.UN_REGISTER);
        registry.setStatus(Const.STATUS_ENABLED);
        registry = hbzPersonConsignorRegistryService.save(registry);
        if (registry != null) {
            return new ResponseDTO(Const.STATUS_OK, "资质提交成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 个人司机注册
     *
     * @param registryMapDTO
     * @return
     */
    @RequestMapping(value = {"/personDriver/submit"}, method = RequestMethod.POST)
    public ResponseDTO submitDriver(@RequestBody HbzPersonDriverRegistryMapDTO registryMapDTO) {
        //验证字段是否符合规范
        String[] errors = ValidationHelper.valid(registryMapDTO, "person_driver_registry_submit");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", errors);
        }
        HbzUserDTO user = hbzUserService.currentUser();
        // 1、删除资质
        HbzPersonDriverRegistryDTO query = new HbzPersonDriverRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzPersonDriverRegistryDTO> registryDTOS = hbzPersonDriverRegistryService.query(query);
        registryDTOS.forEach(hbzPersonDriverRegistryService::delete);

        //新建资质
        HbzPersonDriverRegistryDTO registry = new HbzPersonDriverRegistryDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::parse))
                .addPropMapper(new PropertyMapper<>("drivingValidity", new TimeMillisFormater("yyyy-MM-dd")::parse)).copyProperties(registryMapDTO, registry);
        registry.setStatus(Const.STATUS_ENABLED);
        registry.setRegistryProgress(RegistryProgress.UN_REGISTER);
        registry.setUserId(user.getId());
        registry = hbzPersonDriverRegistryService.save(registry);
        if (registry != null) {
            return new ResponseDTO(Const.STATUS_OK, "提交资质审核成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * @param registryMapDTO
     * @return
     */
    @RequestMapping(value = {"/enterpriseConsignor/submit"}, method = RequestMethod.POST)
    public ResponseDTO submitEntConsignor(@RequestBody HbzEnterpriseConsignorRegistryMapDTO registryMapDTO) {
        //验证字段是否符合规范
        String[] errors = ValidationHelper.valid(registryMapDTO, "enterprise_consignor_registry");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", errors);
        }
        HbzUserDTO user = hbzUserService.currentUser();
        // 1、删除资质
        HbzEnterpriseConsignorRegistryDTO query = new HbzEnterpriseConsignorRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzEnterpriseConsignorRegistryDTO> registryDTOS = hbzEnterpriseConsignService.query(query);
        registryDTOS.forEach(hbzEnterpriseConsignService::delete);

        //新建资质
        HbzEnterpriseConsignorRegistryDTO registry = new HbzEnterpriseConsignorRegistryDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::parse))
                .copyProperties(registryMapDTO, registry);
        registry.setStatus(Const.STATUS_ENABLED);
        registry.setRegistryProgress(RegistryProgress.UN_REGISTER);
        registry.setUserId(user.getId());
        registry = hbzEnterpriseConsignService.save(registry);
        if (registry != null) {
            return new ResponseDTO(Const.STATUS_OK, "提交资质审核成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * @param registryMapDTO
     * @return
     */
    @RequestMapping(value = {"/transEnterprise/submit"}, method = RequestMethod.POST)
    public ResponseDTO submitEnt(@RequestBody HbzTransEnterpriseRegistryMAPDTO registryMapDTO) {
        //验证字段是否符合规范
        String[] errors = ValidationHelper.valid(registryMapDTO, "trans_enterprise_registry");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", errors);
        }
        HbzUserDTO user = hbzUserService.currentUser();
        // 1、删除资质
        HbzTransEnterpriseRegistryDTO query = new HbzTransEnterpriseRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzTransEnterpriseRegistryDTO> registryDTOS = hbzTransEnterpriseRegistryService.query(query);
        registryDTOS.forEach(hbzTransEnterpriseRegistryService::delete);

        //新建资质
        HbzTransEnterpriseRegistryDTO registry = new HbzTransEnterpriseRegistryDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("owerCreateTime", new TimeMillisFormater("yyyy-MM-dd")::parse))
                .copyProperties(registryMapDTO, registry);
        registry.setStatus(Const.STATUS_ENABLED);
        registry.setRegistryProgress(RegistryProgress.UN_REGISTER);
        registry.setUserId(user.getId());
        registry = hbzTransEnterpriseRegistryService.save(registry);
        if (registry != null) {
            return new ResponseDTO(Const.STATUS_OK, "提交资质审核成功", null);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(value = {"/typed/registry"}, method = RequestMethod.POST)
    public ResponseDTO typeRegistry() {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzUserRegistryDTO query = new HbzUserRegistryDTO();
        query.setUserId(user.getId());
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzUserRegistryDTO> registries = hbzUserRegistryService.query(query);
        Map<RegistryCode, RegistryProgress> reginfo = new LinkedHashMap<>();
        Map<RegistryCode, HbzUserRegistryDTO> reg = new LinkedHashMap<>();
        registries.forEach(registry -> {
            reg.put(registry.getRegistryCode(), registry);
            reginfo.put(registry.getRegistryCode(), registry.getRegistryProgress());
        });

        List<Map<String, Object>> list = new ArrayList<>();

        if (reginfo.get(RegistryCode.DeliveryBoy) == null)
            reginfo.put(RegistryCode.DeliveryBoy, RegistryProgress.UN_DO);
        if (reginfo.get(RegistryCode.EnterpriseConsignor) == null)
            reginfo.put(RegistryCode.EnterpriseConsignor, RegistryProgress.UN_DO);
        if (reginfo.get(RegistryCode.PersonConsignor) == null)
            reginfo.put(RegistryCode.PersonConsignor, RegistryProgress.UN_DO);
        if (reginfo.get(RegistryCode.PersonDriver) == null)
            reginfo.put(RegistryCode.PersonDriver, RegistryProgress.UN_DO);
        if (reginfo.get(RegistryCode.TransEnterprise) == null)
            reginfo.put(RegistryCode.TransEnterprise, RegistryProgress.UN_DO);

        Map<String, Object> DeliveryBoy = new LinkedHashMap<>();
        DeliveryBoy.put("key", RegistryCode.DeliveryBoy);
        DeliveryBoy.put("name", RegistryCode.DeliveryBoy.getName());
        DeliveryBoy.put("authType", reginfo.get(RegistryCode.DeliveryBoy));
        DeliveryBoy.put("authTime", reg.get(RegistryCode.DeliveryBoy) == null ? null :
                new TimeMillisFormater("yyyy-MM-dd HH:mm").format(reg.get(RegistryCode.DeliveryBoy).getCreatedDate()));
        list.add(DeliveryBoy);


        Map<String, Object> EnterpriseConsignor = new LinkedHashMap<>();
        EnterpriseConsignor.put("key", RegistryCode.EnterpriseConsignor);
        EnterpriseConsignor.put("name", RegistryCode.EnterpriseConsignor.getName());
        EnterpriseConsignor.put("authType", reginfo.get(RegistryCode.EnterpriseConsignor));
        EnterpriseConsignor.put("authTime", reg.get(RegistryCode.EnterpriseConsignor) == null ? null :
                new TimeMillisFormater("yyyy-MM-dd HH:mm").format(reg.get(RegistryCode.EnterpriseConsignor).getCreatedDate()));
        list.add(EnterpriseConsignor);

        Map<String, Object> PersonConsignor = new LinkedHashMap<>();
        PersonConsignor.put("key", RegistryCode.PersonConsignor);
        PersonConsignor.put("name", RegistryCode.PersonConsignor.getName());
        PersonConsignor.put("authType", reginfo.get(RegistryCode.PersonConsignor));
        PersonConsignor.put("authTime", reg.get(RegistryCode.PersonConsignor) == null ? null :
                new TimeMillisFormater("yyyy-MM-dd HH:mm").format(reg.get(RegistryCode.PersonConsignor).getCreatedDate()));
        list.add(PersonConsignor);


        Map<String, Object> PersonDriver = new LinkedHashMap<>();
        PersonDriver.put("key", RegistryCode.PersonDriver);
        PersonDriver.put("name", RegistryCode.PersonDriver.getName());
        PersonDriver.put("authType", reginfo.get(RegistryCode.PersonDriver));
        PersonDriver.put("authTime", reg.get(RegistryCode.PersonDriver) == null ? null :
                new TimeMillisFormater("yyyy-MM-dd HH:mm").format(reg.get(RegistryCode.PersonDriver).getCreatedDate()));
        list.add(PersonDriver);

        Map<String, Object> TransEnterprise = new LinkedHashMap<>();
        TransEnterprise.put("key", RegistryCode.TransEnterprise);
        TransEnterprise.put("name", RegistryCode.TransEnterprise.getName());
        TransEnterprise.put("authType", reginfo.get(RegistryCode.TransEnterprise));
        TransEnterprise.put("authTime", reg.get(RegistryCode.TransEnterprise) == null ? null :
                new TimeMillisFormater("yyyy-MM-dd HH:mm").format(reg.get(RegistryCode.TransEnterprise).getCreatedDate()));
        list.add(TransEnterprise);

        return new ResponseDTO(Const.STATUS_OK, "注册信息", list);
    }


    /**
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public ResponseDTO registry(@RequestBody HbzUserRegistryDTO registryDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (registryDTO.getRegistryCode() != null) {
            switch (registryDTO.getRegistryCode()) {
                case DeliveryBoy: {
                    HbzDeliveryBoyRegistryDTO registry = hbzDeliveryBoyRegistrySerivce.findHbzDeliveryBoyRegistryByUser(user, false);
                    if (registry != null)
                        return new ResponseDTO(Const.STATUS_OK, "信息", MapSpec.mapA(registry));
                    break;
                }
                case PersonDriver: {
                    HbzPersonDriverRegistryDTO registry = hbzPersonDriverRegistryService.find(user, false);
                    if (registry != null)
                        return new ResponseDTO(Const.STATUS_OK, "信息", MapSpec.mapPersonDriverRegistry(registry));
                    break;
                }
                case PersonConsignor: {
                    HbzPersonConsignorRegistryDTO registry = hbzPersonConsignorRegistryService.findUser(user, false);
                    if (registry != null)
                        return new ResponseDTO(Const.STATUS_OK, "信息", MapSpec.mapPersonConsignor(registry));
                    break;
                }
                case TransEnterprise: {
                    HbzTransEnterpriseRegistryDTO registry = hbzTransEnterpriseRegistryService.findTransEnterpriseRegistry(user, false);
                    if (registry != null)
                        return new ResponseDTO(Const.STATUS_OK, "信息", MapSpec.mapTransEnterprise(registry));
                    break;
                }
                case EnterpriseConsignor: {
                    HbzEnterpriseConsignorRegistryDTO registry = hbzEnterpriseConsignService.find(user, false);
                    if (registry != null)
                        return new ResponseDTO(Const.STATUS_OK, "信息", MapSpec.mapEnterpriseConsignor(registry));
                    break;
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "未找到信息");
    }

}
