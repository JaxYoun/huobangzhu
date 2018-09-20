package com.troy.keeper.hbz.repository.impl;

import com.troy.keeper.hbz.dto.CostStaticsDTO;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.po.HbzBond;
import com.troy.keeper.hbz.po.HbzBondGrade;
import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.repository.CostStaticsRepository;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 使用实体查询费用统计repository实现
 * @Date: Created in 9:55  2018/3/1.
 * @Midified By:
 */
@Component
public class CostStaticsRepositoryImpl implements CostStaticsRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Object[]> queryOrderCostStatistics(Long currentId, CostStaticsDTO costStaticsDTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root = query.from(HbzOrder.class);
        query.select(builder.array(root.get("orderType"), builder.sum(root.get("amount"))));
        query.groupBy(root.get("orderType"));
        List<Predicate> predicateList = new ArrayList<>();
        //默认开始时间为当前年的第一天
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        boolean startDateFlag = costStaticsDTO.getStartDate() != null && !"".equals(costStaticsDTO.getStartDate());
        boolean endDateFlag = costStaticsDTO.getEndDate() != null && !"".equals(costStaticsDTO.getEndDate());
        if (startDateFlag) {
            predicateList.add(builder.ge(root.get("createdDate"), costStaticsDTO.getStartDate()));
        }
        if (endDateFlag) {
            predicateList.add(builder.le(root.get("createdDate"), costStaticsDTO.getEndDate()));
        }
        //默认时间查询
        if (!startDateFlag && !endDateFlag) {
            predicateList.add(builder.ge(root.get("createdDate"), DateUtils.getYearFirst(currentYear).getTime()));
            predicateList.add(builder.le(root.get("createdDate"), new Date().getTime()));
        }
        predicateList.add(builder.or(builder.equal(root.get("orderTrans"), OrderTrans.PAID), builder.equal(root.get("orderTrans"), OrderTrans.LIQUIDATION_COMPLETED)));
        predicateList.add(builder.equal(root.get("createUser"), currentId));
        query.where(predicateList.toArray(new Predicate[predicateList.size()]));
        List<Object[]> result = entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    public List<Object[]> queryBondCostStatistics(Long currentId, CostStaticsDTO costStaticsDTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root1 = query.from(HbzBond.class);
        Root root2 = query.from(HbzBondGrade.class);
        query.select(builder.array(root2.get("bondType"), root2.get("grade"),builder.sum(root1.get("amount"))));
        query.groupBy(root2.get("bondType"), root2.get("grade"));
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(builder.equal(root2.get("id"), root1.get("bondGrade").get("id")));
        //默认开始时间为当前年的第一天
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        boolean startDateFlag = costStaticsDTO.getStartDate() != null && !"".equals(costStaticsDTO.getStartDate());
        boolean endDateFlag = costStaticsDTO.getEndDate() != null && !"".equals(costStaticsDTO.getEndDate());
        if (startDateFlag) {
            predicateList.add(builder.ge(root1.get("createdDate"), costStaticsDTO.getStartDate()));
        }
        if (endDateFlag) {
            predicateList.add(builder.le(root1.get("createdDate"), costStaticsDTO.getEndDate()));
        }
        //默认时间查询
        if (!startDateFlag && !endDateFlag) {
            predicateList.add(builder.ge(root1.get("createdDate"), DateUtils.getYearFirst(currentYear).getTime()));
            predicateList.add(builder.le(root1.get("createdDate"), new Date().getTime()));
        }
        predicateList.add(builder.or(builder.equal(root1.get("bondStatus"), 1), builder.equal(root1.get("bondStatus"), 2)));
        predicateList.add(builder.or(builder.and(builder.equal(root2.get("bondType"), "BOND_FSL"), builder.equal(root2.get("grade"), "C500")),builder.and(builder.equal(root2.get("bondType"), "BOND_LTL"), builder.equal(root2.get("grade"), "C200")),builder.and(builder.equal(root2.get("bondType"), "BOND_SL"), builder.equal(root2.get("grade"), "EC2000"))));
        predicateList.add(builder.equal(root1.get("user").get("id"), currentId));
        query.where(predicateList.toArray(new Predicate[predicateList.size()]));
        List<Object[]> result = entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    public Double queryWarehouseCostStatistics(Long currentId, CostStaticsDTO costStaticsDTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root = query.from(WarehouseEarnestOrder.class);
        query.select(builder.sum(root.get("earnestPrice")));
        List<Predicate> predicateList = new ArrayList<>();
        //默认开始时间为当前年的第一天
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        boolean startDateFlag = costStaticsDTO.getStartDate() != null && !"".equals(costStaticsDTO.getStartDate());
        boolean endDateFlag = costStaticsDTO.getEndDate() != null && !"".equals(costStaticsDTO.getEndDate());
        if (startDateFlag) {
            predicateList.add(builder.ge(root.get("createdDate"), costStaticsDTO.getStartDate()));
        }
        if (endDateFlag) {
            predicateList.add(builder.le(root.get("createdDate"), costStaticsDTO.getEndDate()));
        }
        //默认时间查询
        if (!startDateFlag && !endDateFlag) {
            predicateList.add(builder.ge(root.get("createdDate"), DateUtils.getYearFirst(currentYear).getTime()));
            predicateList.add(builder.le(root.get("createdDate"), new Date().getTime()));
        }
        predicateList.add(builder.and(builder.equal(root.get("payStatus"), WarehouseEarnestPayStatusEnum.PAID)));
        predicateList.add(builder.equal(root.get("createUser").get("id"), currentId));
        query.where(predicateList.toArray(new Predicate[predicateList.size()]));
        List result = entityManager.createQuery(query).getResultList();
        if(result.get(0)!=null){
            return (double)result.get(0);
        }else{
            return 0.0;
        }
    }

    @Override
    public List<Object[]> queryOrderIncomeStatistics(Long currentId, CostStaticsDTO costStaticsDTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root = query.from(HbzOrder.class);
        query.select(builder.array(root.get("orderType"), builder.sum(root.get("amount"))));
        query.groupBy(root.get("orderType"));
        List<Predicate> predicateList = new ArrayList<>();
        //默认开始时间为当前年的第一天
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        boolean startDateFlag = costStaticsDTO.getStartDate() != null && !"".equals(costStaticsDTO.getStartDate());
        boolean endDateFlag = costStaticsDTO.getEndDate() != null && !"".equals(costStaticsDTO.getEndDate());
        if (startDateFlag) {
            predicateList.add(builder.ge(root.get("createdDate"), costStaticsDTO.getStartDate()));
        }
        if (endDateFlag) {
            predicateList.add(builder.le(root.get("createdDate"), costStaticsDTO.getEndDate()));
        }
        //默认时间查询
        if (!startDateFlag && !endDateFlag) {
            predicateList.add(builder.ge(root.get("createdDate"), DateUtils.getYearFirst(currentYear).getTime()));
            predicateList.add(builder.le(root.get("createdDate"), new Date().getTime()));
        }
        predicateList.add(builder.or(builder.equal(root.get("orderTrans"), OrderTrans.PAID), builder.equal(root.get("orderTrans"), OrderTrans.LIQUIDATION_COMPLETED)));
        predicateList.add(builder.equal(root.get("takeUser"), currentId));
        query.where(predicateList.toArray(new Predicate[predicateList.size()]));
        List<Object[]> result = entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    public List<Object[]> queryBondIncomeStatistics(Long currentId, CostStaticsDTO costStaticsDTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root1 = query.from(HbzBond.class);
        Root root2 = query.from(HbzBondGrade.class);
        query.select(builder.array(root2.get("bondType"), root2.get("grade"),builder.sum(root1.get("amount"))));
        query.groupBy(root2.get("bondType"), root2.get("grade"));
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(builder.equal(root2.get("id"), root1.get("bondGrade").get("id")));
        //默认开始时间为当前年的第一天
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        boolean startDateFlag = costStaticsDTO.getStartDate() != null && !"".equals(costStaticsDTO.getStartDate());
        boolean endDateFlag = costStaticsDTO.getEndDate() != null && !"".equals(costStaticsDTO.getEndDate());
        if (startDateFlag) {
            predicateList.add(builder.ge(root1.get("createdDate"), costStaticsDTO.getStartDate()));
        }
        if (endDateFlag) {
            predicateList.add(builder.le(root1.get("createdDate"), costStaticsDTO.getEndDate()));
        }
        //默认时间查询
        if (!startDateFlag && !endDateFlag) {
            predicateList.add(builder.ge(root1.get("createdDate"), DateUtils.getYearFirst(currentYear).getTime()));
            predicateList.add(builder.le(root1.get("createdDate"), new Date().getTime()));
        }
        predicateList.add(builder.or(builder.equal(root1.get("bondStatus"), 1), builder.equal(root1.get("bondStatus"), 2)));
        predicateList.add(builder.or(builder.and(builder.equal(root2.get("bondType"), "BOND_FSL"), builder.equal(root2.get("grade"), "D500")),builder.and(builder.equal(root2.get("bondType"), "BOND_LTL"), builder.equal(root2.get("grade"), "D200")),builder.and(builder.equal(root2.get("bondType"), "BOND_SL"), builder.equal(root2.get("grade"), "ED2000"))));
        predicateList.add(builder.equal(root1.get("user").get("id"), currentId));
        query.where(predicateList.toArray(new Predicate[predicateList.size()]));
        List<Object[]> result = entityManager.createQuery(query).getResultList();
        return result;
    }
}
