package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.Stacks;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.po.HbzWarehouseAudit;
import com.troy.keeper.hbz.po.Warehouse;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.repository.WarehouseEarnestOrderRepository;
import com.troy.keeper.hbz.repository.WarehouseRepository;
import com.troy.keeper.hbz.service.impl.HbzAreaServiceImpl;
import com.troy.keeper.management.dto.WarehouseAuditDTO;
import com.troy.keeper.management.dto.WarehouseEarnestOrderDTO;
import com.troy.keeper.management.dto.WarehouseManageDTO;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.repository.WarehouseAuditRepository;
import com.troy.keeper.management.service.WarehouseAuditService;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.repository.SmUserRepository;
import com.troy.keeper.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 仓储审核ServiceImpl
 * @Date: Created in 11:59  2018/1/9.
 * @Midified By:
 */
@Service
@Transactional
public class WarehouseAuditServiceImpl implements WarehouseAuditService {

    @Autowired
    private WarehouseAuditRepository warehouseAuditRepository;

    @Autowired
    private WarehouseEarnestOrderRepository warehouseEarnestOrderRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SmUserRepository userRepository;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private HbzAreaServiceImpl hbzAreaService;

    @Override
    public Page<WarehouseAuditDTO> queryWarehouseAudit(WarehouseAuditDTO warehouseAuditDTO, Pageable pageable) {
        Page<HbzWarehouseAudit> pageList = warehouseAuditRepository.findAll(new Specification<HbzWarehouseAudit>() {
            @Override
            public Predicate toPredicate(Root<HbzWarehouseAudit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("warehouse").get("publishDate")));//按照发布时间排序
                // 仓库名称模糊查询
                if (StringUtils.isNotBlank(warehouseAuditDTO.getName())) {
                    predicateList.add(criteriaBuilder.like(root.get("warehouse").get("name"), "%" + warehouseAuditDTO.getName() + "%"));
                }
                //审核状态
                if (StringUtils.isNotBlank(warehouseAuditDTO.getType())) {
                    predicateList.add(criteriaBuilder.equal(root.get("type"), warehouseAuditDTO.getType()));
                }
                // 发布时间
                if (null != warehouseAuditDTO.getPublishDateStart() && !"".equals(warehouseAuditDTO.getPublishDateStart())) {
                    predicateList.add(criteriaBuilder.ge(root.get("warehouse").get("publishDate"), warehouseAuditDTO.getPublishDateStart()));
                }
                if (null != warehouseAuditDTO.getPublishDateEnd() && !"".equals(warehouseAuditDTO.getPublishDateEnd())) {
                    //查收时分钟值需增加59999计算
                    predicateList.add(criteriaBuilder.le(root.get("warehouse").get("publishDate"), warehouseAuditDTO.getPublishDateEnd() + 59999l));
                }
                //查询状态为新建和可用的状态可审核
                //predicateList.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("warehouse").get("lifecycle"), "0"), criteriaBuilder.equal(root.get("warehouse").get("lifecycle"), "1")));
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
        Page<WarehouseAuditDTO> WarehouseAuditDTOPage = pageList.map(new Converter<HbzWarehouseAudit, WarehouseAuditDTO>() {
            @Override
            public WarehouseAuditDTO convert(HbzWarehouseAudit hbzWarehouseAudit) {
                WarehouseAuditDTO warehouseDto = new WarehouseAuditDTO();
                if (hbzWarehouseAudit.getCreateUserId() != null) {
                    SmUser user = userRepository.findOne(hbzWarehouseAudit.getCreateUserId());
                    if (user != null) {
                        warehouseDto.setCreateUser(user.getUserName());
                    } else {
                        warehouseDto.setCreateUser(null);
                    }
                }
                Warehouse warehouse = hbzWarehouseAudit.getWarehouse();
                warehouseDto.setName(warehouse.getName());
                warehouseDto.setPublishDate(warehouse.getPublishDate());
                warehouseDto.setCapacity(warehouse.getCapacity());
                warehouseDto.setUnitPrice(warehouse.getUnitPrice());
                warehouseDto.setMinRentTime(warehouse.getMinRentTime());
                warehouseDto.setOwnerName(warehouse.getOwnerName());
                warehouseDto.setTelephone(warehouse.getTelephone());
                warehouseDto.setAddress(warehouse.getAddress());
                warehouseDto.setCoordX(warehouse.getCoordX());
                warehouseDto.setCoordY(warehouse.getCoordY());
                warehouseDto.setWarehouseDescribe(warehouse.getWarehouseDescribe());
                warehouseDto.setId(hbzWarehouseAudit.getId());
                warehouseDto.setCheckedDate(hbzWarehouseAudit.getCheckedDate());
                warehouseDto.setRecordComment(hbzWarehouseAudit.getRecordComment());
                String typeName = userInformationRepository.findUserClassification("WarehouseAuditType", hbzWarehouseAudit.getType());
                warehouseDto.setType(typeName);
                return warehouseDto;
            }
        });
        return WarehouseAuditDTOPage;
    }

    @Override
    public HbzWarehouseAudit findById(Long id) {
        HbzWarehouseAudit warehouseAudit = warehouseAuditRepository.findById(id);
        return warehouseAudit;
    }

    @Override
    public Boolean updateWarehouseAudit(WarehouseAuditDTO warehouseAuditDTO) {
        HbzWarehouseAudit warehouseAudit = warehouseAuditRepository.findById(warehouseAuditDTO.getId());
        if (null != warehouseAudit) {
            //审核状态为01(通过)或者02(不通过)
            if ("01".equals(warehouseAuditDTO.getType()) || "02".equals(warehouseAuditDTO.getType())) {
                warehouseAudit.setCheckedDate(new Date().getTime());
                warehouseAudit.setCreateUserId(SecurityUtils.getCurrentUserId());
                if ("01".equals(warehouseAuditDTO.getType())) {
                    //设置为可用
                    warehouseRepository.updateLifecycle(1, warehouseAudit.getWarehouse().getId());
                    //设置发布时间
                    warehouseRepository.updatePublishDate(new Date().getTime(), warehouseAudit.getWarehouse().getId());
                    Stacks.push(true);
                }
                if ("02".equals(warehouseAuditDTO.getType())) {
                    //设置为失效
                    warehouseRepository.updateLifecycle(3, warehouseAudit.getWarehouse().getId());
                    //设置发布时间
                    warehouseRepository.updatePublishDate(null,warehouseAudit.getWarehouse().getId());
                    Stacks.push(false);
                }
            }
            warehouseAudit.setType(warehouseAuditDTO.getType());
            warehouseAudit.setRecordComment(warehouseAuditDTO.getRecordComment());
            warehouseAudit.setStatus("1");//设置初始值状态
            warehouseAudit.setLastUpdatedDate(new Date().getTime());
            warehouseAudit.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        } else {
            return false;
        }
        return warehouseAuditRepository.save(warehouseAudit) != null;
    }

    @Override
    public WarehouseAuditDTO queryWarehouseAuditDetail(WarehouseAuditDTO warehouseAuditDTO) {
        WarehouseAuditDTO warehouseAuditDto = new WarehouseAuditDTO();
        HbzWarehouseAudit warehouseAudit = warehouseAuditRepository.findById(warehouseAuditDTO.getId());
        if (null != warehouseAudit) {
            if (warehouseAudit.getCreateUserId() != null) {
                SmUser user = userRepository.findOne(warehouseAudit.getCreateUserId());
                if (user != null) {
                    warehouseAuditDto.setCreateUser(user.getUserName());
                } else {
                    warehouseAuditDto.setCreateUser(null);
                }
            }
            Warehouse warehouse = warehouseAudit.getWarehouse();
            warehouseAuditDto.setName(warehouse.getName());
            warehouseAuditDto.setPublishDate(warehouse.getPublishDate());
            warehouseAuditDto.setCapacity(warehouse.getCapacity());
            warehouseAuditDto.setUnitPrice(warehouse.getUnitPrice());
            warehouseAuditDto.setMinRentTime(warehouse.getMinRentTime());
            warehouseAuditDto.setOwnerName(warehouse.getOwnerName());
            warehouseAuditDto.setTelephone(warehouse.getTelephone());
            warehouseAuditDto.setAddress(warehouse.getAddress());
            warehouseAuditDto.setCoordX(warehouse.getCoordX());
            warehouseAuditDto.setCoordY(warehouse.getCoordY());
            warehouseAuditDto.setWarehouseDescribe(warehouse.getWarehouseDescribe());
            warehouseAuditDto.setId(warehouseAudit.getId());
            warehouseAuditDto.setCheckedDate(warehouseAudit.getCheckedDate());
            warehouseAuditDto.setRecordComment(warehouseAudit.getRecordComment());
            String typeName = userInformationRepository.findUserClassification("WarehouseAuditType", warehouseAudit.getType());
            warehouseAuditDto.setType(typeName);
            return warehouseAuditDto;
        } else {
            return null;
        }
    }

    @Override
    public Page<WarehouseManageDTO> queryWarehouseManage(WarehouseManageDTO warehouseManageDTO, Pageable pageable) {
        Page<HbzWarehouseAudit> pageList = warehouseAuditRepository.findAll(new Specification<HbzWarehouseAudit>() {
            @Override
            public Predicate toPredicate(Root<HbzWarehouseAudit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("warehouse").get("publishDate")));//按照发布时间排序
                // 仓库名称模糊查询
                if (StringUtils.isNotBlank(warehouseManageDTO.getName())) {
                    predicateList.add(criteriaBuilder.like(root.get("warehouse").get("name"), "%" + warehouseManageDTO.getName() + "%"));
                }
                //状态
                if (StringUtils.isNotBlank(warehouseManageDTO.getLifecycle())) {
                    predicateList.add(criteriaBuilder.equal(root.get("warehouse").get("lifecycle"), warehouseManageDTO.getLifecycle()));
                }
                // 新建时间
                if (null != warehouseManageDTO.getCreatedDateStart() && !"".equals(warehouseManageDTO.getCreatedDateStart())) {
                    predicateList.add(criteriaBuilder.ge(root.get("warehouse").get("createdDate"), warehouseManageDTO.getCreatedDateStart()));
                }
                if (null != warehouseManageDTO.getCreatedDateEnd() && !"".equals(warehouseManageDTO.getCreatedDateEnd())) {
                    //查收时分钟值需增加59999计算
                    predicateList.add(criteriaBuilder.le(root.get("warehouse").get("createdDate"), warehouseManageDTO.getCreatedDateEnd() + 59999l));
                }
                //区域查询
                if (null != warehouseManageDTO.getOriginAreaId() && !"".equals( warehouseManageDTO.getOriginAreaId())){
                    predicateList.add(criteriaBuilder.equal(root.get("warehouse").get("originArea").get("id"),warehouseManageDTO.getOriginAreaId()));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
        Page<WarehouseManageDTO> warehouseManageDTOPage = pageList.map(new Converter<HbzWarehouseAudit, WarehouseManageDTO>() {
            @Override
            public WarehouseManageDTO convert(HbzWarehouseAudit hbzWarehouseAudit) {
                WarehouseManageDTO warehouseDto = new WarehouseManageDTO();
                if (hbzWarehouseAudit.getCreateUserId() != null) {
                    SmUser user = userRepository.findOne(hbzWarehouseAudit.getCreateUserId());
                    if (user != null) {
                        warehouseDto.setCreateUser(user.getUserName());
                    } else {
                        warehouseDto.setCreateUser(null);
                    }
                }
                Warehouse warehouse = hbzWarehouseAudit.getWarehouse();
                warehouseDto.setName(warehouse.getName());
                warehouseDto.setCheckedDate(hbzWarehouseAudit.getCheckedDate());
                warehouseDto.setRecordComment(hbzWarehouseAudit.getRecordComment());
                //warehouseDto.setPublishDate(warehouse.getPublishDate());
                HbzAreaDTO hbzAreaDTO = hbzAreaService.findById(hbzWarehouseAudit.getWarehouse().getOriginArea().getId());
                warehouseDto.setOriginAreaName(hbzAreaDTO.getAreaName());
                warehouseDto.setCreatedDate(warehouse.getCreatedDate());
                warehouseDto.setCapacity(warehouse.getCapacity());
                warehouseDto.setUnitPrice(warehouse.getUnitPrice());
                warehouseDto.setMinRentTime(warehouse.getMinRentTime());
                warehouseDto.setOwnerName(warehouse.getOwnerName());
                warehouseDto.setTelephone(warehouse.getTelephone());
                warehouseDto.setAddress(warehouse.getAddress());
                warehouseDto.setCoordX(warehouse.getCoordX());
                warehouseDto.setCoordY(warehouse.getCoordY());
                warehouseDto.setWarehouseDescribe(warehouse.getWarehouseDescribe());
                warehouseDto.setId(hbzWarehouseAudit.getId());
                String typeName = userInformationRepository.findUserClassification("WarehouseStatus", warehouse.getLifecycle() + "");
                warehouseDto.setLifecycleValue(typeName);
                warehouseDto.setLifecycle(warehouse.getLifecycle() + "");
                return warehouseDto;
            }
        });
        return warehouseManageDTOPage;
    }

    @Override
    public WarehouseManageDTO queryWarehouseManageDetail(WarehouseManageDTO warehouseManageDTO) {
        WarehouseManageDTO warehouseManageDto = new WarehouseManageDTO();
        HbzWarehouseAudit warehouseAudit = warehouseAuditRepository.findById(warehouseManageDTO.getId());
        if (null != warehouseAudit) {
            if (warehouseAudit.getCreateUserId() != null) {
                SmUser user = userRepository.findOne(warehouseAudit.getCreateUserId());
                if (user != null) {
                    warehouseManageDto.setCreateUser(user.getUserName());
                } else {
                    warehouseManageDto.setCreateUser(null);
                }
            }
            Warehouse warehouse = warehouseAudit.getWarehouse();
            warehouseManageDto.setName(warehouse.getName());
            //warehouseManageDto.setPublishDate(warehouse.getPublishDate());
            warehouseManageDto.setCreatedDate(warehouse.getCreatedDate());
            HbzAreaDTO hbzAreaDTO = hbzAreaService.findById(warehouseAudit.getWarehouse().getOriginArea().getId());
            warehouseManageDto.setOriginAreaName(hbzAreaDTO.getAreaName());
            warehouseManageDto.setCapacity(warehouse.getCapacity());
            warehouseManageDto.setUnitPrice(warehouse.getUnitPrice());
            warehouseManageDto.setMinRentTime(warehouse.getMinRentTime());
            warehouseManageDto.setOwnerName(warehouse.getOwnerName());
            warehouseManageDto.setTelephone(warehouse.getTelephone());
            warehouseManageDto.setAddress(warehouse.getAddress());
            warehouseManageDto.setCoordX(warehouse.getCoordX());
            warehouseManageDto.setCoordY(warehouse.getCoordY());
            warehouseManageDto.setWarehouseDescribe(warehouse.getWarehouseDescribe());
            warehouseManageDto.setId(warehouseAudit.getId());
            String typeName = userInformationRepository.findUserClassification("WarehouseStatus", warehouse.getLifecycle() + "");
            warehouseManageDto.setLifecycleValue(typeName);
            warehouseManageDto.setLifecycle(warehouse.getLifecycle() + "");
            return warehouseManageDto;
        } else {
            return null;
        }
    }

    @Override
    public Boolean updateWarehouseManage(WarehouseManageDTO warehouseManageDTO) {
        HbzWarehouseAudit warehouseAudit = warehouseAuditRepository.findById(warehouseManageDTO.getId());
        if (null != warehouseAudit) {
            Warehouse warehouse = warehouseAudit.getWarehouse();
            //修改备注
            warehouseRepository.updateDescribe(warehouseManageDTO.getWarehouseDescribe(), warehouse.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateLifecycleTimer() {
        warehouseRepository.updateLifecycleTimer();
    }

    @Override
    public Boolean updateLifecycleOverdue(WarehouseManageDTO warehouseManageDTO) {
        HbzWarehouseAudit warehouseAudit = warehouseAuditRepository.findById(warehouseManageDTO.getId());
        if (null != warehouseAudit) {
            Warehouse warehouse = warehouseAudit.getWarehouse();
            //修改仓储过期
            warehouseRepository.updateLifecycleOverdue(warehouse.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<WarehouseEarnestOrderDTO> queryWarehouseEarnestOrderByPage(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO, Pageable pageable) {
        Page<WarehouseEarnestOrder> pageList = warehouseEarnestOrderRepository.findAll(new Specification<WarehouseEarnestOrder>() {
            @Override
            public Predicate toPredicate(Root<WarehouseEarnestOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(warehouseEarnestOrderDTO.getWarehouseName())) {
                    predicateList.add(criteriaBuilder.like(root.get("warehouseName"), "%" + warehouseEarnestOrderDTO.getWarehouseName() + "%"));
                }
                // 支付状态
                if (warehouseEarnestOrderDTO.getPayStatus() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("payStatus"), warehouseEarnestOrderDTO.getPayStatus()));
                }
                // 创建时间
                if (StringUtils.isNotBlank(warehouseEarnestOrderDTO.getCreateDateStart())) {
                    predicateList.add(criteriaBuilder.ge(root.get("createdDate"), DateUtils.defaultStringFormatToLong(warehouseEarnestOrderDTO.getCreateDateStart())));
                }
                if (StringUtils.isNotBlank(warehouseEarnestOrderDTO.getCreateDateEnd())) {
                    predicateList.add(criteriaBuilder.le(root.get("createdDate"), DateUtils.defaultStringFormatToLong(warehouseEarnestOrderDTO.getCreateDateEnd())));
                }
                if (StringUtils.isNotBlank(warehouseEarnestOrderDTO.getStatus())) {
                    predicateList.add(criteriaBuilder.equal(root.get("status"), warehouseEarnestOrderDTO.getStatus()));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
        Page<WarehouseEarnestOrderDTO> warehouseEarnestOrderDTOPage = pageList.map(new Converter<WarehouseEarnestOrder, WarehouseEarnestOrderDTO>() {
            @Override
            public WarehouseEarnestOrderDTO convert(WarehouseEarnestOrder warehouseEarnestOrder) {
                WarehouseEarnestOrderDTO warehouseEarnestOrderDto = new WarehouseEarnestOrderDTO();
                //BeanUtils.copyProperties(warehouseEarnestOrder,warehouseEarnestOrderDto);
                warehouseEarnestOrderDto.setEarnestPrice(warehouseEarnestOrder.getEarnestPrice());
                warehouseEarnestOrderDto.setId(warehouseEarnestOrder.getId());
                warehouseEarnestOrderDto.setOrderNo(warehouseEarnestOrder.getOrderNo());
                warehouseEarnestOrderDto.setPayStatus(warehouseEarnestOrder.getPayStatus());
                warehouseEarnestOrderDto.setPayStatusValue(warehouseEarnestOrder.getPayStatus().getName());
                warehouseEarnestOrderDto.setWarehouseName(warehouseEarnestOrder.getWarehouseName());
                warehouseEarnestOrderDto.setPayType(warehouseEarnestOrder.getPayType());
                if (null != warehouseEarnestOrder.getPayType()) {
                    warehouseEarnestOrderDto.setPayTypeValue(warehouseEarnestOrder.getPayType().getName());
                }
                warehouseEarnestOrderDto.setStatus(warehouseEarnestOrder.getStatus());
                warehouseEarnestOrderDto.setCreateDate(warehouseEarnestOrder.getCreatedDate());
                WarehouseManageDTO warehouseDto = new WarehouseManageDTO();
                HbzUser user = hbzUserRepository.findOne(warehouseEarnestOrder.getCreateUser().getId());
                warehouseEarnestOrderDto.setCreateUser(user.getNickName());
                warehouseEarnestOrderDto.setDealUser(user.getNickName());
                warehouseDto.setCreateUser(user.getNickName());
                Warehouse warehouse = warehouseEarnestOrder.getWarehouse();
                warehouseDto.setName(warehouse.getName());
                //warehouseDto.setPublishDate(warehouse.getPublishDate());
                warehouseDto.setCreatedDate(warehouse.getCreatedDate());
                warehouseDto.setCapacity(warehouse.getCapacity());
                warehouseDto.setUnitPrice(warehouse.getUnitPrice());
                warehouseDto.setMinRentTime(warehouse.getMinRentTime());
                warehouseDto.setOwnerName(warehouse.getOwnerName());
                warehouseDto.setTelephone(warehouse.getTelephone());
                warehouseDto.setAddress(warehouse.getAddress());
                warehouseDto.setCoordX(warehouse.getCoordX());
                warehouseDto.setCoordY(warehouse.getCoordY());
                warehouseDto.setWarehouseDescribe(warehouse.getWarehouseDescribe());
                warehouseDto.setId(warehouse.getId());
                String typeName = userInformationRepository.findUserClassification("WarehouseStatus", warehouse.getLifecycle() + "");
                warehouseDto.setLifecycleValue(typeName);
                warehouseDto.setLifecycle(warehouse.getLifecycle() + "");
                warehouseEarnestOrderDto.setWarehouseDTO(warehouseDto);
                return warehouseEarnestOrderDto;
            }
        });
        return warehouseEarnestOrderDTOPage;
    }

    @Override
    public WarehouseEarnestOrderDTO queryWarehouseEarnestOrderDetail(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        WarehouseEarnestOrder warehouseEarnestOrder = warehouseEarnestOrderRepository.findOne(warehouseEarnestOrderDTO.getId());
        WarehouseEarnestOrderDTO warehouseEarnestOrderDto = new WarehouseEarnestOrderDTO();
        warehouseEarnestOrderDto.setEarnestPrice(warehouseEarnestOrder.getEarnestPrice());
        warehouseEarnestOrderDto.setId(warehouseEarnestOrder.getId());
        warehouseEarnestOrderDto.setOrderNo(warehouseEarnestOrder.getOrderNo());
        warehouseEarnestOrderDto.setPayStatus(warehouseEarnestOrder.getPayStatus());
        warehouseEarnestOrderDto.setPayStatusValue(warehouseEarnestOrder.getPayStatus().getName());
        warehouseEarnestOrderDto.setWarehouseName(warehouseEarnestOrder.getWarehouseName());
        warehouseEarnestOrderDto.setPayType(warehouseEarnestOrder.getPayType());
        if (null != warehouseEarnestOrder.getPayType()) {
            warehouseEarnestOrderDto.setPayTypeValue(warehouseEarnestOrder.getPayType().getName());
        }
        warehouseEarnestOrderDto.setStatus(warehouseEarnestOrder.getStatus());
        warehouseEarnestOrderDto.setCreateDate(warehouseEarnestOrder.getCreatedDate());
        WarehouseManageDTO warehouseDto = new WarehouseManageDTO();
        HbzUser user = hbzUserRepository.findOne(warehouseEarnestOrder.getCreateUser().getId());
        warehouseEarnestOrderDto.setCreateUser(user.getNickName());
        warehouseEarnestOrderDto.setDealUser(user.getNickName());
        warehouseDto.setCreateUser(user.getNickName());
        Warehouse warehouse = warehouseEarnestOrder.getWarehouse();
        warehouseDto.setName(warehouse.getName());
        //warehouseDto.setPublishDate(warehouse.getPublishDate());
        warehouseDto.setCreatedDate(warehouse.getCreatedDate());
        warehouseDto.setCapacity(warehouse.getCapacity());
        warehouseDto.setUnitPrice(warehouse.getUnitPrice());
        warehouseDto.setMinRentTime(warehouse.getMinRentTime());
        warehouseDto.setOwnerName(warehouse.getOwnerName());
        warehouseDto.setTelephone(warehouse.getTelephone());
        warehouseDto.setAddress(warehouse.getAddress());
        warehouseDto.setCoordX(warehouse.getCoordX());
        warehouseDto.setCoordY(warehouse.getCoordY());
        warehouseDto.setWarehouseDescribe(warehouse.getWarehouseDescribe());
        warehouseDto.setId(warehouse.getId());
        String typeName = userInformationRepository.findUserClassification("WarehouseStatus", warehouse.getLifecycle() + "");
        warehouseDto.setLifecycleValue(typeName);
        warehouseDto.setLifecycle(warehouse.getLifecycle() + "");
        warehouseEarnestOrderDto.setWarehouseDTO(warehouseDto);
        return warehouseEarnestOrderDto;
    }

    @Override
    public String refundWarehouseEarnestOrder(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        String result = null;
        //TODO 修改仓储订单状态、仓储状态、退款状态、调用支付退款接口
        WarehouseEarnestOrder warehouseEarnestOrder = warehouseEarnestOrderRepository.findOne(warehouseEarnestOrderDTO.getId());
        if (null != warehouseEarnestOrder && !"PAID".equals(warehouseEarnestOrder.getPayStatus())) {
            result = "该订单支付未成功或者已退款完成，不能继续退款";
        }
        //1.调用支付退款接口
        //2.如果成功修改仓储状态以及仓储状态和审核状态
        //3.保存记录信息到退款记录表
        return result;
    }
}
