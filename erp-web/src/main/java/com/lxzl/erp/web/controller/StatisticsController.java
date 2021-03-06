package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.StatisticsIntervalType;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.*;
import com.lxzl.erp.common.domain.statistics.pojo.FinanceStatisticsDataWeeklyExcel;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIndexInfo;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsOperateData;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsSalesmanMonth;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsDataMeta;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-25 17:07
 */
@Controller
@ControllerLog
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    @RequestMapping(value = "queryIndexInfo", method = RequestMethod.POST)
    public Result queryIndexInfo(@RequestBody StatisticsIndexInfo statisticsIndexInfo, BindingResult validResult) {
        ServiceResult<String, StatisticsIndexInfo> serviceResult = statisticsService.queryIndexInfo();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 收入统计
     * @param statisticsIncomePageParam
     * @return
     */
    @RequestMapping(value = "queryIncome", method = RequestMethod.POST)
    public Result queryIncome(@RequestBody @Validated StatisticsIncomePageParam statisticsIncomePageParam, BindingResult validResult) {
        return resultGenerator.generate(statisticsService.queryIncome(statisticsIncomePageParam));
    }

    /**
     * 未收明细
     * @param unReceivablePageParam
     * @return
     */
    @RequestMapping(value = "queryUnReceivable", method = RequestMethod.POST)
    public Result queryUnReceivable(@RequestBody UnReceivablePageParam unReceivablePageParam) {
        return resultGenerator.generate(statisticsService.queryUnReceivable(unReceivablePageParam));
    }

    /**
     * 未收统计
     * @param statisticsUnReceivablePageParam
     * @return
     */
    @RequestMapping(value = "queryStatisticsUnReceivable", method = RequestMethod.POST)
    public Result queryStatisticsUnReceivable(@RequestBody StatisticsUnReceivablePageParam statisticsUnReceivablePageParam) {
        return resultGenerator.generate(statisticsService.queryStatisticsUnReceivable(statisticsUnReceivablePageParam));
    }
    /**
     * 未收汇总，分公司维度
     * @return
     */
    @RequestMapping(value = "queryStatisticsUnReceivableForSubCompany", method = RequestMethod.POST)
    public Result queryStatisticsUnReceivableForSubCompany() {
        return resultGenerator.generate(statisticsService.queryStatisticsUnReceivableForSubCompany());
    }
    /**
     * 设备长租统计
     * @param homeRentParam
     * @return
     */
    @RequestMapping(value = "queryLongRent", method = RequestMethod.POST)
    public Result queryLongRent(@RequestBody @Validated HomeRentParam homeRentParam, BindingResult validResult) {
        return resultGenerator.generate(statisticsService.queryLongRent(homeRentParam));
    }
    /**
     * 设备短租统计
     * @param homeRentParam
     * @return
     */
    @RequestMapping(value = "queryShortRent", method = RequestMethod.POST)
    public Result queryShortRent(@RequestBody @Validated HomeRentParam homeRentParam, BindingResult validResult) {
        return resultGenerator.generate(statisticsService.queryShortRent(homeRentParam));
    }
    /**
     * 设备长租统计（时间维度）
     * @param homeRentByTimeParam
     * @return
     */
    @RequestMapping(value = "queryLongRentByTime", method = RequestMethod.POST)
    public Result queryLongRentByTime(@RequestBody @Validated HomeRentByTimeParam homeRentByTimeParam, BindingResult validResult) {
        return resultGenerator.generate(statisticsService.queryLongRentByTime(homeRentByTimeParam));
    }
    /**
     * 设备短租统计（时间维度）
     * @param homeRentByTimeParam
     * @return
     */
    @RequestMapping(value = "queryShortRentByTime", method = RequestMethod.POST)
    public Result queryShortRentByTime(@RequestBody @Validated HomeRentByTimeParam homeRentByTimeParam, BindingResult validResult) {
        return resultGenerator.generate(statisticsService.queryShortRentByTime(homeRentByTimeParam));
    }
    /**
     * 待收明细
     * @param awaitReceivablePageParam
     * @return
     */
    @RequestMapping(value = "queryAwaitReceivable", method = RequestMethod.POST)
    public Result queryAwaitReceivable(@RequestBody AwaitReceivablePageParam awaitReceivablePageParam) {
        return resultGenerator.generate(statisticsService.queryAwaitReceivable(awaitReceivablePageParam));
    }

    /**
     * 待收统计
     * @param statisticsAwaitReceivablePageParam
     * @return
     */
    @RequestMapping(value = "queryStatisticsAwaitReceivable", method = RequestMethod.POST)
    public Result queryStatisticsAwaitReceivable(@RequestBody StatisticsAwaitReceivablePageParam statisticsAwaitReceivablePageParam) {
        return resultGenerator.generate(statisticsService.queryStatisticsAwaitReceivable(statisticsAwaitReceivablePageParam));
    }

    /**
     * 业务员提成数据查询
     * @param statisticsSalesmanPageParam
     * @return
     */
    @RequestMapping(value = "querySalesman", method = RequestMethod.POST)
    public Result querySalesman(@RequestBody @Validated StatisticsSalesmanPageParam statisticsSalesmanPageParam) {
        return resultGenerator.generate(statisticsService.querySalesman(statisticsSalesmanPageParam));
    }

    /**
     * 长短租详细统计
     * @param statisticsRentInfoPageParam
     * @return
     */
    @RequestMapping(value = "queryRentInfo", method = RequestMethod.POST)
    public Result queryRentInfo(@RequestBody @Validated StatisticsRentInfoPageParam statisticsRentInfoPageParam) {
        return resultGenerator.generate(statisticsService.queryRentInfo(statisticsRentInfoPageParam));
    }

    /**
     * 确认业务员提成统计月结信息
     * @param statisticsSalesmanMonth
     * @return
     */
    @RequestMapping(value = "updateStatisticsSalesmanMonth", method = RequestMethod.POST)
    public Result updateStatisticsSalesmanMonth(@RequestBody @Validated({UpdateGroup.class,IdGroup.class})  StatisticsSalesmanMonth statisticsSalesmanMonth, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = statisticsService.updateStatisticsSalesmanMonth(statisticsSalesmanMonth);
        return resultGenerator.generate(serviceResult);
    }
//    /**
//     * 创建业务员提成统计月结信息
//     * @param date
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "createStatisticsSalesmanMonth",method = RequestMethod.POST)
//    public Result createStatisticsSalesmanMonth(@RequestBody @Validated Date date, BindingResult validResult){
//        ServiceResult<String, String> serviceResult = statisticsService.createStatisticsSalesmanMonth(date);
//        return resultGenerator.generate(serviceResult);
//    }

    /**
     * 统计财务本周数据
     * @return
     */
    @RequestMapping(value = "statisticsFinanceDataWeeklyNow", method = RequestMethod.POST)
    public Result statisticsFinanceDataWeeklyNow() {
        ServiceResult<String, Boolean> serviceResult = statisticsService.statisticsFinanceDataWeeklyNow();
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 重新统计历史财务周数据
     * @return
     */
    @RequestMapping(value = "reStatisticsFinanceDataWeekly", method = RequestMethod.POST)
    public Result reStatisticsFinanceDataWeekly(@RequestBody @Validated FinanceStatisticsParam financeStatisticsParam, BindingResult validResult) {
        if (financeStatisticsParam != null) {
            financeStatisticsParam.setStatisticsInterval(StatisticsIntervalType.STATISTICS_INTERVAL_WEEKLY);
        }
        ServiceResult<String, Boolean> serviceResult = statisticsService.reStatisticsFinanceDataWeekly(financeStatisticsParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 重新统计历史财务数据
     * @return
     */
    @RequestMapping(value = "reStatisticsFinanceData", method = RequestMethod.POST)
    public Result reStatisticsFinanceData(@RequestBody @Validated FinanceStatisticsParam financeStatisticsParam, BindingResult validResult) {

        ServiceResult<String, Boolean> serviceResult = statisticsService.reStatisticsFinanceData(financeStatisticsParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 获取所有财务统计数据（分页）
     * @return
     */
    @RequestMapping(value = "getStatisticsFinanceDataListPagable", method = RequestMethod.POST)
    public Result getStatisticsFinanceDataListPagable(@RequestBody @Validated FinanceStatisticsParam financeStatisticsParam, BindingResult validResult) {
        ServiceResult<String, Page<FinanceStatisticsDataMeta>> serviceResult = statisticsService.findAllStatisticsFinanceDataMeta(financeStatisticsParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 获取财务统计数据详情
     * @return
     */
    @RequestMapping(value = "getStatisticsFinanceDataDetail", method = RequestMethod.POST)
    public Result getStatisticsFinanceDataDetail(@RequestBody @Validated FinanceStatisticsParam financeStatisticsParam, BindingResult validResult) {
        ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> serviceResult = statisticsService.findStatisticsFinanceDataDetail(financeStatisticsParam);
        return resultGenerator.generate(serviceResult);
    }
    /**
     * 生成经营数据记录(定时任务调度)
     * @param date
     * @param validResult
     * @return
     */
    @RequestMapping(value = "createStatisticsOperateData",method = RequestMethod.POST)
    public Result createStatisticsOperateData(@RequestBody @Validated Date date, BindingResult validResult){
        ServiceResult<String, String> serviceResult = statisticsService.createStatisticsOperateData(date);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 查询日经营数据
     * @param statisticsOperateDataPageParam
     * @return
     */
    @RequestMapping(value = "queryStatisticsOperateDataForDay", method = RequestMethod.POST)
    public Result queryStatisticsOperateDataForDay(@RequestBody StatisticsOperateDataPageParam statisticsOperateDataPageParam) {
        ServiceResult<String, Page<StatisticsOperateData>> serviceResult = statisticsService.queryStatisticsOperateDataForDay(statisticsOperateDataPageParam);
        return resultGenerator.generate(serviceResult);
    }
    /**
     * 查询周经营数据
     * @param statisticsOperateDataPageParam
     * @return
     */
    @RequestMapping(value = "queryStatisticsOperateDataForWeek", method = RequestMethod.POST)
    public Result queryStatisticsOperateDataForWeek(@RequestBody StatisticsOperateDataPageParam statisticsOperateDataPageParam) {
        ServiceResult<String, Page<StatisticsOperateData>> serviceResult = statisticsService.queryStatisticsOperateDataForWeek(statisticsOperateDataPageParam);
        return resultGenerator.generate(serviceResult);
    }
    /**
     * 查询月经营数据
     * @param statisticsOperateDataPageParam
     * @return
     */
    @RequestMapping(value = "queryStatisticsOperateDataForMonth", method = RequestMethod.POST)
    public Result queryStatisticsOperateDataForMonth(@RequestBody StatisticsOperateDataPageParam statisticsOperateDataPageParam) {
        ServiceResult<String, Page<StatisticsOperateData>> serviceResult = statisticsService.queryStatisticsOperateDataForMonth(statisticsOperateDataPageParam);
        return resultGenerator.generate(serviceResult);
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private StatisticsService statisticsService;
}
