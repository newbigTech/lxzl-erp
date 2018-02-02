package com.lxzl.erp.core.service.statistics.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.constant.RentLengthType;
import com.lxzl.erp.common.constant.StatementDetailType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.statistics.HomeRentParam;
import com.lxzl.erp.common.domain.statistics.StatisticsIncomePageParam;
import com.lxzl.erp.common.domain.statistics.StatisticsUnReceivablePageParam;
import com.lxzl.erp.common.domain.statistics.UnReceivablePageParam;
import com.lxzl.erp.common.domain.statistics.pojo.*;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.amount.support.AmountSupport;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statistics.StatisticsMapper;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-24 16:25
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    @Override
    public ServiceResult<String, StatisticsIndexInfo> queryIndexInfo() {
        ServiceResult<String, StatisticsIndexInfo> result = new ServiceResult<>();
        StatisticsIndexInfo statisticsIndexInfo = new StatisticsIndexInfo();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("productEquipmentQueryParam", new ProductEquipmentQueryParam());
        Integer totalProductEquipmentCount = productEquipmentMapper.listCount(paramMap);
        statisticsIndexInfo.setTotalProductEquipmentCount(totalProductEquipmentCount);

        paramMap.put("customerQueryParam", new CustomerQueryParam());
        Integer totalCustomerCount = customerMapper.listCount(paramMap);
        statisticsIndexInfo.setTotalCustomerCount(totalCustomerCount);

        BigDecimal totalRentAmount = orderMapper.findPaidOrderAmount();
        statisticsIndexInfo.setTotalRentAmount(totalRentAmount);

        // 空参数
        paramMap.clear();
        paramMap.put("orderQueryParam", new OrderQueryParam());
        Integer totalOrderCount = orderMapper.findOrderCountByParams(paramMap);
        statisticsIndexInfo.setTotalOrderCount(totalOrderCount);

        List<Map<String, Object>> subCompanyRentAmountList = orderMapper.querySubCompanyOrderAmount(paramMap);
        Map<String, BigDecimal> subCompanyRentAmount = new HashMap<>();
        for (Map<String, Object> subCompanyRentAmountMap : subCompanyRentAmountList) {
            if (subCompanyRentAmountMap.get("total_order_amount") != null && subCompanyRentAmountMap.get("total_order_amount") instanceof BigDecimal) {
                subCompanyRentAmount.put(subCompanyRentAmountMap.get("sub_company_name").toString(), (BigDecimal) subCompanyRentAmountMap.get("total_order_amount"));
            } else {
                subCompanyRentAmount.put(subCompanyRentAmountMap.get("sub_company_name").toString(), BigDecimal.ZERO);
            }
        }
        statisticsIndexInfo.setSubCompanyRentAmount(subCompanyRentAmount);

        Map<Integer, BigDecimal> monthRentAmount = new HashMap<>();
        statisticsIndexInfo.setMonthRentAmount(monthRentAmount);

        result.setResult(statisticsIndexInfo);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsIncome> queryIncome(StatisticsIncomePageParam statisticsIncomePageParam) {
        ServiceResult<String, StatisticsIncome> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statisticsIncomePageParam.getPageNo(), statisticsIncomePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("incomeQueryParam", statisticsIncomePageParam);

        StatisticsIncome statisticsIncome = statisticsMapper.queryIncomeCount(maps);
        List<StatisticsIncomeDetail> statisticsIncomeDetailList = statisticsMapper.queryIncome(maps);
        Map<String, StatisticsIncomeDetail> statisticsIncomeDetailMap = ListUtil.listToMap(statisticsIncomeDetailList, "orderItemReferId", "orderItemType");
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForStatistics(maps);
        BigDecimal totalRent = BigDecimal.ZERO;    //总租金
        BigDecimal totalPrepayRent = BigDecimal.ZERO;    //总预付租金
        BigDecimal totalOtherPaid = BigDecimal.ZERO;    //总其他收入
        if (CollectionUtil.isNotEmpty(statementOrderDetailDOList)) {
            for (StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList) {
                String key = statementOrderDetailDO.getOrderItemReferId() + "-" + statementOrderDetailDO.getOrderItemType();
                //如果在返回列表中有数据，则处理租金及预付租金字段
                if (statisticsIncomeDetailMap.containsKey(key)) {
                    StatisticsIncomeDetail statisticsIncomeDetail = statisticsIncomeDetailMap.get(key);
                    //计算查询区间内租金费用
                    BigDecimal rentAmount = calculateRentAmount(statisticsIncomePageParam.getStartTime(), statisticsIncomePageParam.getEndTime(), statementOrderDetailDO);
                    //计算查询区间内预付租金费用
                    BigDecimal prepayRentAmount = calculatePrepayRentAmount(statisticsIncomePageParam.getEndTime(), statementOrderDetailDO);

                    statisticsIncomeDetail.setRentAmount(BigDecimalUtil.add(statisticsIncomeDetail.getRentAmount(), rentAmount));
                    statisticsIncomeDetail.setPrepayRentAmount(BigDecimalUtil.add(statisticsIncomeDetail.getPrepayRentAmount(), prepayRentAmount));
                    statisticsIncomeDetail.setOtherPaidAmount(BigDecimalUtil.add(statisticsIncomeDetail.getOtherPaidAmount(), statementOrderDetailDO.getStatementDetailOtherPaidAmount()));

                    BigDecimal in = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailOtherPaidAmount(),
                            BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailRentPaidAmount(),
                                    BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositPaidAmount(),
                                            statementOrderDetailDO.getStatementDetailRentDepositPaidAmount())));
                    BigDecimal out = BigDecimalUtil.add(statementOrderDetailDO.getStatementDetailDepositReturnAmount(), statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());

                    statisticsIncomeDetail.setIncomeAmount(BigDecimalUtil.sub(BigDecimalUtil.add(statisticsIncomeDetail.getIncomeAmount(), in), out));

                    //统计总租金及总预付租金
                    totalRent = BigDecimalUtil.add(totalRent, rentAmount);
                    totalPrepayRent = BigDecimalUtil.add(totalPrepayRent, prepayRentAmount);
                    totalOtherPaid = BigDecimalUtil.add(totalOtherPaid, statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                }
            }
        }
        statisticsIncome.setTotalRent(totalRent);
        statisticsIncome.setTotalPrepayRent(totalPrepayRent);
        statisticsIncome.setTotalOtherPaid(totalOtherPaid);
        BigDecimal in = BigDecimalUtil.add(statisticsIncome.getTotalPrepayRent(), BigDecimalUtil.add(statisticsIncome.getTotalOtherPaid(), BigDecimalUtil.add(statisticsIncome.getTotalRent(), BigDecimalUtil.add(statisticsIncome.getTotalDeposit(), statisticsIncome.getTotalRentDeposit()))));


        BigDecimal out = BigDecimalUtil.add(statisticsIncome.getTotalReturnDeposit(), statisticsIncome.getTotalReturnRentDeposit());
        statisticsIncome.setTotalIncome(BigDecimalUtil.sub(in, out));
        Page<StatisticsIncomeDetail> page = new Page<>(statisticsIncomeDetailList, statisticsIncome.getTotalCount(), statisticsIncomePageParam.getPageNo(), statisticsIncomePageParam.getPageSize());
        statisticsIncome.setStatisticsIncomeDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsIncome);
        return result;
    }

    @Override
    public ServiceResult<String, UnReceivable> queryUnReceivable(UnReceivablePageParam unReceivablePageParam) {
        ServiceResult<String, UnReceivable> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(unReceivablePageParam.getPageNo(), unReceivablePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("unReceivableQueryParam", unReceivablePageParam);

        UnReceivable unReceivable = statisticsMapper.queryUnReceivableCount(maps);
        List<UnReceivableDetail> statisticsIncomeDetailList = statisticsMapper.queryUnReceivable(maps);
        Page<UnReceivableDetail> page = new Page<>(statisticsIncomeDetailList, unReceivable.getTotalCount(), unReceivablePageParam.getPageNo(), unReceivablePageParam.getPageSize());
        unReceivable.setUnReceivableDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(unReceivable);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsUnReceivable> queryStatisticsUnReceivable(StatisticsUnReceivablePageParam statisticsUnReceivablePageParam) {
        ServiceResult<String, StatisticsUnReceivable> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(statisticsUnReceivablePageParam.getPageNo(), statisticsUnReceivablePageParam.getPageSize());
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("statisticsUnReceivablePageParam", statisticsUnReceivablePageParam);

        StatisticsUnReceivable statisticsUnReceivable = statisticsMapper.queryStatisticsUnReceivableCount(maps);
        List<StatisticsUnReceivableDetail> statisticsUnReceivableDetailList = statisticsMapper.queryStatisticsUnReceivable(maps);
        Page<StatisticsUnReceivableDetail> page = new Page<>(statisticsUnReceivableDetailList, statisticsUnReceivable.getTotalCount(), statisticsUnReceivablePageParam.getPageNo(), statisticsUnReceivablePageParam.getPageSize());
        statisticsUnReceivable.setStatisticsUnReceivableDetailPage(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsUnReceivable);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsUnReceivableForSubCompany> queryStatisticsUnReceivableForSubCompany() {
        ServiceResult<String, StatisticsUnReceivableForSubCompany> result = new ServiceResult<>();
        StatisticsUnReceivableForSubCompany statisticsUnReceivableForSubCompany = statisticsMapper.queryStatisticsUnReceivableCountForSubCompany();
        List<StatisticsUnReceivableDetailForSubCompany> statisticsUnReceivableDetailForSubCompanyList = statisticsMapper.queryStatisticsUnReceivableForSubCompany();
        statisticsUnReceivableForSubCompany.setStatisticsUnReceivableDetailForSubCompanyList(statisticsUnReceivableDetailForSubCompanyList);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(statisticsUnReceivableForSubCompany);
        return result;
    }

    @Override
    public ServiceResult<String, StatisticsHomeByRentLengthType> queryLongRent(HomeRentParam homeRentParam) {
        ServiceResult<String, StatisticsHomeByRentLengthType> serviceResult = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        homeRentParam.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_LONG);
        maps.put("homeRentParam", homeRentParam);
        StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = statisticsMapper.queryHomeByRentLengthType(maps);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForHome(maps);
        BigDecimal rentDeposit = BigDecimal.ZERO;//租金押金
        BigDecimal deposit = BigDecimal.ZERO;//设备押金
        BigDecimal returnRentDeposit = BigDecimal.ZERO;//退租金押金
        BigDecimal returnDeposit = BigDecimal.ZERO;//退设备押金
        BigDecimal rent = BigDecimal.ZERO;//租金
        BigDecimal prepayRent = BigDecimal.ZERO;//预付租金
        BigDecimal rentIncome = BigDecimal.ZERO;//租金收入
        BigDecimal otherAmount = BigDecimal.ZERO;//其他费用
        if(CollectionUtil.isNotEmpty(statementOrderDetailDOList)){
            for(StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList){
                rentDeposit = BigDecimalUtil.add(rentDeposit,statementOrderDetailDO.getStatementDetailRentDepositPaidAmount());
                deposit = BigDecimalUtil.add(deposit,statementOrderDetailDO.getStatementDetailDepositPaidAmount());
                //计算查询区间内租金费用
                BigDecimal rentAmount = calculateRentAmount(homeRentParam.getStartTime(), homeRentParam.getEndTime(), statementOrderDetailDO);
                //计算查询区间内预付租金费用
                BigDecimal prepayRentAmount = calculatePrepayRentAmount(homeRentParam.getEndTime(), statementOrderDetailDO);

                returnRentDeposit = BigDecimalUtil.add(returnRentDeposit,statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());
                returnDeposit = BigDecimalUtil.add(returnDeposit,statementOrderDetailDO.getStatementDetailDepositReturnAmount());
                rent = BigDecimalUtil.add(rent,rentAmount);
                prepayRent = BigDecimalUtil.add(prepayRent,prepayRentAmount);
                otherAmount = BigDecimalUtil.add(otherAmount,statementOrderDetailDO.getStatementDetailOtherPaidAmount());
                rentIncome = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.add(rentIncome,statementOrderDetailDO.getStatementDetailAmount()),statementOrderDetailDO.getStatementDetailDepositReturnAmount()),statementOrderDetailDO.getStatementDetailRentDepositReturnAmount());
            }
        }
        statisticsHomeByRentLengthType.setRentDeposit(rentDeposit);
        statisticsHomeByRentLengthType.setReturnRentDeposit(returnRentDeposit);
        statisticsHomeByRentLengthType.setReturnDeposit(returnDeposit);
        statisticsHomeByRentLengthType.setDeposit(deposit);
        statisticsHomeByRentLengthType.setRent(rent);
        statisticsHomeByRentLengthType.setPrepayRent(prepayRent);
        statisticsHomeByRentLengthType.setOtherAmount(otherAmount);
        statisticsHomeByRentLengthType.setRentIncome(rentIncome);
        //净增台数(新增减退租)
        statisticsHomeByRentLengthType.setIncreaseProductCount(statisticsHomeByRentLengthType.getProductCountByNewCustomer()+statisticsHomeByRentLengthType.getProductCountByOldCustomer()-statisticsHomeByRentLengthType.getReturnProductCount());
        statisticsHomeByRentLengthType.setTotalOrderCount(statisticsHomeByRentLengthType.getOrderCountByNewCustomer()+statisticsHomeByRentLengthType.getOrderCountByOldCustomer());
        statisticsHomeByRentLengthType.setTotalProductCount(statisticsHomeByRentLengthType.getProductCountByNewCustomer()+statisticsHomeByRentLengthType.getProductCountByOldCustomer());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsHomeByRentLengthType);
        return serviceResult;
    }
    @Override
    public ServiceResult<String, StatisticsHomeByRentLengthType> queryShortRent(HomeRentParam homeRentParam) {
        ServiceResult<String, StatisticsHomeByRentLengthType> serviceResult = new ServiceResult<>();
        Map<String, Object> maps = new HashMap<>();
        homeRentParam.setRentLengthType(RentLengthType.RENT_LENGTH_TYPE_SHORT);
        maps.put("homeRentParam", homeRentParam);
        StatisticsHomeByRentLengthType statisticsHomeByRentLengthType = statisticsMapper.queryHomeByRentLengthType(maps);
        List<StatementOrderDetailDO> statementOrderDetailDOList = statementOrderDetailMapper.listAllForHome(maps);
        BigDecimal rentIncome = BigDecimal.ZERO;//租金收入
        if(CollectionUtil.isNotEmpty(statementOrderDetailDOList)){
            for(StatementOrderDetailDO statementOrderDetailDO : statementOrderDetailDOList){
                if(StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
                    rentIncome = BigDecimalUtil.add(rentIncome,statementOrderDetailDO.getStatementDetailAmount());
                }
            }
        }
        statisticsHomeByRentLengthType.setRentIncome(rentIncome);
        statisticsHomeByRentLengthType.setTotalOrderCount(statisticsHomeByRentLengthType.getOrderCountByNewCustomer()+statisticsHomeByRentLengthType.getOrderCountByOldCustomer());
        statisticsHomeByRentLengthType.setTotalProductCount(statisticsHomeByRentLengthType.getProductCountByNewCustomer()+statisticsHomeByRentLengthType.getProductCountByOldCustomer());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(statisticsHomeByRentLengthType);
        return serviceResult;
    }
    private BigDecimal calculateRentAmount(Date startTime, Date endTime, StatementOrderDetailDO statementOrderDetailDO) {
        //比较日期大小确定统计起始时间，结束时间
        //起始时间为MAX[统计起始时间，结算开始时间]
        //结束时间为MIN[统计结束时间，结算结束时间]
        if(!StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
            return BigDecimal.ZERO;
        }
        Date start = startTime.compareTo(statementOrderDetailDO.getStatementStartTime()) > 0 ? startTime : statementOrderDetailDO.getStatementStartTime();
        Date end = endTime.compareTo(statementOrderDetailDO.getStatementEndTime()) < 0 ? endTime : statementOrderDetailDO.getStatementEndTime();

        if (OrderRentType.RENT_TYPE_MONTH.equals(statementOrderDetailDO.getRentType())) {
            return BigDecimalUtil.mul(amountSupport.calculateRentAmount(start, end, statementOrderDetailDO.getGoodsUnitAmount()), new BigDecimal(statementOrderDetailDO.getGoodsCount()));
        } else if (OrderRentType.RENT_TYPE_DAY.equals(statementOrderDetailDO.getRentType())) {
            //计算两日期时间差
            Integer dayCount = DateUtil.daysBetween(start, end);
            return BigDecimalUtil.mul(statementOrderDetailDO.getGoodsUnitAmount(), new BigDecimal(dayCount));
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculatePrepayRentAmount(Date endTime, StatementOrderDetailDO statementOrderDetailDO) {
        //如果结算单结束时间小于等于统计结束时间，则预付租金为0
        if (DateUtil.daysBetween(statementOrderDetailDO.getStatementEndTime(), endTime) >= 0) {
            return BigDecimal.ZERO;
        }
        if(!StatementDetailType.STATEMENT_DETAIL_TYPE_RENT.equals(statementOrderDetailDO.getStatementDetailType())){
            return BigDecimal.ZERO;
        }
        BigDecimal amount = BigDecimalUtil.mul(amountSupport.calculateRentAmount(statementOrderDetailDO.getStatementStartTime(), endTime, statementOrderDetailDO.getGoodsUnitAmount()), new BigDecimal(statementOrderDetailDO.getGoodsCount()));
//        System.out.println("结算金额已交的为"+statementOrderDetailDO.getStatementDetailRentPaidAmount()+"元");
//        System.out.println("结算日起，至统计结束时间为止，需交金额为"+amount+"元");
//        System.out.println("预付"+BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(),amount)+"元");
        BigDecimal prepayRentAmount = BigDecimalUtil.sub(statementOrderDetailDO.getStatementDetailRentPaidAmount(), amount);
        return BigDecimalUtil.compare(prepayRentAmount, BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : prepayRentAmount;
    }

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private StatementOrderDetailMapper statementOrderDetailMapper;
    @Autowired
    private AmountSupport amountSupport;
}
