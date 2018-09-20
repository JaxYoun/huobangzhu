package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.JsonArrToListMapper;
import com.troy.keeper.hbz.mapper.MyHbzUserMapper;
import com.troy.keeper.hbz.mapper.WarehouseEarnestOrderMapper;
import com.troy.keeper.hbz.mapper.WarehouseMapper;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.*;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.MapService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.hbz.vo.HbzUserVO;
import com.troy.keeper.hbz.vo.WarehouseEarnestOrderVO;
import com.troy.keeper.hbz.vo.WarehouseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 16:18
 */
@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private MapService map;

    @Autowired
    private HbzAreaRepository hbzAreaRepository;

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseEarnestOrderRepository warehouseEarnestOrderRepository;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private CostStaticsRepository costStaticsRepository;

    @Autowired
    private WarehouseEarnestOrderMapper warehouseEarnestOrderMapper;

    @Autowired
    private MyHbzUserMapper myHbzUserMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private HbzAreaServiceImpl hbzAreaService;

    @Autowired
    private WarehouseApplyRepository warehouseApplyRepository;

    @Autowired
    private WarehouseAuditRepository warehouseAuditRepository;

    @Override
    public WarehouseVO getWarehouseById(WarehouseDTO warehouseDTO) {
        Warehouse warehouseFromDB = warehouseRepository.findOne(warehouseDTO.getId());
        if (warehouseFromDB == null) {
            return null;
        }
        WarehouseVO warehouseVO = new WarehouseVO();
        BeanUtils.copyProperties(warehouseFromDB, warehouseVO);
        return warehouseVO;
    }

    @Override
    @Transactional
    public WarehouseVO createWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(warehouseDTO, warehouse, "createUser");
        //保存仓储区域ID 根据ID查询具体区域
        HbzAreaDTO hbzAreaDTO = this.map.getAreaByLocation(warehouseDTO.getCoordX(), warehouseDTO.getCoordY());
        warehouse.setOriginArea(hbzAreaRepository.findByOutCode(hbzAreaDTO.getOutCode()));
        warehouse.setCreateUser(this.hbzUserRepository.findOne(warehouseDTO.getCreateUserDTO().getId()));
        Warehouse warehouseFromDB = this.warehouseRepository.save(warehouse);

        //插入审核记录
        HbzWarehouseAudit hbzWarehouseAudit = new HbzWarehouseAudit();
        hbzWarehouseAudit.setWarehouse(warehouseFromDB);
        hbzWarehouseAudit.setCreatedBy(warehouseFromDB.getCreatedBy());
        hbzWarehouseAudit.setCreatedDate(Instant.now().getEpochSecond());
        hbzWarehouseAudit.setLastUpdatedBy(warehouseFromDB.getCreatedBy());
        hbzWarehouseAudit.setLastUpdatedDate(Instant.now().getEpochSecond());
        hbzWarehouseAudit.setType("00");
        hbzWarehouseAudit.setStatus(Const.STATUS_ENABLED);
        this.warehouseApplyRepository.save(hbzWarehouseAudit);

        WarehouseVO warehouseVO = new WarehouseVO();
        BeanUtils.copyProperties(warehouseFromDB, warehouseVO);
        warehouseVO.setContentImageList(JsonArrToListMapper.platArrToList(staticImagePrefix, warehouseFromDB.getContentImageList()));
        warehouseVO.setTitleImageList(JsonArrToListMapper.platArrToList(staticImagePrefix, warehouseFromDB.getTitleImageList()));
        return warehouseVO;
    }

    @Override
    public HbzUserDTO findByWarehourse(WarehouseDTO warehouseDTO) {
        Warehouse w = warehouseRepository.findOne(warehouseDTO.getId());
        return hbzUserService.findById(w.getCreateUser().getId());
    }

    @Override
    public boolean deleteWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouseFromDB = this.warehouseRepository.findOne(warehouseDTO.getId());
        warehouseFromDB.setStatus(Const.STATUS_DISABLED);
        return this.warehouseRepository.save(warehouseFromDB) != null;
    }

    @Override
    public WarehouseVO getWarehouseDetail(WarehouseDTO warehouseDTO) {
        WarehouseVO warehouseVO = new WarehouseVO();
        Warehouse warehouseFromDB = this.warehouseRepository.findOne(warehouseDTO.getId());
        BeanUtils.copyProperties(warehouseFromDB, warehouseVO);
        //保存仓储区域ID 根据ID查询具体区域
        warehouseVO.setFormatedCreateDate(DateUtils.longToString(warehouseFromDB.getCreatedDate(), DateUtils.yyyy_MM_dd_HH_mm));
        warehouseVO.setFormatedLastModifiedDate(DateUtils.longToString(warehouseFromDB.getLastUpdatedDate(), DateUtils.yyyy_MM_dd_HH_mm));
        //HbzAreaDTO hbzAreaDTO = hbzAreaService.findById(warehouseFromDB.getOriginArea().getId());
        //HbzAreaDTO parentDTO = hbzAreaDTO.getParent();
        //if(null!=parentDTO){
        //    HbzAreaDTO parentPDTO = parentDTO.getParent();
        //    if(null != parentPDTO && (0 != parentPDTO.getId())){
        //        warehouseVO.setOriginAreaName(parentPDTO.getAreaName()+"-"+parentDTO.getAreaName()+"-"+hbzAreaDTO.getAreaName());
        //    }else{
        //        warehouseVO.setOriginAreaName(parentDTO.getAreaName()+"-"+hbzAreaDTO.getAreaName());
        //    }
        //}else{
        //    warehouseVO.setOriginAreaName(hbzAreaDTO.getAreaName());
        //}
        warehouseVO.setOriginAreaId(warehouseFromDB.getOriginArea().getId());
        HbzUserVO hbzUserVO = new HbzUserVO();
        BeanUtils.copyProperties(warehouseFromDB.getCreateUser(), hbzUserVO, "bond, activated, starLevel, langKey, imageUrl, activationKey, sex, roleName, org, orgId");
        warehouseVO.setCreateUserVO(hbzUserVO);
        warehouseVO.setContentImageList(JsonArrToListMapper.platArrToList(staticImagePrefix, warehouseFromDB.getContentImageList()));
        warehouseVO.setTitleImageList(JsonArrToListMapper.platArrToList(staticImagePrefix, warehouseFromDB.getTitleImageList()));
        return warehouseVO;
    }

    @Override
    public boolean updateWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = this.warehouseRepository.findOne(warehouseDTO.getId());
        BeanUtils.copyProperties(warehouseDTO, warehouse, "createUser", "createdBy", "createdDate", "titleImageList", "contentImageList", "status", "lifecycle");
        //保存仓储区域ID 根据ID查询具体区域
        HbzAreaDTO hbzAreaDTO = this.map.getAreaByLocation(warehouseDTO.getCoordX(), warehouseDTO.getCoordY());
        warehouse.setOriginArea(hbzAreaRepository.findByOutCode(hbzAreaDTO.getOutCode()));
        warehouse.setTitleImageList(JsonArrToListMapper.cutPrefix(this.staticImagePrefix, warehouseDTO.getTitleImageList()));
        warehouse.setContentImageList(JsonArrToListMapper.cutPrefix(this.staticImagePrefix, warehouseDTO.getContentImageList()));
        return this.warehouseRepository.save(warehouse) != null;
    }

    /**
     * 分页条件查询我创建的仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    @Override
    public Page<WarehouseVO> getMyWarehouseListByPage(WarehouseDTO warehouseDTO, Pageable pageable) {
        //构造查询条件
        Specification<Warehouse> warehouseSpecification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            //仓储名
            if (StringUtils.isNoneBlank(warehouseDTO.getName())) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + warehouseDTO.getName() + "%"));
            }

            //容量
            if (warehouseDTO.getMinCapacity() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("capacity"), warehouseDTO.getMinCapacity()));
            }
            if (warehouseDTO.getMaxCapacity() != null) {
                predicateList.add(criteriaBuilder.le(root.get("capacity"), warehouseDTO.getMaxCapacity()));
            }

            //单价
            if (warehouseDTO.getMinUnitPrice() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("unitPrice"), warehouseDTO.getMinUnitPrice()));
            }
            if (warehouseDTO.getMaxUnitPrice() != null) {
                predicateList.add(criteriaBuilder.le(root.get("unitPrice"), warehouseDTO.getMaxUnitPrice()));
            }

            //起租时长
            if (warehouseDTO.getMinMinRentTime() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("minRentTime"), warehouseDTO.getMinRentTime()));
            }
            if (warehouseDTO.getMaxMinRentTime() != null) {
                predicateList.add(criteriaBuilder.le(root.get("minRentTime"), warehouseDTO.getMaxMinRentTime()));
            }

            //地址名
            if (StringUtils.isNoneBlank(warehouseDTO.getAddress())) {
                predicateList.add(criteriaBuilder.like(root.get("address"), "%" + warehouseDTO.getAddress() + "%"));
            }

            //创建人
            if (warehouseDTO.getCreatedBy() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("createdBy"), warehouseDTO.getCreatedBy()));
            }

            //流转状态
            if (warehouseDTO.getLifecycle() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("lifecycle"), warehouseDTO.getLifecycle()));
            }

            //跟新时间
            if (StringUtils.isNotBlank(warehouseDTO.getMinFormatedLastModifiedDate())) {
                try {
                    predicateList.add(criteriaBuilder.ge(root.get("lastUpdatedDate"), DateUtils.stringToLong(warehouseDTO.getMinFormatedLastModifiedDate(), DateUtils.yyyy_MM_dd_HH_mm)));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (StringUtils.isNotBlank(warehouseDTO.getMaxFormatedLastModifiedDate())) {
                try {
                    predicateList.add(criteriaBuilder.le(root.get("lastUpdatedDate"), DateUtils.stringToLong(warehouseDTO.getMaxFormatedLastModifiedDate(), DateUtils.yyyy_MM_dd_HH_mm)));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            //非删除状态
            predicateList.add(criteriaBuilder.equal(root.get("status"), warehouseDTO.getStatus()));

            //区域查询
            if (null != warehouseDTO.getOriginAreaId() && !"".equals( warehouseDTO.getOriginAreaId())){
                predicateList.add(criteriaBuilder.equal(root.join("originArea").get("id"),warehouseDTO.getOriginAreaId()));
            }

            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);
            return criteriaBuilder.and(predicateArr);
        };

        //执行分页查询
        Page<Warehouse> warehousePage = warehouseRepository.findAll(warehouseSpecification, pageable);

        //将查询记录封装为VO
        Page<WarehouseVO> warehouseVOPage = warehousePage.map((warehouse) -> {
            WarehouseVO warehouseVO = new WarehouseVO();
            BeanUtils.copyProperties(warehouse, warehouseVO);
            warehouseVO.setFormatedCreateDate(DateUtils.longToString(warehouse.getCreatedDate(), DateUtils.yyyy_MM_dd_HH_mm));
            warehouseVO.setFormatedLastModifiedDate(DateUtils.longToString(warehouse.getLastUpdatedDate(), DateUtils.yyyy_MM_dd_HH_mm));
            HbzAreaDTO hbzAreaDTO = hbzAreaService.findById(warehouse.getOriginArea().getId());
            HbzAreaDTO parentDTO = hbzAreaDTO.getParent();
            if(null!=parentDTO){
                HbzAreaDTO parentPDTO = parentDTO.getParent();
                if(null != parentPDTO && (0 != parentPDTO.getId())){
                        warehouseVO.setOriginAreaName(parentPDTO.getAreaName()+"-"+parentDTO.getAreaName()+"-"+hbzAreaDTO.getAreaName());
                  }else{
                    warehouseVO.setOriginAreaName(parentDTO.getAreaName()+"-"+hbzAreaDTO.getAreaName());
                }
            }else{
                warehouseVO.setOriginAreaName(hbzAreaDTO.getAreaName());
            }
            HbzWarehouseAudit hbzWarehouseAudit = warehouseAuditRepository.findByWarehouseId(warehouse.getId());
            warehouseVO.setType(hbzWarehouseAudit.getType());
            warehouseVO.setOriginAreaId(warehouse.getOriginArea().getId());
            HbzUserVO hbzUserVO = new HbzUserVO();
            BeanUtils.copyProperties(warehouse.getCreateUser(), hbzUserVO, "bond, activated, starLevel, langKey, imageUrl, activationKey, sex, roleName, org, orgId");
            warehouseVO.setCreateUserVO(hbzUserVO);
            warehouseVO.setContentImageList(JsonArrToListMapper.platArrToList(staticImagePrefix, warehouse.getContentImageList()));
            warehouseVO.setTitleImageList(JsonArrToListMapper.platArrToList(staticImagePrefix, warehouse.getTitleImageList()));
            return warehouseVO;
        });
        return warehouseVOPage;
    }

    /**
     * 生成仓储租赁诚意金订单
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @Override
    public WarehouseEarnestOrderVO generateWarehouseEarnestOrder(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        WarehouseEarnestOrder warehouseEarnestOrder = this.warehouseEarnestOrderMapper.dtoToEntity(warehouseEarnestOrderDTO);

        //保存
        WarehouseEarnestOrder warehouseEarnestOrderFromDb = warehouseEarnestOrderRepository.save(warehouseEarnestOrder);
        Warehouse warehouseFromDb = this.warehouseRepository.findOne(warehouseEarnestOrderFromDb.getWarehouse().getId());
        if (warehouseFromDb != null) {
            warehouseEarnestOrderFromDb.setWarehouse(warehouseFromDb);
        }
        WarehouseEarnestOrderVO warehouseEarnestOrderVO = this.warehouseEarnestOrderMapper.entityToVo(warehouseEarnestOrderFromDb);
        //受理人
        if (warehouseEarnestOrderFromDb.getDealUser() != null) {
            HbzUser dealHbzUser = warehouseEarnestOrderFromDb.getDealUser();
            HbzUserVO dealHbzUserVO = new HbzUserVO();
            BeanUtils.copyProperties(dealHbzUser, dealHbzUserVO);
            warehouseEarnestOrderVO.setDealUser(dealHbzUserVO);
        }

        //图片地址拼接
        String title = warehouseEarnestOrderFromDb.getWarehouse().getTitleImageList();
        warehouseEarnestOrderVO.getWarehouse().setTitleImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, title));
        String content = warehouseEarnestOrderFromDb.getWarehouse().getContentImageList();
        warehouseEarnestOrderVO.getWarehouse().setContentImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, content));

        return warehouseEarnestOrderVO;
    }

    /**
     * 分页条件查询我的仓储诚意金订单
     *
     * @param warehouseEarnestOrderDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<WarehouseEarnestOrderVO> getMyWarehouseEarnestOrderByPage(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO, Pageable pageable) {
        Specification<WarehouseEarnestOrder> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(warehouseEarnestOrderDTO.getWarehouseName())) {
                predicateList.add(criteriaBuilder.like(root.get("warehouseName"), "%" + warehouseEarnestOrderDTO.getWarehouseName() + "%"));
            }
            if (warehouseEarnestOrderDTO.getCreateDate() != null) {
                predicateList.add(criteriaBuilder.ge(root.get("createdDate"), warehouseEarnestOrderDTO.getCreatedDateGE()));
                predicateList.add(criteriaBuilder.le(root.get("createdDate"), warehouseEarnestOrderDTO.getCreatedDateLE()));
            }

            // 支付状态
            if (warehouseEarnestOrderDTO.getPayStatus() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("payStatus"), warehouseEarnestOrderDTO.getPayStatus()));
            }

            if (warehouseEarnestOrderDTO.getCreatedBy() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("createdBy"), warehouseEarnestOrderDTO.getCreatedBy()));
            }
            if (StringUtils.isNotBlank(warehouseEarnestOrderDTO.getStatus())) {
                predicateList.add(criteriaBuilder.equal(root.get("status"), warehouseEarnestOrderDTO.getStatus()));
            }

            Predicate[] predicateArr = new Predicate[predicateList.size()];
            predicateList.toArray(predicateArr);

            return criteriaBuilder.and(predicateArr);
        });

        Page<WarehouseEarnestOrder> warehouseEarnestOrderPage = this.warehouseEarnestOrderRepository.findAll(specification, pageable);

        return warehouseEarnestOrderPage.map(it -> {
            WarehouseEarnestOrderVO warehouseEarnestOrderVOFromDb = warehouseEarnestOrderMapper.entityToVo(it);
            //图片地址拼接
            String title = it.getWarehouse().getTitleImageList();
            warehouseEarnestOrderVOFromDb.getWarehouse().setTitleImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, title));
            String content = it.getWarehouse().getContentImageList();
            warehouseEarnestOrderVOFromDb.getWarehouse().setContentImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, content));

            return warehouseEarnestOrderVOFromDb;
        });
    }

    /**
     * 获取仓储租赁诚意金详情
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    @Override
    public WarehouseEarnestOrderVO getWarehouseEarnestOrderDetail(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO) {
        WarehouseEarnestOrder warehouseEarnestOrderFromDb = this.warehouseEarnestOrderRepository.findOne(warehouseEarnestOrderDTO.getId());
        WarehouseEarnestOrderVO warehouseEarnestOrderVOFromDb = this.warehouseEarnestOrderMapper.entityToVo(warehouseEarnestOrderFromDb);

        //图片地址拼接
        String title = warehouseEarnestOrderFromDb.getWarehouse().getTitleImageList();
        warehouseEarnestOrderVOFromDb.getWarehouse().setTitleImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, title));
        String content = warehouseEarnestOrderFromDb.getWarehouse().getContentImageList();
        warehouseEarnestOrderVOFromDb.getWarehouse().setContentImageList(JsonArrToListMapper.platArrToList(this.staticImagePrefix, content));

        return warehouseEarnestOrderVOFromDb;
    }

    /**
     * 费用支出统计
     */
    @Override
    public Map<String, Double> cost(CostStaticsDTO costStaticsDTO) {
        Map<String, Double> result = new HashMap<>();
        Double wareHouse = costStaticsRepository.queryWarehouseCostStatistics(hbzUserService.currentUser().getId(), costStaticsDTO);
        result.put("wareHouse", wareHouse);
        return result;
    }

    /**
     * 通过订单号查询仓储诚意金
     *
     * @param orderNo
     * @return
     */
    @Override
    public WarehouseEarnestOrder findByOrderNo(String orderNo) {
        return warehouseEarnestOrderRepository.findByOrderNo(orderNo);
    }

    /**
     * 修改仓储诚意金状态
     *
     * @param warehouseOrder
     */
    @Override
    public void save(WarehouseEarnestOrder warehouseOrder) {
        warehouseEarnestOrderRepository.save(warehouseOrder);
    }
}