package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.StatisticsIncomePageParam;
import com.lxzl.erp.common.domain.statistics.StatisticsUnReceivablePageParam;
import com.lxzl.erp.common.domain.statistics.UnReceivablePageParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsIndexInfo;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private StatisticsService statisticsService;
}
